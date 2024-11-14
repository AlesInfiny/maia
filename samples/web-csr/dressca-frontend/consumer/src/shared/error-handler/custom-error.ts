/* eslint max-classes-per-file: 0 */
export abstract class CustomErrorBase extends Error {
  cause?: Error | null;

  response?: ProblemDetails | null;

  constructor(message: string, cause?: Error) {
    super(message);
    // ラップ前のエラーを cause として保持
    this.cause = cause;
    this.response = cause.response.data;
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

interface ProblemDetails {
  detail: string;
  exceptionId: string;
  exceptionValue: string[];
  instance: string;
  status: number;
  title: string;
  type: string;
}
