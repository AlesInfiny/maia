import { describe, it, expect, vi, beforeEach } from 'vitest'
import { createTestingPinia } from '@pinia/testing'
import { setActivePinia } from 'pinia'
import { authenticationService } from '@/services/authentication/authentication-service'
import { useAuthenticationStore } from '@/stores/authentication/authentication'
import { useBasketStore } from '@/stores/basket/basket'
import { useCatalogStore } from '@/stores/catalog/catalog'
import { useNotificationStore } from '@/stores/notification/notification'
import type { BasketResponse } from '@/generated/api-client'
import axios from 'axios'

/**
 * vi.mock はファイルの先頭に巻き上げられるので、
 * vi.hoisted を使用してモックの参照を先に確保します。
 */
const { abortAllRequestsMock, getBasketItemsMock } = vi.hoisted(() => {
  return {
    abortAllRequestsMock: vi.fn(),
    getBasketItemsMock: vi.fn(),
  }
})

vi.mock('@/api-client', () => ({
  basketItemsApi: () => ({
    getBasketItems: getBasketItemsMock,
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

describe('authenticationService_signOut', () => {
  beforeEach(() => {
    vi.clearAllMocks()
    sessionStorage.clear()
    setupPinia()
  })

  describe('ログアウト中に実行中だった API がログアウト後に完了して store の state を書き戻さない', () => {
    it('signOut 実行後に API がキャンセルされてもストアの state は初期値のまま', async () => {
      // Arrange
      const basketStore = useBasketStore()

      // API が遅延して解決される Promise を用意
      let rejectApi!: (reason: unknown) => void
      const delayedApiPromise = new Promise<{ data: BasketResponse }>((_resolve, reject) => {
        rejectApi = reject
      })
      getBasketItemsMock.mockReturnValue(delayedApiPromise)

      // abortAllRequests が呼ばれたら、ペンディング中の API を CanceledError で reject する
      // 実際の振る舞いを再現: AbortController.abort() により axios がリクエストをキャンセルする
      abortAllRequestsMock.mockImplementation(() => {
        rejectApi(new axios.CanceledError('canceled'))
      })

      // API 呼び出しを開始（完了を待たない）
      const fetchPromise = basketStore.fetch().catch(() => {
        // キャンセルによるエラーは無視
      })

      // Act: API が完了する前にログアウトを実行
      const { signOut } = authenticationService()
      signOut()

      await fetchPromise

      // Assert: ストアの state が初期値のまま（API レスポンスで書き戻されていない）
      expect(basketStore.basket).toEqual({})
      expect(basketStore.addedItemId).toBeUndefined()
      expect(basketStore.deletedItemIds).toEqual([])
      expect(abortAllRequestsMock).toHaveBeenCalled()
    })
  })

  describe('ログアウト後に store が初期化される', () => {
    it('全ストアの state が初期値にリセットされる', () => {
      // Arrange
      const authenticationStore = useAuthenticationStore()
      const basketStore = useBasketStore()
      const catalogStore = useCatalogStore()
      const notificationStore = useNotificationStore()

      // 各ストアにデータを設定
      authenticationStore.signIn()
      basketStore.basket = {
        buyerId: 'test',
        account: {
          consumptionTaxRate: 0.1,
          consumptionTax: 100,
          deliveryCharge: 500,
          totalItemsPrice: 1000,
          totalPrice: 1600,
        },
        basketItems: [],
      }
      basketStore.addedItemId = 42
      basketStore.deletedItemIds = [1, 2, 3]
      catalogStore.categories = [{ id: 1, name: 'カテゴリ1' }]
      catalogStore.brands = [{ id: 1, name: 'ブランド1' }]
      catalogStore.catalogItemPage = { items: [], totalCount: 10 }
      notificationStore.setMessage('エラー', 'id-1', 'タイトル', '詳細', 500, 10000)

      // Act
      const { signOut } = authenticationService()
      signOut()

      // Assert
      expect(authenticationStore.authenticationState).toBe(false)
      expect(authenticationStore.isAuthenticated).toBe(false)

      expect(basketStore.basket).toEqual({})
      expect(basketStore.addedItemId).toBeUndefined()
      expect(basketStore.deletedItemIds).toEqual([])

      expect(catalogStore.categories).toEqual([])
      expect(catalogStore.brands).toEqual([])
      expect(catalogStore.catalogItemPage).toEqual({})

      expect(notificationStore.message).toBe('')
      expect(notificationStore.id).toBe('')
      expect(notificationStore.title).toBe('')
      expect(notificationStore.detail).toBe('')
      expect(notificationStore.status).toBe(0)
      expect(notificationStore.timeout).toBe(5000)
    })
  })

  describe('ログアウト後にストレージが初期化される', () => {
    it('sessionStorage から isAuthenticated キーが削除される', () => {
      // Arrange
      const authenticationStore = useAuthenticationStore()
      authenticationStore.signIn()
      expect(sessionStorage.getItem('isAuthenticated')).toBe('true')

      // Act
      const { signOut } = authenticationService()
      signOut()

      // Assert
      expect(sessionStorage.getItem('isAuthenticated')).toBeNull()
    })
  })
})
