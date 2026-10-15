import type { Router } from 'vue-router'
import { routeNames } from './route-names'
import { createAuthenticationGuard } from '@/security/public-api'
import { createInAppNavigationGuard } from '@/system-common/guards/in-app-navigation-guard'

/**
 * アプリケーションのナビゲーションガードをルーターに登録します。
 * @param router ガードを登録するルーター。
 */
export function registerNavigationGuards(router: Router) {
  router.beforeEach(createInAppNavigationGuard({ fallback: { name: routeNames.displayItem } }))
  router.beforeEach(
    createAuthenticationGuard({
      toLogin: (redirect) => ({ name: routeNames.login, query: { redirect } }),
    }),
  )
}
