import type { NavigationGuardWithThis, RouteLocationRaw } from 'vue-router'
import { useAuthenticationStore } from '../stores/authentication'

export interface AuthenticationGuardOptions {
  toLogin: (redirect: string) => RouteLocationRaw
}

/**
 * 認証が必要な画面に未認証でアクセスしたとき、ログイン画面へ誘導するナビゲーションガードを生成します。
 * @param options ガードの設定。
 * @returns `router.beforeEach` に登録するナビゲーションガード。
 */
export function createAuthenticationGuard(
  options: AuthenticationGuardOptions,
): NavigationGuardWithThis<undefined> {
  return (to) => {
    const authenticationStore = useAuthenticationStore()

    if (!to.meta.allowAnonymous && !authenticationStore.isAuthenticated) {
      return options.toLogin(to.fullPath)
    }

    return true
  }
}
