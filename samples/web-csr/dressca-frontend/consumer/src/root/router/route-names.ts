import type { RouteRecordInfo } from 'vue-router'

/**
 * 画面のルート名です。
 * 画面遷移では文字列リテラルではなくこの定数を参照してください。
 */
export const routeNames = {
  /** 陳列品一覧（トップ）画面。 */
  displayItem: 'display-item',
  /** ログイン画面。 */
  login: 'authentication-login',
  /** 買い物かご画面。 */
  basket: 'basket',
  /** 注文確認画面。 */
  checkout: 'ordering-checkout',
  /** 注文完了画面。 */
  done: 'ordering-done',
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
  [routeNames.displayItem]: RouteRecordInfo<typeof routeNames.displayItem, '/'>
  [routeNames.login]: RouteRecordInfo<typeof routeNames.login, '/authentication/login'>
  [routeNames.basket]: RouteRecordInfo<typeof routeNames.basket, '/basket'>
  [routeNames.checkout]: RouteRecordInfo<typeof routeNames.checkout, '/ordering/checkout'>
  [routeNames.done]: RouteRecordInfo<
    typeof routeNames.done,
    '/ordering/done/:orderId',
    { orderId: string | number },
    { orderId: string }
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
