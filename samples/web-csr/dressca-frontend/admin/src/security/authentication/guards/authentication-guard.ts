import type { NavigationGuardWithThis, RouteLocationRaw } from 'vue-router'
import { useAuthenticationStore } from '@/security/authorization/stores/authentication'

/**
 * 認証のナビゲーションガードを生成するための設定です。
 */
export interface AuthenticationGuardOptions {
  /**
   * 未認証のときの遷移先を返します。
   * 遷移先の画面は利用する側（ app 層）が決めます。
   * @param redirect 認証後に戻る画面のパス。
   * @returns 未認証のときの遷移先。
   */
  toLogin: (redirect: string) => RouteLocationRaw
}

/**
 * 認証が必要な画面に未認証でアクセスしたとき、ログイン画面へ誘導するナビゲーションガードを生成します。
 * `meta.requiresAuth` に `false` を指定した画面以外は、認証が必要な画面として扱います。
 * @param options ガードの設定。
 * @returns `router.beforeEach` に登録するナビゲーションガード。
 */
export function createAuthenticationGuard(
  options: AuthenticationGuardOptions,
): NavigationGuardWithThis<undefined> {
  return (to) => {
    const authenticationStore = useAuthenticationStore()

    if (to.meta.requiresAuth !== false && !authenticationStore.isAuthenticated) {
      return options.toLogin(to.fullPath)
    }

    return true
  }
}
