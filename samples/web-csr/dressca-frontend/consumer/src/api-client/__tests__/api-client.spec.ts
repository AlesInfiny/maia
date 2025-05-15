import { describe, it, expect, vi } from 'vitest';
import { UnknownError } from '@/shared/error-handler/custom-error';
import { axiosInstance } from '@/api-client';
import axios from 'axios';

describe('axiosInstance_レスポンスインターセプター_HTTPステータスに応じた例外をスロー', () => {
  it('AxiosError以外_UnknownErrorをthrow', async () => {
    // Arrange
    axiosInstance.defaults.adapter = vi
      .fn()
      .mockRejectedValue(new Error('想定外のエラー'));
    // 多くの場合で Axios がうまく AxiosError に包み直してしまうので、検証のため強制的に挙動を差し替えます。
    vi.spyOn(axios, 'isAxiosError').mockReturnValue(false);

    // Act
    const responsePromise = axiosInstance.get('/test');

    // Assert
    await expect(responsePromise).rejects.toThrow(UnknownError);
  });
});
