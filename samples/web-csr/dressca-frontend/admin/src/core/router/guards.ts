import type { Router } from 'vue-router'
import { routeNames } from './route-names'
import { createAuthenticationGuard } from '@/security/public-api'

/**
 * アプリケーションのナビゲーションガードをルーターに登録します。
 * @param router ガードを登録するルーター。
 */
export function registerNavigationGuards(router: Router) {
  router.beforeEach(
    createAuthenticationGuard({
      toLogin: (redirect) => ({ name: routeNames.login, query: { redirect } }),
    }),
  )
}
