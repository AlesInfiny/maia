#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
Semgrep native JSON (top-level "results") -> CSV ledger

This script is robust to redacted outputs where:
  extra.lines == "requires login"
  extra.fingerprint == "requires login"

In that case, it extracts a snippet directly from the source file using
path + start/end line, so you can build a ledger without Semgrep Cloud login.

Usage:
  semgrep scan --config .semgrep --json --output semgrep.json
  python semgrep_to_csv.py semgrep.json -o out --repo-root .

Stdin:
  cat semgrep.json | python semgrep_to_csv.py - -o out --repo-root .
"""

from __future__ import annotations

import argparse
import csv
import json
import os
import re
from dataclasses import dataclass, asdict
from pathlib import Path
from typing import Any, Dict, List


REDACTED_SENTINELS = {"requires login", "REQUIRES LOGIN", "Requires login"}


@dataclass
class Finding:
    severity: str
    api_kind: str
    engine: str
    file: str
    start_line: int
    end_line: int
    start_col: int
    end_col: int
    rule_id: str
    message: str
    code: str  # snippet; may be extracted from file


def _safe_get(d: Dict[str, Any], path: List[str], default=None):
    cur: Any = d
    for key in path:
        if not isinstance(cur, dict) or key not in cur:
            return default
        cur = cur[key]
    return cur


def _normalize_path(p: str) -> str:
    """
    Normalize path separators so JSON produced on Windows can be used anywhere.
    Keep it repo-relative if it already is.
    """
    p = (p or "").strip()
    return p.replace("\\", "/")


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
    if "RegExUtils" in code or "org.apache.commons.lang3" in code:
        return "java.util.regex (via commons-lang3)"
    return "unknown"


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
    """
    Extract a code snippet from source file using line range with some context.
    Returns "" if file can't be read.
    """
    rel_path = _normalize_path(rel_path)

    # Try repo-relative path first
    p = (repo_root / rel_path).resolve()
    if not p.exists():
        # Fallback: treat JSON path as absolute or cwd-relative
        p2 = Path(rel_path)
        if p2.exists():
            p = p2.resolve()
        else:
            return ""

    try:
        text = p.read_text(encoding="utf-8", errors="replace")
    except Exception:
        return ""

    lines = text.splitlines()
    if start_line <= 0 or start_line > len(lines):
        return ""

    # Basic region
    s = max(1, start_line - context)
    e = min(len(lines), (end_line if end_line > 0 else start_line) + context)

    # Clamp to max_lines
    if (e - s + 1) > max_lines:
        e = min(len(lines), s + max_lines - 1)

    snippet = "\n".join(lines[s - 1 : e])
    return snippet.strip("\n")


def _normalize_for_csv(code: str) -> str:
    # Put snippet into a single CSV cell
    return re.sub(r"\s*\n\s*", " ⏎ ", (code or "").strip()).strip()


def _is_redacted(value: str) -> bool:
    v = (value or "").strip()
    if not v:
        return False
    if v in REDACTED_SENTINELS:
        return True
    if v.lower() == "requires login":
        return True
    return False


def _extract_findings(doc: Dict[str, Any], repo_root: Path) -> List[Finding]:
    results = doc.get("results", []) or []
    findings: List[Finding] = []

    for r in results:
        rule_id = r.get("check_id") or r.get("rule_id") or ""
        extra = r.get("extra", {}) or {}
        severity = (extra.get("severity") or "INFO").upper()
        message = str(extra.get("message") or "").strip()

        path_raw = r.get("path") or _safe_get(r, ["location", "path"], "") or ""
        path = _normalize_path(str(path_raw))

        start = r.get("start") or _safe_get(r, ["location", "start"], {}) or {}
        end = r.get("end") or _safe_get(r, ["location", "end"], {}) or {}

        start_line = int(start.get("line") or 0)
        start_col = int(start.get("col") or 0)
        end_line = int(end.get("line") or start_line)
        end_col = int(end.get("col") or start_col)

        # Prefer Semgrep snippet, but handle redacted outputs
        code = str(extra.get("lines") or "").strip("\n")
        if (not code) or _is_redacted(code):
            code = _extract_snippet_from_file(repo_root, path, start_line, end_line)

        api_kind = _guess_api_kind(str(rule_id), message, code)
        engine = _guess_engine(code)

        findings.append(
            Finding(
                severity=severity,
                api_kind=api_kind,
                engine=engine,
                file=path,
                start_line=start_line,
                end_line=end_line,
                start_col=start_col,
                end_col=end_col,
                rule_id=str(rule_id),
                message=message,
                code=_normalize_for_csv(code),
            )
        )

    findings.sort(key=lambda x: (x.file, x.start_line, x.rule_id))
    return findings


def _write_csv(findings: List[Finding], out_csv: Path) -> None:
    out_csv.parent.mkdir(parents=True, exist_ok=True)
    fieldnames = [
        "severity",
        "api_kind",
        "engine",
        "file",
        "start_line",
        "end_line",
        "start_col",
        "end_col",
        "rule_id",
        "message",
        "code",
    ]
    with open(out_csv, "w", encoding="utf-8", newline="") as f:
        w = csv.DictWriter(f, fieldnames=fieldnames)
        w.writeheader()
        for x in findings:
            row = asdict(x)
            w.writerow({k: row.get(k, "") for k in fieldnames})


def main() -> int:
    ap = argparse.ArgumentParser()
    ap.add_argument("semgrep_json", help="Semgrep native JSON with top-level 'results' (use '-' for stdin)")
    ap.add_argument("-o", "--outdir", default="out", help="Output directory (default: out)")
    ap.add_argument("--csv-name", default="regex_ledger.csv", help="CSV filename (default: regex_ledger.csv)")
    ap.add_argument(
        "--repo-root",
        default=".",
        help="Repository root path used to read source files for snippets (default: .)",
    )
    args = ap.parse_args()

    doc = _load_json(args.semgrep_json)
    if not isinstance(doc, dict) or "results" not in doc:
        raise SystemExit("This script expects Semgrep native JSON with top-level 'results'.")

    findings = _extract_findings(doc, Path(args.repo_root))
    out_csv = Path(args.outdir) / args.csv_name
    _write_csv(findings, out_csv)

    print(f"[OK] wrote: {out_csv.as_posix()}")
    print(f"[OK] findings: {len(findings)}")
    return 0


if __name__ == "__main__":
    raise SystemExit(main())
