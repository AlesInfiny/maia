import { describe, it, expect, vi } from 'vitest'
import {
  HttpError,
  NetworkError,
  ServerError,
  UnauthorizedError,
  UnknownError,
} from '@/shared/error-handler/custom-error'
import { abortAllRequests, axiosInstance } from '@/api-client'
import axios, { HttpStatusCode } from 'axios'

describe('axiosInstance_レスポンスインターセプター_HTTPステータスに応じた例外をスロー', () => {
  it('responseが存在しない_NetworkError をスロー', async () => {
    // Arrange
    axiosInstance.defaults.adapter = vi.fn().mockRejectedValue({
      isAxiosError: true,
      response: undefined,
    })

    // Act
    const responsePromise = axiosInstance.get('/test')

    // Assert
    await expect(responsePromise).rejects.toThrow(NetworkError)
  })

  it('HTTP500レスポンス_ServerErrorをスロー', async () => {
    // Arrange
    axiosInstance.defaults.adapter = vi.fn().mockRejectedValue({
      isAxiosError: true,
      response: { status: HttpStatusCode.InternalServerError },
    })

    // Act
    const responsePromise = axiosInstance.get('/test')

    // Assert
    await expect(responsePromise).rejects.toThrow(ServerError)
  })

  it('HTTP401レスポンス_UnauthorizedErrorをスロー', async () => {
    // Arrange
    axiosInstance.defaults.adapter = vi.fn().mockRejectedValue({
      isAxiosError: true,
      response: { status: HttpStatusCode.Unauthorized },
    })

    // Act
    const responsePromise = axiosInstance.get('/test')

    // Assert
    await expect(responsePromise).rejects.toThrow(UnauthorizedError)
  })

  it('HTTPステータスコード未登録 _HttpErrorをスロー', async () => {
    // Arrange
    axiosInstance.defaults.adapter = vi.fn().mockRejectedValue({
      isAxiosError: true,
      response: { status: 123 },
    })

    // Act
    const responsePromise = axiosInstance.get('/test')

    // Assert
    await expect(responsePromise).rejects.toThrow(HttpError)
  })

  it('AxiosError以外_UnknownErrorをthrow', async () => {
    // Arrange
    axiosInstance.defaults.adapter = vi.fn().mockRejectedValue(new Error('想定外のエラー'))
    // 多くの場合で Axios がうまく AxiosError に包み直してしまうので、検証のため強制的に挙動を差し替えます。
    vi.spyOn(axios, 'isAxiosError').mockReturnValue(false)

    // Act
    const responsePromise = axiosInstance.get('/test')

    // Assert
    await expect(responsePromise).rejects.toThrow(UnknownError)
  })
})

describe('axiosInstance_リクエスト中断制御', () => {
  it('abortAllRequests 実行後は新しい AbortSignal が使われる', async () => {
    // Arrange
    const capturedSignals: Array<AbortSignal | undefined> = []
    axiosInstance.defaults.adapter = vi.fn().mockImplementation((config) => {
      capturedSignals.push(config.signal)
      return Promise.resolve({
        data: {},
        status: HttpStatusCode.Ok,
        statusText: 'OK',
        headers: {},
        config,
      })
    })

    // Act
    await axiosInstance.get('/test-1')
    await axiosInstance.get('/test-2')
    const signalBeforeAbort = capturedSignals[0]

    abortAllRequests()

    await axiosInstance.get('/test-3')
    const signalAfterAbort = capturedSignals[2]

    // Assert
    expect(signalBeforeAbort).toBeDefined()
    expect(capturedSignals[1]).toBe(signalBeforeAbort)
    expect(signalBeforeAbort?.aborted).toBe(true)
    expect(signalAfterAbort).toBeDefined()
    expect(signalAfterAbort).not.toBe(signalBeforeAbort)
    expect(signalAfterAbort?.aborted).toBe(false)
  })

  it('キャンセルエラーは独自例外へラップせずそのまま伝播する', async () => {
    // Arrange
    axiosInstance.defaults.adapter = vi.fn().mockRejectedValue(new axios.CanceledError('canceled'))

    // Act
    const responsePromise = axiosInstance.get('/test')

    // Assert
    await expect(responsePromise).rejects.toBeInstanceOf(axios.CanceledError)
  })
})
