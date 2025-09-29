/* eslint-disable no-console */
export type LogLevel = 'debug' | 'info' | 'warn' | 'error'
// Error は unknown 型として catch されるので、 error インスタンスを受け入れるために unknown 型にしています。
export type LogArgs = unknown

const emptyFunction: (..._args: unknown[]) => void = () => {}

type LogHandler = (...message: LogArgs[]) => void
export interface Logger {
  debug: LogHandler
  info: LogHandler
  warn: LogHandler
  error: LogHandler
}

let _logger: Logger

export function initializeLogger() {
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
      debug: emptyFunction,
      info: emptyFunction,
      warn: emptyFunction,
      error: (...args) => console.error(...args),
    }
    _logger = logger
  }
}

export function useLogger() {
  if (!_logger) {
    initializeLogger()
  }
  return _logger
}
