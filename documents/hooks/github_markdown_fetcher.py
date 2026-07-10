import re
import time
import urllib.error
import urllib.request

MAX_RETRIES = 3
TIMEOUT_SECONDS = 10


def fetch_url(url):
    for attempt in range(MAX_RETRIES + 1):
        try:
            with urllib.request.urlopen(url, timeout=TIMEOUT_SECONDS) as response:
                return response.read().decode('utf-8')
        except urllib.error.HTTPError as error:
            if error.code != 429 or attempt == MAX_RETRIES:
                raise RuntimeError(f"Failed to fetch GitHub markdown: {url}") from error

            retry_after = error.headers.get('Retry-After')
            try:
                wait_seconds = int(retry_after) if retry_after else 2 ** attempt
            except ValueError:
                wait_seconds = 2 ** attempt

            time.sleep(wait_seconds)
        except urllib.error.URLError as error:
            raise RuntimeError(f"Failed to fetch GitHub markdown: {url}") from error


def replacer(match):
    filename = f"{match.group('filename')}.{match.group('extension')}"
    url = f"https://raw.githubusercontent.com/{match.group('user')}/{filename}"
    code = fetch_url(url)
    spacing = match.group('spacing')
    
    # begin と end が整数に変換できるかどうかをチェック
    try:
        begin = int(match.group('begin'))
        end = int(match.group('end'))
        # begin と end が整数に変換できた場合
        lines = code.split('\n')[begin - 1:end]
    except (ValueError, TypeError):
        # 変換できなかった場合は全ての行を取得
        lines = code.split('\n')
    
    return '\n'.join(
      list(map(
        lambda x: spacing + x,
        lines,
      ))
    )

def on_page_markdown(markdown, **kwargs):
  return re.sub(
    re.compile(
      r'^(?P<spacing>[ ]*)https://github.com/(?P<user>[\w/\-]+)/blob/(?P<filename>[\w\d\-/\.]+)\.(?P<extension>[\w-]+)(#L(?P<begin>\d+)(-L(?P<end>\d+))?)?$',
      re.MULTILINE,
    ),
    replacer,
    markdown,
  )
