import { describe, it, expect, vi, beforeEach } from 'vitest'
import { createTestingPinia } from '@pinia/testing'
import { setActivePinia } from 'pinia'
import { useAuthentication } from '../use-authentication'
import { useAuthenticationStore } from '../../stores/authentication'
import { useNotificationStore } from '@/business-common/stores/notification'

/**
 * vi.mock はファイルの先頭に巻き上げられるので、
 * vi.hoisted を使用してモックの参照を先に確保します。
 */
const { abortAllRequestsMock } = vi.hoisted(() => {
  return {
    abortAllRequestsMock: vi.fn(),
  }
})

vi.mock('@/system-common/api-client/request-abort-manager', () => ({
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

describe('useAuthentication_signOut', () => {
  beforeEach(() => {
    vi.clearAllMocks()
    sessionStorage.clear()
    setupPinia()
  })

  it('処理中の API リクエストを中断する', () => {
    // Act
    const { signOut } = useAuthentication()
    signOut()

    // Assert
    expect(abortAllRequestsMock).toHaveBeenCalled()
  })

  it('認証状態と通知の state が初期値にリセットされる', () => {
    // Arrange
    const authenticationStore = useAuthenticationStore()
    const notificationStore = useNotificationStore()
    authenticationStore.signIn()
    notificationStore.setMessage('エラー', 'id-1', 'タイトル', '詳細', 500, 10000)

    // Act
    const { signOut } = useAuthentication()
    signOut()

    // Assert
    expect(authenticationStore.authenticationState).toBe(false)
    expect(authenticationStore.isAuthenticated).toBe(false)

    expect(notificationStore.message).toBe('')
    expect(notificationStore.id).toBe('')
    expect(notificationStore.title).toBe('')
    expect(notificationStore.detail).toBe('')
    expect(notificationStore.status).toBe(0)
    expect(notificationStore.timeout).toBe(5000)
  })

  it('sessionStorage から isAuthenticated キーが削除される', () => {
    // Arrange
    const authenticationStore = useAuthenticationStore()
    authenticationStore.signIn()
    expect(sessionStorage.getItem('isAuthenticated')).toBe('true')

    // Act
    const { signOut } = useAuthentication()
    signOut()

    // Assert
    expect(sessionStorage.getItem('isAuthenticated')).toBeNull()
  })
})
