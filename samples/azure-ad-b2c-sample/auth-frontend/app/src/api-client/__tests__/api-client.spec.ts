// axios-instance.spec.ts
import { describe, it, expect, vi, beforeEach } from 'vitest';
import { axiosInstance } from '@/api-client';
import {
  NetworkError,
  ServerError,
  UnauthorizedError,
  HttpError,
  UnknownError,
} from '@/shared/custom-errors';
import axios, { HttpStatusCode } from 'axios';

describe('axiosInstance_レスポンスインターセプター_HTTPステータスに応じた例外をスロー', () => {
  beforeEach(() => {
    vi.restoreAllMocks();
  });

  it('HTTP500レスポンス_ServerErrorをスロー', async () => {
    axiosInstance.defaults.adapter = vi.fn().mockRejectedValue({
      isAxiosError: true,
      response: { status: HttpStatusCode.InternalServerError },
    });

    const promise = axiosInstance.get('/test');
    await expect(promise).rejects.toThrow(ServerError);
  });

  it('HTTP401レスポンス_UnauthorizedErrorをスロー', async () => {
    axiosInstance.defaults.adapter = vi.fn().mockRejectedValue({
      isAxiosError: true,
      response: { status: HttpStatusCode.Unauthorized },
    });

    const promise = axiosInstance.get('/test');
    await expect(promise).rejects.toThrow(UnauthorizedError);
  });

  it('HTTPステータスコード未登録 _HttpErrorをスロー', async () => {
    axiosInstance.defaults.adapter = vi.fn().mockRejectedValue({
      isAxiosError: true,
      response: { status: 123 },
    });

    const promise = axiosInstance.get('/test');
    await expect(promise).rejects.toThrow(HttpError);
  });

  it('responseが存在しない_NetworkError をスロー', async () => {
    axiosInstance.defaults.adapter = vi.fn().mockRejectedValue({
      isAxiosError: true,
      response: undefined,
    });

    const promise = axiosInstance.get('/test');
    await expect(promise).rejects.toThrow(NetworkError);
  });

  it('AxiosError以外_UnknownErrorをthrow', async () => {
    // Arrange
    axiosInstance.defaults.adapter = vi
      .fn()
      .mockRejectedValue(new Error('想定外のエラー'));
    // 多くの場合で Axios がうまく AxiosError に包み直してしまうので、検証のため強制的に挙動を差し替えます。
    vi.spyOn(axios, 'isAxiosError').mockReturnValue(false);

    const promise = axiosInstance.get('/test');
    await expect(promise).rejects.toThrow(UnknownError);
  });
});
