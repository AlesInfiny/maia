import type { RouteRecordInfo } from 'vue-router'

/**
 * 画面のルート名です。
 * 画面遷移では文字列リテラルではなくこの定数を参照してください。
 */
export const routeNames = {
  /** ホーム画面。 */
  home: 'home',
  /** ログイン画面。 */
  login: 'authentication-login',
  /** カタログアイテム一覧画面。 */
  catalogItems: 'catalog-items',
  /** カタログアイテム追加画面。 */
  catalogItemsAdd: 'catalog-items-add',
  /** カタログアイテム編集画面。 */
  catalogItemsEdit: 'catalog-items-edit',
  /** エラー画面。 */
  error: 'error',
  /** どのルートにも該当しない場合の画面。 */
  notFound: 'not-found',
} as const

/**
 * ルート名ごとのパスとパラメーターの型です。
 * vue-router の型を拡張し、 `router.push` や `useRoute` でルート名とパラメーターの組み合わせを検査します。
 * ルート表（ routes.ts ）にルートを追加したときは、この型にも追加します。
 */
export interface RouteNamedMap {
  [routeNames.home]: RouteRecordInfo<typeof routeNames.home, '/'>
  [routeNames.login]: RouteRecordInfo<typeof routeNames.login, '/authentication/login'>
  [routeNames.catalogItems]: RouteRecordInfo<typeof routeNames.catalogItems, '/catalog/items'>
  [routeNames.catalogItemsAdd]: RouteRecordInfo<
    typeof routeNames.catalogItemsAdd,
    '/catalog/items/add'
  >
  [routeNames.catalogItemsEdit]: RouteRecordInfo<
    typeof routeNames.catalogItemsEdit,
    '/catalog/items/edit/:itemId',
    { itemId: string | number },
    { itemId: string }
  >
  [routeNames.error]: RouteRecordInfo<typeof routeNames.error, '/error'>
  [routeNames.notFound]: RouteRecordInfo<
    typeof routeNames.notFound,
    '/:pathMatch(.*)*',
    { pathMatch: string | string[] },
    { pathMatch: string[] }
  >
}

declare module 'vue-router' {
  interface TypesConfig {
    RouteNamedMap: RouteNamedMap
  }
}
