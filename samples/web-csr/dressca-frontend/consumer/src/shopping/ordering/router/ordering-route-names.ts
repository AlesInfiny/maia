/**
 * 注文ドメインのルート名です。
 * 画面遷移では文字列リテラルではなくこの定数を参照してください。
 * ドメイン外からは `@/system-common/router/route-names` 経由で参照します。
 */
export const orderingRouteNames = {
  /** 注文確認画面。 */
  checkout: 'ordering/checkout',
  /** 注文完了画面。 */
  done: 'ordering/done',
} as const
