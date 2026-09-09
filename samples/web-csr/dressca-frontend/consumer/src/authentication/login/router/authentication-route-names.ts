/**
 * 認証ドメインのルート名です。
 * 画面遷移では文字列リテラルではなくこの定数を参照してください。
 * ドメイン外からは `@/system-common/router/route-names` 経由で参照します。
 */
export const authenticationRouteNames = {
  /** ログイン画面。 */
  login: 'authentication/login',
} as const
