#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
Semgrep native JSON (top-level "results") -> Markdown ledger

- Robust to redacted outputs:
    extra.lines == "requires login"
- Extracts code snippets directly from source files using path + line range
- Intended for review / audit (ReDoS investigation, etc.)

Usage:
  semgrep scan --config .semgrep --json --output semgrep.json
  python semgrep_to_md.py semgrep.json -o out --repo-root .
"""

from __future__ import annotations

import argparse
import json
import os
from dataclasses import dataclass
from pathlib import Path
from typing import Any, Dict, List
from datetime import datetime


REDACTED_SENTINELS = {"requires login", "REQUIRES LOGIN", "Requires login"}


@dataclass
class Finding:
    severity: str
    api_kind: str
    engine: str
    file: str
    start_line: int
    end_line: int
    rule_id: str
    message: str
    code: str


def _safe_get(d: Dict[str, Any], path: List[str], default=None):
    cur: Any = d
    for key in path:
        if not isinstance(cur, dict) or key not in cur:
            return default
        cur = cur[key]
    return cur


def _normalize_path(p: str) -> str:
    return (p or "").replace("\\", "/")


def _guess_api_kind(rule_id: str, message: str, code: str) -> str:
    text = " ".join([rule_id or "", message or "", code or ""]).lower()

    if "@pattern" in text:
        return "@Pattern (Bean Validation)"
    if "pattern.compile" in text:
        return "Pattern.compile"
    if ".matches(" in text:
        return "String.matches"
    if ".split(" in text:
        return "String.split"
    if ".replaceall(" in text:
        return "String.replaceAll"
    if ".replacefirst(" in text:
        return "String.replaceFirst"
    if "regexrequestmatcher" in text:
        return "Spring Security RegexRequestMatcher"
    if "regexutils" in text:
        return "commons-lang3 RegExUtils"
    if "@requestmapping" in text or "@getmapping" in text or "@postmapping" in text:
        return "Spring MVC Mapping (may include {var:regex})"

    return "regex usage (unknown)"


def _guess_engine(code: str) -> str:
    if "com.google.re2j" in code:
        return "re2j"
    if "java.util.regex" in code or "Pattern.compile" in code:
        return "java.util.regex"
    if "RegExUtils" in code:
        return "java.util.regex (via commons-lang3)"
    return "unknown"


def _is_redacted(v: str) -> bool:
    return (v or "").strip() in REDACTED_SENTINELS


def _load_json(fp: str) -> Dict[str, Any]:
    if fp == "-":
        return json.load(os.sys.stdin)
    with open(fp, "r", encoding="utf-8") as f:
        return json.load(f)


def _extract_snippet_from_file(
    repo_root: Path,
    rel_path: str,
    start_line: int,
    end_line: int,
    *,
    context: int = 2,
    max_lines: int = 60,
) -> str:
    rel_path = _normalize_path(rel_path)
    p = (repo_root / rel_path).resolve()
    if not p.exists():
        return ""

    try:
        text = p.read_text(encoding="utf-8", errors="replace")
    except Exception:
        return ""

    lines = text.splitlines()
    if start_line <= 0 or start_line > len(lines):
        return ""

    s = max(1, start_line - context)
    e = min(len(lines), (end_line if end_line > 0 else start_line) + context)

    if (e - s + 1) > max_lines:
        e = min(len(lines), s + max_lines - 1)

    return "\n".join(lines[s - 1 : e]).strip("\n")


def _extract_findings(doc: Dict[str, Any], repo_root: Path) -> List[Finding]:
    results = doc.get("results", []) or []
    out: List[Finding] = []

    for r in results:
        rule_id = r.get("check_id") or ""
        extra = r.get("extra", {}) or {}
        severity = (extra.get("severity") or "INFO").upper()
        message = str(extra.get("message") or "").strip()

        path = _normalize_path(str(r.get("path") or ""))
        start = r.get("start") or {}
        end = r.get("end") or {}

        start_line = int(start.get("line") or 0)
        end_line = int(end.get("line") or start_line)

        code = str(extra.get("lines") or "").strip("\n")
        if not code or _is_redacted(code):
            code = _extract_snippet_from_file(repo_root, path, start_line, end_line)

        api_kind = _guess_api_kind(rule_id, message, code)
        engine = _guess_engine(code)

        out.append(
            Finding(
                severity=severity,
                api_kind=api_kind,
                engine=engine,
                file=path,
                start_line=start_line,
                end_line=end_line,
                rule_id=rule_id,
                message=message,
                code=code,
            )
        )

    out.sort(key=lambda x: (x.file, x.start_line, x.rule_id))
    return out


def _write_markdown(findings: List[Finding], out_md: Path) -> None:
    out_md.parent.mkdir(parents=True, exist_ok=True)

    lines: List[str] = []
    lines.append("# Regex Usage Ledger (Semgrep)")
    lines.append("")
    lines.append(f"- Generated: {datetime.now().strftime('%Y-%m-%d %H:%M:%S')}")
    lines.append(f"- Findings: {len(findings)}")
    lines.append("")

    # Summary
    by_kind: Dict[str, int] = {}
    for f in findings:
        by_kind[f.api_kind] = by_kind.get(f.api_kind, 0) + 1

    lines.append("## Summary")
    lines.append("")
    lines.append("| API kind | Count |")
    lines.append("|---|---:|")
    for k, v in sorted(by_kind.items(), key=lambda x: (-x[1], x[0])):
        lines.append(f"| {k} | {v} |")
    lines.append("")

    # Details
    lines.append("## Details")
    lines.append("")

    current_file = None
    for f in findings:
        if f.file != current_file:
            current_file = f.file
            lines.append(f"### `{current_file}`")
            lines.append("")

        lines.append(
            f"- **{f.api_kind}**  \n"
            f"  - Location: `{f.file}:{f.start_line}-{f.end_line}`  \n"
            f"  - Severity: `{f.severity}`  \n"
            f"  - Engine: `{f.engine}`  \n"
            f"  - Rule: `{f.rule_id}`"
        )
        if f.message:
            lines.append(f"  - Message: {f.message}")
        lines.append("")
        lines.append("```java")
        lines.append(f.code or "")
        lines.append("```")
        lines.append("")

    out_md.write_text("\n".join(lines), encoding="utf-8")


def main() -> int:
    ap = argparse.ArgumentParser()
    ap.add_argument("semgrep_json", help="Semgrep native JSON (use '-' for stdin)")
    ap.add_argument("-o", "--outdir", default="out", help="Output directory (default: out)")
    ap.add_argument("--md-name", default="regex_ledger.md", help="Markdown filename")
    ap.add_argument("--repo-root", default=".", help="Repository root (default: .)")
    args = ap.parse_args()

    doc = _load_json(args.semgrep_json)
    if "results" not in doc:
        raise SystemExit("Expected Semgrep native JSON with top-level 'results'.")

    findings = _extract_findings(doc, Path(args.repo_root))
    out_md = Path(args.outdir) / args.md_name
    _write_markdown(findings, out_md)

    print(f"[OK] wrote: {out_md.as_posix()}")
    print(f"[OK] findings: {len(findings)}")
    return 0


if __name__ == "__main__":
    raise SystemExit(main())
