import type { NavigationGuardWithThis, RouteLocationRaw } from 'vue-router'

/**
 * 画面内の操作からだけ到達させる画面のナビゲーションガードを生成するための設定です。
 */
export interface InAppNavigationGuardOptions {
  /**
   * URL を直接指定してアクセスされたときの遷移先です。
   */
  fallback: RouteLocationRaw
}

/**
 * `meta.requiresInAppNavigation` に `true` を指定した画面に、
 * URL を直接指定してアクセスされたとき、既定の画面へ遷移させるナビゲーションガードを生成します。
 * 遷移元の画面がない場合を、 URL を直接指定したアクセスとみなします。
 * @param options ガードの設定。
 * @returns `router.beforeEach` に登録するナビゲーションガード。
 */
export function createInAppNavigationGuard(
  options: InAppNavigationGuardOptions,
): NavigationGuardWithThis<undefined> {
  return (to, from) => {
    if (to.meta.requiresInAppNavigation && !from.name) {
      return options.fallback
    }

    return true
  }
}
