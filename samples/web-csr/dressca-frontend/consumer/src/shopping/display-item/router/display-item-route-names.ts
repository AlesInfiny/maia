/**
 * 陳列品ドメインのルート名です。
 * 画面遷移では文字列リテラルではなくこの定数を参照してください。
 * ドメイン外からは `@/system-common/router/route-names` 経由で参照します。
 */
export const displayItemRouteNames = {
  /** 陳列品一覧（トップ）画面。 */
  displayItem: 'display-item',
} as const
