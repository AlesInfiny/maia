import { describe, it, expect } from 'vitest';
import {
  HttpError,
  NetworkError,
  UnauthorizedError,
  ConflictError,
  NotFoundError,
  ServerError,
} from '@/shared/error-handler/custom-error';
import { axiosInstance } from '@/api-client';
import { http, HttpResponse } from 'msw';
import { HttpStatusCode } from 'axios';
import { server } from '../../../mock/node';

describe('axiosInstance_レスポンスインターセプター_HTTPステータスに応じた例外をスロー', () => {
  it('HTTP500レスポンス_ServerErrorをスロー', async () => {
    // Arrange
    server.use(
      http.get(
        '/test',
        () =>
          new HttpResponse(null, {
            status: HttpStatusCode.InternalServerError,
          }),
      ),
    );

    // Act
    const responsePromise = axiosInstance.get('/test');

    // Assert
    await expect(responsePromise).rejects.toThrow(ServerError);
  });

  it('HTTP401レスポンス_UnauthorizedErrorをスロー', async () => {
    // Arrange
    server.use(
      http.get(
        '/test',
        () => new HttpResponse(null, { status: HttpStatusCode.Unauthorized }),
      ),
    );

    // Act
    const responsePromise = axiosInstance.get('/test');

    // Assert
    await expect(responsePromise).rejects.toThrow(UnauthorizedError);
  });

  it('HTTP404レスポンス_NotFoundErrorをスロー', async () => {
    // Arrange
    server.use(
      http.get(
        '/test',
        () => new HttpResponse(null, { status: HttpStatusCode.NotFound }),
      ),
    );

    // Act
    const responsePromise = axiosInstance.get('/test');

    // Assert
    await expect(responsePromise).rejects.toThrow(NotFoundError);
  });

  it('HTTP409レスポンス_ConflictErrorをスロー', async () => {
    // Arrange
    server.use(
      http.get(
        '/test',
        () => new HttpResponse(null, { status: HttpStatusCode.Conflict }),
      ),
    );

    // Act
    const responsePromise = axiosInstance.get('/test');

    // Assert
    await expect(responsePromise).rejects.toThrow(ConflictError);
  });

  it('HTTPレスポンスなし_NetworkErrorをスロー', async () => {
    // Arrange
    server.use(http.get('/test', () => undefined));
    // 何も返さないと msw と jsdom のほうでエラーが出てしてしまうので、 代わりに undefined を返却します。

    // Act
    const responsePromise = axiosInstance.get('/test');

    // Assert
    await expect(responsePromise).rejects.toThrow(NetworkError);
  });

  it('HTTPステータスコード未登録 _HttpErrorをスロー', async () => {
    // Arrange
    server.use(
      http.get('/test', () => new HttpResponse(null, { status: 123 })),
    );

    // Act
    const responsePromise = axiosInstance.get('/test');

    // Assert
    await expect(responsePromise).rejects.toThrow(HttpError);
  });
});
