/* eslint-disable no-console */

/**
 * ログレベルを表す文字列リテラル型です。
 * - 'debug': デバッグ情報
 * - 'info' : 通常の情報
 * - 'warn' : 警告
 * - 'error': エラー
 */
export type LogLevel = 'debug' | 'info' | 'warn' | 'error'

/**
 * ログの引数として受け入れる型です。
 * Error は catch 時に unknown 型で扱われるため、
 * エラーインスタンスを直接受け入れられるように unknown 型としています。
 */
export type LogArgs = unknown

/**
 * 何も処理を行わないダミー関数です。
 * 本番環境などで不要なログ出力を抑止するために利用します。
 */
const doNothing: (..._args: unknown[]) => void = () => {}

/**
 * ログ出力関数の型定義です。
 * 複数の任意の値を受け取り、出力を行います。
 */
type LogHandler = (...message: LogArgs[]) => void

/**
 * アプリケーションで利用するロガーのインターフェースです。
 * 各ログレベルに対応するメソッドを提供します。
 */
export interface Logger {
  debug: LogHandler
  info: LogHandler
  warn: LogHandler
  error: LogHandler
}

/**
 * 内部で保持するロガーのインスタンスです。
 * `initializeLogger` によって設定し、
 * `useLogger` によって取得します。
 */
let _logger: Logger

/**
 * ロガーを初期化します。
 *
 * - 開発環境 (`import.meta.env.DEV === true`) では console API を利用したロガーを設定します。
 * - 本番環境ではデフォルトでログ出力を抑制し、error のみ console に出力するロガーを設定します。
 *
 * 必要に応じて本番環境用のロガーを差し替えてください。
 */
function initializeLogger() {
  if (import.meta.env.DEV) {
    const logger: Logger = {
      debug: (...args) => console.debug(...args),
      info: (...args) => console.info(...args),
      warn: (...args) => console.warn(...args),
      error: (...args) => console.error(...args),
    }
    _logger = logger
  } else {
    // 本番環境用のロガーを注入します。
    // 適切な出力先を設定してください。
    const logger: Logger = {
      debug: doNothing,
      info: doNothing,
      warn: doNothing,
      error: (...args) => console.error(...args),
    }
    _logger = logger
  }
}

/**
 * ロガーを取得します。
 * 初回呼び出し時にロガーが未初期化であれば `initializeLogger` により初期化します。
 * @returns アプリケーション共通で利用するロガーインスタンス
 */
export function useLogger() {
  if (!_logger) {
    initializeLogger()
  }
  return _logger
}
