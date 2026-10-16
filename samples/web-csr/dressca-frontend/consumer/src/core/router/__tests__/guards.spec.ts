import { beforeEach, describe, expect, it } from 'vitest'
import { createPinia, setActivePinia } from 'pinia'
import { defineComponent } from 'vue'
import { createMemoryHistory, createRouter, type Router, type RouteRecordRaw } from 'vue-router'
import { routes } from '@/core/router/routes'
import { registerNavigationGuards } from '@/core/router/guards'
import { routeNames } from '@/core/router/route-names'
import { useAuthentication } from '@/security/authentication/composables/use-authentication'

const StubPage = defineComponent({ name: 'StubPage', render: () => null })

/**
 * ナビゲーションガードを登録した、テスト用のルーターを生成します。
 * @returns テスト用のルーター。
 */
function createGuardedRouter(): Router {
  const stubRoutes = routes.map((route) => ({ ...route, component: StubPage }) as RouteRecordRaw)
  const router = createRouter({ history: createMemoryHistory(), routes: stubRoutes })
  registerNavigationGuards(router)
  return router
}

describe('アプリケーションのナビゲーションガード', () => {
  beforeEach(() => {
    sessionStorage.clear()
    setActivePinia(createPinia())
  })

  it('注文確認画面に URL を直接指定してアクセスすると_トップページに遷移する', async () => {
    // Arrange
    const router = createGuardedRouter()
    useAuthentication().signIn()
    // Act
    await router.push('/ordering/checkout')
    // Assert
    expect(router.currentRoute.value.name).toBe(routeNames.displayItem)
  })

  it('未認証で画面内から注文確認画面に遷移すると_戻り先を付けてログイン画面に遷移する', async () => {
    // Arrange
    const router = createGuardedRouter()
    await router.push('/basket')
    // Act
    await router.push({ name: routeNames.checkout })
    // Assert
    expect(router.currentRoute.value.name).toBe(routeNames.login)
    expect(router.currentRoute.value.query.redirect).toBe('/ordering/checkout')
  })

  it('認証済みで画面内から注文確認画面に遷移すると_注文確認画面に遷移する', async () => {
    // Arrange
    const router = createGuardedRouter()
    useAuthentication().signIn()
    await router.push('/basket')
    // Act
    await router.push({ name: routeNames.checkout })
    // Assert
    expect(router.currentRoute.value.name).toBe(routeNames.checkout)
  })

  it('未認証で認証が不要な画面にアクセスすると_その画面に遷移する', async () => {
    // Arrange
    const router = createGuardedRouter()
    // Act
    await router.push('/basket')
    // Assert
    expect(router.currentRoute.value.name).toBe(routeNames.basket)
  })
})
