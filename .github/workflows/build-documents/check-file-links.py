"""ビルドしたドキュメント内の、ページ以外のファイルへのリンク切れを検出します。

Zensical のビルドは Markdown ページとアンカーへのリンクのみを検証し、
画像や zip ファイルなどへのリンク切れを検出しないため、
ビルド成果物の HTML に含まれる相対リンクの参照先が存在することを確認します。

使い方: python check-file-links.py <ビルド成果物のフォルダーのパス>
"""

import os
import sys
from html.parser import HTMLParser
from urllib.parse import unquote, urlsplit


class LinkCollector(HTMLParser):
    """HTML から href 属性と src 属性の値を収集します。"""

    def __init__(self) -> None:
        super().__init__()
        self.links: list[str] = []

    def handle_starttag(self, tag: str, attrs: list[tuple[str, str | None]]) -> None:
        for name, value in attrs:
            if name in ("href", "src") and value:
                self.links.append(value)


def is_file_link(link: str) -> bool:
    """ページ以外のファイルを参照する相対リンクかどうかを判定します。"""
    url = urlsplit(link)
    if url.scheme or url.netloc or not url.path:
        return False
    # ディレクトリ形式の URL や HTML ページへのリンクは Zensical のビルドで検証する。
    if url.path.endswith("/") or url.path.endswith(".html"):
        return False
    return os.path.splitext(url.path)[1] != ""


def main() -> None:
    sys.stdout.reconfigure(encoding="utf-8")
    site_dir = os.path.abspath(sys.argv[1])
    broken: list[tuple[str, str]] = []

    for root, _, files in os.walk(site_dir):
        for file in files:
            if not file.endswith(".html"):
                continue
            page = os.path.join(root, file)
            collector = LinkCollector()
            with open(page, encoding="utf-8") as f:
                collector.feed(f.read())
            for link in collector.links:
                if not is_file_link(link):
                    continue
                path = unquote(urlsplit(link).path)
                base = site_dir if path.startswith("/") else root
                target = os.path.normpath(os.path.join(base, path.lstrip("/")))
                if not os.path.isfile(target):
                    broken.append((os.path.relpath(page, site_dir), link))

    for page, link in sorted(set(broken)):
        print(f"リンク切れ: {page} -> {link}")

    if broken:
        sys.exit(1)
    print("ファイルへのリンク切れはありません。")


if __name__ == "__main__":
    main()
