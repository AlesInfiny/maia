import { describe, it, expect, vi, beforeEach, afterEach } from 'vitest'
import {
  HttpError,
  NetworkError,
  UnauthorizedError,
  ConflictError,
  NotFoundError,
  ServerError,
  UnknownError,
} from '@/shared/error-handler/custom-error'
import { axiosInstance } from '@/api-client'
import { http, HttpResponse } from 'msw'
import axios, { HttpStatusCode } from 'axios'
import { server } from '../../../mock/node'

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
  axiosInstance.defaults.adapter = defaultAdapter
})

afterEach(() => {
  vi.restoreAllMocks()
  adapterMock.mockReset()
  axiosInstance.defaults.adapter = defaultAdapter
})

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
    )

    // Act
    const responsePromise = axiosInstance.get('/test')

    // Assert
    await expect(responsePromise).rejects.toThrow(ServerError)
  })

  it('HTTP401レスポンス_UnauthorizedErrorをスロー', async () => {
    // Arrange
    server.use(
      http.get('/test', () => new HttpResponse(null, { status: HttpStatusCode.Unauthorized })),
    )

    // Act
    const responsePromise = axiosInstance.get('/test')

    // Assert
    await expect(responsePromise).rejects.toThrow(UnauthorizedError)
  })

  it('HTTP404レスポンス_NotFoundErrorをスロー', async () => {
    // Arrange
    server.use(http.get('/test', () => new HttpResponse(null, { status: HttpStatusCode.NotFound })))

    // Act
    const responsePromise = axiosInstance.get('/test')

    // Assert
    await expect(responsePromise).rejects.toThrow(NotFoundError)
  })

  it('HTTP409レスポンス_ConflictErrorをスロー', async () => {
    // Arrange
    server.use(http.get('/test', () => new HttpResponse(null, { status: HttpStatusCode.Conflict })))

    // Act
    const responsePromise = axiosInstance.get('/test')

    // Assert
    await expect(responsePromise).rejects.toThrow(ConflictError)
  })

  it('HTTPレスポンスなし_NetworkErrorをスロー', async () => {
    // Arrange
    server.use(http.get('/test', () => HttpResponse.error())) // msw の API を使用してネットワークエラーを再現します。

    // Act
    const responsePromise = axiosInstance.get('/test')

    // Assert
    await expect(responsePromise).rejects.toThrow(NetworkError)
  })

  it('HTTPステータスコード未登録 _HttpErrorをスロー', async () => {
    // Arrange
    server.use(http.get('/test', () => new HttpResponse(null, { status: 123 })))

    // Act
    const responsePromise = axiosInstance.get('/test')

    // Assert
    await expect(responsePromise).rejects.toThrow(HttpError)
  })
  it('AxiosError以外_UnknownErrorをthrow', async () => {
    // Arrange
    server.use(
      http.get('/test', () => {
        throw new Error()
      }),
    )
    // 多くの場合で Axios がうまく AxiosError に包み直してしまうので、検証のために強制的に挙動を差し替えます。
    vi.spyOn(axios, 'isAxiosError').mockReturnValue(false)

    // Act
    const responsePromise = axiosInstance.get('/test')

    // Assert
    await expect(responsePromise).rejects.toThrow(UnknownError)
  })

  it('リクエストキャンセル時_CanceledErrorをスロー', async () => {
    // Arrange: キャンセルエラーを再現するアダプターを差し替えます。
    const canceledError = new axios.CanceledError('canceled')
    adapterMock.mockImplementation(() => {
      return new Promise((_resolve, reject) => {
        setTimeout(() => reject(canceledError), 0)
      })
    })
    axiosInstance.defaults.adapter = adapterMock

    const responsePromise = axiosInstance.get('/test')

    // Assert: axios の CanceledError で reject される
    await expect(responsePromise).rejects.toBe(canceledError)
  })
})
