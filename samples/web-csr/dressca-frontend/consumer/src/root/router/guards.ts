import type { Router } from 'vue-router'
import { routeNames } from './route-names'
import { createAuthenticationGuard } from '@/security/public-api'
import { createInAppNavigationGuard } from '@/system-common/guards/in-app-navigation-guard'

/**
 * アプリケーションのナビゲーションガードをルーターに登録します。
 * 各ガードが必要とする遷移先は、ここで画面のルート名を指定して結線します。
 * @param router ガードを登録するルーター。
 */
export function registerNavigationGuards(router: Router) {
  // 注文確認・注文完了の画面へ URL を直接指定してアクセスされた場合は、トップページへ遷移します。
  router.beforeEach(createInAppNavigationGuard({ fallback: { name: routeNames.displayItem } }))
  router.beforeEach(
    createAuthenticationGuard({
      toLogin: (redirect) => ({ name: routeNames.login, query: { redirect } }),
    }),
  )
}
