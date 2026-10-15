import type { RouteRecordInfo } from 'vue-router'

export const routeNames = {
  displayItem: 'display-item',
  login: 'authentication-login',
  basket: 'basket',
  checkout: 'ordering-checkout',
  done: 'ordering-done',
  error: 'error',
  notFound: 'not-found',
} as const

/** vue-router の型を拡張し、 `router.push` や `useRoute` でルート名とパラメーターの組み合わせを検査します。 */
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

/** vue-router の HistoryState はインデックスシグネチャを持つため、インターフェイスではなく型エイリアスで定義します。 */
export type BasketRouteState = {
  addedItemId: string
}

declare module 'vue-router' {
  interface TypesConfig {
    RouteNamedMap: RouteNamedMap
  }
}
