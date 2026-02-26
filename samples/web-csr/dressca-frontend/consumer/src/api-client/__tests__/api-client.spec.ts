import { describe, it, expect, vi, beforeEach, afterEach } from 'vitest'
import {
  HttpError,
  NetworkError,
  ServerError,
  UnauthorizedError,
  UnknownError,
} from '@/shared/error-handler/custom-error'
import { axiosInstance } from '@/api-client'
import axios, { HttpStatusCode } from 'axios'

/**
 * vi.mock はファイルの先頭に巻き上げられるので、
 * vi.hoisted を使用してモックの参照を先に確保します。
 */
const { adapterMock } = vi.hoisted(() => ({
  adapterMock: vi.fn(),
}))

const defaultAdapter = axiosInstance.defaults.adapter

beforeEach(() => {
  adapterMock.mockReset()
  axiosInstance.defaults.adapter = adapterMock
})

afterEach(() => {
  vi.restoreAllMocks()
  adapterMock.mockReset()
  axiosInstance.defaults.adapter = defaultAdapter
})

describe('axiosInstance_レスポンスインターセプター_HTTPステータスに応じた例外をスロー', () => {
  it('リクエストキャンセル時_CanceledErrorをスロー', async () => {
    // Arrange
    const canceledError = new axios.CanceledError('canceled')
    adapterMock.mockRejectedValue(canceledError)

    // Act
    const responsePromise = axiosInstance.get('/test')

    // Assert
    await expect(responsePromise).rejects.toBe(canceledError)
  })

  it('responseが存在しない_NetworkError をスロー', async () => {
    // Arrange
    adapterMock.mockRejectedValue({
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
    adapterMock.mockRejectedValue({
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
    adapterMock.mockRejectedValue({
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
    adapterMock.mockRejectedValue({
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
    adapterMock.mockRejectedValue(new Error('想定外のエラー'))
    // 多くの場合で Axios がうまく AxiosError に包み直してしまうので、検証のため強制的に挙動を差し替えます。
    vi.spyOn(axios, 'isAxiosError').mockReturnValue(false)

    // Act
    const responsePromise = axiosInstance.get('/test')

    // Assert
    await expect(responsePromise).rejects.toThrow(UnknownError)
  })
})
