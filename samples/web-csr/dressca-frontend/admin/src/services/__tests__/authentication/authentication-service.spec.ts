import { describe, it, expect, vi, beforeEach } from 'vitest'
import { createTestingPinia } from '@pinia/testing'
import { setActivePinia } from 'pinia'
import { logout } from '@/services/authentication/authentication-service'
import { useAuthenticationStore } from '@/stores/authentication/authentication'
import { useNotificationStore } from '@/stores/notification/notification'
import axios from 'axios'

/**
 * vi.mock はファイルの先頭に巻き上げられるので、
 * vi.hoisted を使用してモックの参照を先に確保します。
 */
const { abortAllRequestsMock, getLoginUserMock } = vi.hoisted(() => {
  return {
    abortAllRequestsMock: vi.fn(),
    getLoginUserMock: vi.fn(),
  }
})

vi.mock('@/api-client', () => ({
  usersApi: () => ({
    getLoginUser: getLoginUserMock,
  }),
}))

vi.mock('@/api-client/request-abort-manager', () => ({
  abortAllRequests: abortAllRequestsMock,
}))

/**
 * テスト用の Pinia を作成し、アクティブに設定するヘルパー関数です。
 * @returns アクティブ化済みの Pinia インスタンス。
 */
function setupPinia() {
  const pinia = createTestingPinia({
    createSpy: vi.fn,
    stubActions: false,
  })
  setActivePinia(pinia)
  return pinia
}

describe('logout', () => {
  beforeEach(() => {
    sessionStorage.clear()
  })

  describe('ログアウト中に実行中だった API がログアウト後に完了して store の state を書き戻さない', () => {
    it('logout 実行後に API がキャンセルされてもストアの state は初期値のまま', async () => {
      // Arrange
      setupPinia()
      const authenticationStore = useAuthenticationStore()

      // API が遅延して解決される Promise を用意
      let rejectApi!: (reason: unknown) => void
      const delayedApiPromise = new Promise<{
        data: { userName: string; roles: string }
      }>((_resolve, reject) => {
        rejectApi = reject
      })
      getLoginUserMock.mockReturnValue(delayedApiPromise)

      // abortAllRequests が呼ばれたら、ペンディング中の API を CanceledError で reject する
      // 実際の振る舞いを再現: AbortController.abort() により axios がリクエストをキャンセルする
      abortAllRequestsMock.mockImplementation(() => {
        rejectApi(new axios.CanceledError('canceled'))
      })

      // API 呼び出しを開始（完了を待たない）
      const signInPromise = authenticationStore.signInAsync()

      // Act: API が完了する前にログアウトを実行
      logout()

      await expect(signInPromise).rejects.toBeInstanceOf(axios.CanceledError)

      // Assert: ストアの state が初期値のまま（API レスポンスで書き戻されていない）
      expect(authenticationStore.authenticationState).toBe(false)
      expect(authenticationStore.userName).toBe('')
      expect(authenticationStore.userRoles).toBe('')
      expect(abortAllRequestsMock).toHaveBeenCalled()
    })
  })

  describe('ログアウト後に store が初期化される', () => {
    it('全ストアの state が初期値にリセットされる', () => {
      // Arrange
      setupPinia()
      const authenticationStore = useAuthenticationStore()
      const notificationStore = useNotificationStore()

      // 各ストアにデータを設定
      authenticationStore.authenticationState = true
      authenticationStore.userName = 'テストユーザー'
      authenticationStore.userRoles = 'ROLE_ADMIN'
      sessionStorage.setItem('isAuthenticated', JSON.stringify(true))
      sessionStorage.setItem('userName', JSON.stringify('テストユーザー'))
      sessionStorage.setItem('userRoles', JSON.stringify('ROLE_ADMIN'))
      notificationStore.setMessage('エラーが発生しました', 10000)

      // Act
      logout()

      // Assert
      expect(authenticationStore.authenticationState).toBe(false)
      expect(authenticationStore.isAuthenticated).toBe(false)
      expect(authenticationStore.userName).toBe('')
      expect(authenticationStore.userRoles).toBe('')

      expect(notificationStore.message).toBe('')
      expect(notificationStore.timeout).toBe(5000)
    })
  })

  describe('ログアウト後にストレージが初期化される', () => {
    it('sessionStorage から認証関連キーがすべて削除される', () => {
      // Arrange
      setupPinia()
      const authenticationStore = useAuthenticationStore()
      authenticationStore.authenticationState = true
      authenticationStore.userName = 'テストユーザー'
      authenticationStore.userRoles = 'ROLE_ADMIN'
      sessionStorage.setItem('isAuthenticated', JSON.stringify(true))
      sessionStorage.setItem('userName', JSON.stringify('テストユーザー'))
      sessionStorage.setItem('userRoles', JSON.stringify('ROLE_ADMIN'))

      // Act
      logout()

      // Assert
      expect(sessionStorage.getItem('isAuthenticated')).toBeNull()
      expect(sessionStorage.getItem('userName')).toBeNull()
      expect(sessionStorage.getItem('userRoles')).toBeNull()
    })
  })
})
