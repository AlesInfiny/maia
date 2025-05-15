import { describe, it, expect, vi } from 'vitest';
import {
  HttpError,
  NetworkError,
  UnauthorizedError,
  ConflictError,
  NotFoundError,
  ServerError,
  UnknownError,
} from '@/shared/error-handler/custom-error';
import { axiosInstance } from '@/api-client';
import { http, HttpResponse } from 'msw';
import axios, { HttpStatusCode } from 'axios';
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
    server.use(http.get('/test', () => HttpResponse.error())); // msw の API を使用してネットワークエラーを再現します。

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
  it('AxiosError以外_UnknownErrorをthrow', async () => {
    // Arrange
    server.use(
      http.get('/test', () => {
        throw new Error();
      }),
    );
    // 多くの場合で Axios がうまく AxiosError に包み直してしまうので、検証のために強制的に挙動を差し替えます。
    vi.spyOn(axios, 'isAxiosError').mockReturnValue(false);

    // Act
    const responsePromise = axiosInstance.get('/test');

    // Assert
    await expect(responsePromise).rejects.toThrow(UnknownError);
  });
});
