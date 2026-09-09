/**
 * カタログドメインのルート名です。
 * 画面遷移では文字列リテラルではなくこの定数を参照してください。
 * ドメイン外からは `@/system-common/router/route-names` 経由で参照します。
 */
export const catalogRouteNames = {
  /** カタログアイテムの一覧画面。 */
  items: 'catalog/items',
  /** カタログアイテムの編集画面。 */
  itemsEdit: 'catalog/items/edit',
  /** カタログアイテムの追加画面。 */
  itemsAdd: 'catalog/items/add',
} as const
