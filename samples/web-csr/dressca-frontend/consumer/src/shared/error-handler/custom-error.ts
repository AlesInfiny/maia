/* eslint max-classes-per-file: 0 */
export abstract class CustomErrorBase extends Error {
  cause?: Error | null;

  constructor(message: string, cause?: Error) {
    super(message);
    // ラップ前のエラーを cause として保持
    this.cause = cause;
  }
}

export class HttpError extends CustomErrorBase {
  constructor(message: string, cause?: Error) {
    super(message, cause);
    this.name = 'HttpError';
  }
}

export class NetworkError extends HttpError {
  constructor(message: string, cause?: Error) {
    super(message, cause);
    this.name = 'NetworkError';
  }
}

export class UnauthorizedError extends HttpError {
  constructor(message: string, cause?: Error) {
    super(message, cause);
    this.name = 'UnauthorizedError';
  }
}

export class ServerError extends HttpError {
  constructor(message: string, cause?: Error) {
    super(message, cause);
    this.name = 'ServerError';
  }
}
