import { describe, it, expect, vi, beforeEach, afterEach } from 'vitest'
import { axiosInstance } from '@/api-client'
import { abortAllRequests } from '@/api-client/request-abort-manager'
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

describe('axiosInstance_リクエスト中断制御', () => {
  it('abortAllRequests 実行後は新しい AbortSignal が使われる', async () => {
    // Arrange
    const capturedSignals: Array<AbortSignal | undefined> = []
    adapterMock.mockImplementation((config) => {
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

  it('リクエストがキャンセルされた場合は CanceledError で reject される', async () => {
    // Arrange
    const canceledError = new axios.CanceledError('canceled')
    adapterMock.mockRejectedValue(canceledError)

    // Act
    const responsePromise = axiosInstance.get('/test')

    // Assert: キャンセル時はカスタムエラーに変換されず CanceledError のまま reject される
    await expect(responsePromise).rejects.toBe(canceledError)
  })
})
