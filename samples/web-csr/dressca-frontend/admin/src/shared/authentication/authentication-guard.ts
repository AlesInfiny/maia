import type { Router } from 'vue-router'
import { useAuthenticationStore } from '@/stores/authentication/authentication'

/**
 * ナビゲーションガードです。
 * 画面遷移する際に共通して実行する処理を定義します。
 * たとえば、未認証の場合はログイン画面に遷移させます。
 * @param router vue-router。
 */
export const authenticationGuard = (router: Router) => {
  router.beforeEach((to) => {
    const authenticationStore = useAuthenticationStore()

    if (to.meta.requiresAuth && !authenticationStore.isAuthenticated) {
      return {
        name: 'authentication/login',
        query: {
          redirectName: to.name?.toString(),
          redirectParams: JSON.stringify(to.params),
          redirectQuery: JSON.stringify(to.query),
        },
      }
    }

    return true
  })
}
