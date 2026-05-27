import { describe, it, expect, vi, beforeEach } from 'vitest'
import axios from 'axios'
import { useCustomErrorHandler } from '@/shared/error-handler/custom-error-handler'
import { ServerError, NetworkError, UnauthorizedError } from '@/shared/error-handler/custom-error'

// useEventBus のモック
const emitMock = vi.fn()
vi.mock('@vueuse/core', () => ({
  useEventBus: () => ({ emit: emitMock }),
}))

// useLogger のモック
vi.mock('@/composables/use-logger', () => ({
  useLogger: () => ({
    info: vi.fn(),
    error: vi.fn(),
    warn: vi.fn(),
  }),
}))

describe('useCustomErrorHandler', () => {
  let handleErrorAsync: ReturnType<typeof useCustomErrorHandler>

  beforeEach(() => {
    handleErrorAsync = useCustomErrorHandler()
    emitMock.mockClear()
  })

  describe('CanceledError の処理', () => {
    it('axios.CanceledError の場合はコールバックを実行せず正常終了する', async () => {
      // Arrange
      const canceledError = new axios.CanceledError('canceled')
      const callback = vi.fn()

      // Act
      await handleErrorAsync(canceledError, callback)

      // Assert: コールバックが呼ばれず、例外もスローされない
      expect(callback).not.toHaveBeenCalled()
      expect(emitMock).not.toHaveBeenCalled()
    })

    it('axios.CanceledError の場合は再スローしない', async () => {
      // Arrange
      const canceledError = new axios.CanceledError('canceled')

      // Act & Assert: 例外がスローされないことを確認
      await expect(handleErrorAsync(canceledError, vi.fn())).resolves.toBeUndefined()
    })
  })

  describe('CustomErrorBase の処理', () => {
    it('ServerError の場合はコールバックが実行される', async () => {
      // Arrange
      const serverError = new ServerError('Internal Server Error')
      const callback = vi.fn()

      // Act
      await handleErrorAsync(serverError, callback)

      // Assert
      expect(callback).toHaveBeenCalledOnce()
      expect(emitMock).toHaveBeenCalledWith(
        expect.objectContaining({ message: 'サーバーエラーが発生しました。' }),
      )
    })

    it('NetworkError の場合はコールバックが実行される', async () => {
      // Arrange
      const networkError = new NetworkError('Network Error')
      const callback = vi.fn()

      // Act
      await handleErrorAsync(networkError, callback)

      // Assert
      expect(callback).toHaveBeenCalledOnce()
      expect(emitMock).toHaveBeenCalledWith(
        expect.objectContaining({ message: 'ネットワークエラーが発生しました。' }),
      )
    })

    it('UnauthorizedError の場合はコールバックが実行される', async () => {
      // Arrange
      const unauthorizedError = new UnauthorizedError('Unauthorized')
      const callback = vi.fn()

      // Act
      await handleErrorAsync(unauthorizedError, callback)

      // Assert
      expect(callback).toHaveBeenCalledOnce()
      expect(emitMock).toHaveBeenCalledWith(
        expect.objectContaining({ details: 'ログインしてください。' }),
      )
    })
  })

  describe('ハンドリング不能エラーの処理', () => {
    it('CustomErrorBase でも CanceledError でもないエラーは再スローされる', async () => {
      // Arrange
      const unknownError = new Error('unknown')
      const callback = vi.fn()

      // Act & Assert
      await expect(handleErrorAsync(unknownError, callback)).rejects.toThrow('unknown')
      expect(callback).not.toHaveBeenCalled()
    })
  })
})
