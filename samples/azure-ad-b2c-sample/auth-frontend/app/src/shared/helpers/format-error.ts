/**
 * Error オブジェクトを整形済みの文字列として取得します。
 * @param error 整形対象の Error インスタンス
 * @returns フォーマット済み文字列
 */
export function formatError(error: Error): string {
  const { name } = error
  const { message } = error
  const stack = error.stack ?? 'error.stackが未定義です。'

  return [
    `===== ERROR =====`,
    `name   : ${name}`,
    `message: ${message}`,
    `stack  :`,
    `${stack}`,
  ].join('\n')
}
