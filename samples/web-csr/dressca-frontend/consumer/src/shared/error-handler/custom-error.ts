/* eslint max-classes-per-file: 0 */
export abstract class CustomErrorBase extends Error {
  cause?: Error | null

  constructor(message: string, cause?: Error) {
    super(message)
    // ラップ前のエラーを cause として保持
    this.cause = cause
  }
}

/**
 * 原因不明のエラーを表すカスタムエラーです。
 */
export class UnknownError extends CustomErrorBase {
  constructor(message: string, cause?: Error) {
    super(message, cause)
    this.name = 'UnknownError'
  }
}

export class HttpError extends CustomErrorBase {
  response?: ProblemDetails | null

  constructor(message: string, cause?: Error & { response?: { data?: ProblemDetails } }) {
    super(message, cause)
    this.response = cause?.response?.data ?? null
    this.name = 'HttpError'
  }
}

export class NetworkError extends HttpError {
  constructor(message: string, cause?: Error) {
    super(message, cause)
    this.name = 'NetworkError'
  }
}

export class UnauthorizedError extends HttpError {
  constructor(message: string, cause?: Error) {
    super(message, cause)
    this.name = 'UnauthorizedError'
  }
}

export class ServerError extends HttpError {
  constructor(message: string, cause?: Error) {
    super(message, cause)
    this.name = 'ServerError'
  }
}

export interface ProblemDetails {
  detail: string
  exceptionId: string
  exceptionValues: string[]
  instance: string
  status: number
  title: string
  type: string
}
