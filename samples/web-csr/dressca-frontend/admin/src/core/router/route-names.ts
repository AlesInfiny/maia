import type { RouteRecordInfo } from 'vue-router'

export const routeNames = {
  home: 'home',
  login: 'authentication-login',
  catalogItems: 'catalog-items',
  catalogItemsAdd: 'catalog-items-add',
  catalogItemsEdit: 'catalog-items-edit',
  error: 'error',
  notFound: 'not-found',
} as const

/** vue-router の型を拡張し、 `router.push` や `useRoute` でルート名とパラメーターの組み合わせを検査します。 */
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
