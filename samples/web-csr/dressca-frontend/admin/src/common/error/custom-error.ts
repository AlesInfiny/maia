/* eslint max-classes-per-file: 0 */

/**
 * カスタムエラーの基底クラスです。
 */
export abstract class CustomErrorBase extends Error {
  cause?: Error | null

  constructor(message: string, cause?: Error) {
    super(message)
    // ラップ前のエラーを cause として保持
    this.cause = cause
  }

  abstract toJSON(): Record<string, unknown>
}

/**
 * 原因不明のエラーを表すカスタムエラーです。
 */
export class UnknownError extends CustomErrorBase {
  constructor(message: string, cause?: Error) {
    super(message, cause)
    this.name = 'UnknownError'
  }

  toJSON() {
    return {
      timestamp: new Date().toISOString(),
      name: this.name,
      message: this.message,
      stack: this.stack,
      response: null,
      cause: this.cause ?? null,
    }
  }
}

/**
 * HTTP 通信でのエラーを表すカスタムエラーです。
 */
export class HttpError extends CustomErrorBase {
  response?: ProblemDetails | null

  constructor(message: string, cause?: Error & { response?: { data?: ProblemDetails } }) {
    super(message, cause)
    this.response = cause?.response?.data ?? null
    this.name = 'HttpError'
  }

  toJSON() {
    return {
      timestamp: new Date().toISOString(),
      name: this.name,
      message: this.message,
      stack: this.stack,
      response: this.response ?? null,
      cause: this.cause ?? null,
    }
  }
}

/**
 * ネットワークエラーを表すカスタムエラーです。
 */
export class NetworkError extends HttpError {
  constructor(message: string, cause?: Error) {
    super(message, cause)
    this.name = 'NetworkError'
  }
}

/**
 * 401 Unauthorized を表すカスタムエラーです。
 */
export class UnauthorizedError extends HttpError {
  constructor(message: string, cause?: Error) {
    super(message, cause)
    this.name = 'UnauthorizedError'
  }
}

/**
 * 409 Conflict を表すカスタムエラーです。
 */
export class ConflictError extends HttpError {
  constructor(message: string, cause?: Error) {
    super(message, cause)
    this.name = 'ConflictError'
  }
}

/**
 * 404 Not Found を表すカスタムエラーです。
 */
export class NotFoundError extends HttpError {
  constructor(message: string, cause?: Error) {
    super(message, cause)
    this.name = 'NotFoundError'
  }
}

/**
 * 500 Internal Server Error を表すカスタムエラーです。
 */
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
