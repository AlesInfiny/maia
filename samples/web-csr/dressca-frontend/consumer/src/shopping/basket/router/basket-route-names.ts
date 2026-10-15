/**
 * 買い物かごドメインのルート名です。
 * 画面遷移では文字列リテラルではなくこの定数を参照してください。
 * ドメイン外からは `@/system-common/router/route-names` 経由で参照します。
 */
export const basketRouteNames = {
  /** 買い物かご画面。 */
  basket: 'basket',
} as const
