import { beforeEach, describe, expect, it } from 'vitest'
import { createPinia, setActivePinia } from 'pinia'
import { createMemoryHistory, createRouter, type Router } from 'vue-router'
import { routes } from '@/app/router/routes'
import { registerNavigationGuards } from '@/app/router/guards'
import { routeNames } from '@/app/router/route-names'
import { useAuthenticationStore } from '@/security/public-api'

/**
 * ナビゲーションガードを登録した、テスト用のルーターを生成します。
 * @returns テスト用のルーター。
 */
function createGuardedRouter(): Router {
  const router = createRouter({ history: createMemoryHistory(), routes })
  registerNavigationGuards(router)
  return router
}

describe('認証のナビゲーションガード', () => {
  beforeEach(() => {
    sessionStorage.clear()
    setActivePinia(createPinia())
  })

  it('未認証で認証が必要な画面にアクセスすると_戻り先を付けてログイン画面に遷移する', async () => {
    // Arrange
    const router = createGuardedRouter()
    // Act
    await router.push('/catalog/items/edit/1?tab=detail')
    // Assert
    expect(router.currentRoute.value.name).toBe(routeNames.login)
    expect(router.currentRoute.value.query.redirect).toBe('/catalog/items/edit/1?tab=detail')
  })

  it('未認証で認証が不要な画面にアクセスすると_その画面に遷移する', async () => {
    // Arrange
    const router = createGuardedRouter()
    // Act
    await router.push('/authentication/login')
    // Assert
    expect(router.currentRoute.value.name).toBe(routeNames.login)
    expect(router.currentRoute.value.query.redirect).toBeUndefined()
  })

  it('認証済みで認証が必要な画面にアクセスすると_その画面に遷移する', async () => {
    // Arrange
    const router = createGuardedRouter()
    useAuthenticationStore().authenticationState = true
    // Act
    await router.push('/catalog/items')
    // Assert
    expect(router.currentRoute.value.name).toBe(routeNames.catalogItems)
  })
})
