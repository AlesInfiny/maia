"""CI でのドキュメントビルド用に zensical.toml の設定値を書き換えます。

zensical.toml では環境変数を参照できないため、ビルド環境に応じて変更する設定値を
環境変数から読み取って書き換えます。

- VERSION: project.extra.version に設定するバージョン。
- GOOGLE_ANALYTICS_PROVIDER: project.extra.analytics.provider に設定する値。
- GOOGLE_ANALYTICS_PROPERTY: project.extra.analytics.property に設定する値。

あわせて、 project.plugins.social.enabled と project.plugins.rss.enabled を true に設定します。

使い方: python apply-release-settings.py <zensical.toml のパス>
"""

import json
import os
import re
import sys
import tomllib


def replace_in_table(text: str, table: str, key: str, value: object) -> str:
    """指定したテーブル直下のキーの値を置き換えます。"""
    table_pattern = re.compile(rf"^\[{re.escape(table)}\][ \t]*$", re.MULTILINE)
    table_match = table_pattern.search(text)
    if table_match is None:
        raise ValueError(f"テーブル [{table}] が見つかりません。")

    # 次のテーブル定義までをテーブルの範囲とする。
    next_table = re.compile(r"^\[", re.MULTILINE).search(text, table_match.end())
    end = next_table.start() if next_table else len(text)

    key_pattern = re.compile(rf"^({re.escape(key)}[ \t]*=[ \t]*).*$", re.MULTILINE)
    section = text[table_match.end() : end]
    new_section, count = key_pattern.subn(
        lambda m: m.group(1) + json.dumps(value, ensure_ascii=False), section, count=1
    )
    if count == 0:
        raise ValueError(f"[{table}] に {key} が見つかりません。")

    return text[: table_match.end()] + new_section + text[end:]


def main() -> None:
    path = sys.argv[1]
    with open(path, encoding="utf-8") as f:
        text = f.read()

    settings = [
        ("project.extra", "version", os.environ.get("VERSION", "Local Version")),
        ("project.extra.analytics", "provider", os.environ.get("GOOGLE_ANALYTICS_PROVIDER", "")),
        ("project.extra.analytics", "property", os.environ.get("GOOGLE_ANALYTICS_PROPERTY", "")),
        ("project.plugins.social", "enabled", True),
        ("project.plugins.rss", "enabled", True),
    ]
    for table, key, value in settings:
        text = replace_in_table(text, table, key, value)

    # 書き換え後の内容が TOML として正しく、意図した値になっていることを確認する。
    config = tomllib.loads(text)
    for table, key, value in settings:
        actual = config
        for name in table.split(".") + [key]:
            actual = actual[name]
        if actual != value:
            raise ValueError(f"{table}.{key} の書き換えに失敗しました。")

    with open(path, "w", encoding="utf-8", newline="\n") as f:
        f.write(text)


if __name__ == "__main__":
    main()
