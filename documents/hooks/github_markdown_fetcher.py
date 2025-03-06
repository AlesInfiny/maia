import re
import urllib.request

def replacer(match):
    filename = f"{match.group('filename')}.{match.group('extension')}"
    url = f"https://raw.githubusercontent.com/{match.group('user')}/{filename}"
    code = urllib.request.urlopen(url).read().decode('utf-8')
    extension = 'js' if match.group('extension') == 'vue' else match.group('extension')
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
      r'^(?P<spacing>[ ]*)https://github.com/(?P<user>[\w/\-]+)/blob/(?P<filename>[\w\d\-/\.]+)\.(?P<extension>\w+)(#L(?P<begin>\d+)(-L(?P<end>\d+))?)?$',
      re.MULTILINE,
    ),
    replacer,
    markdown,
  )