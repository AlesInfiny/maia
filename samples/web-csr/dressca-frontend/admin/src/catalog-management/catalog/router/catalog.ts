import type { RouteRecordRaw } from 'vue-router'
import { catalogRouteNames } from './catalog-route-names'

/**
 * '/catalog/'に割り当てるコンポーネントを定義します。
 */
export const catalogRoutes: RouteRecordRaw[] = [
  {
    path: '/catalog/items',
    name: catalogRouteNames.items,
    component: () => import('@/pages/catalog/items/ItemsPage.vue'),
    meta: { requiresAuth: true },
  },
  {
    path: '/catalog/items/edit/:itemId',
    name: catalogRouteNames.itemsEdit,
    component: () => import('@/pages/catalog/items/edit/ItemsEditPage.vue'),
    meta: { requiresAuth: true },
  },
  {
    path: '/catalog/items/add',
    name: catalogRouteNames.itemsAdd,
    component: () => import('@/pages/catalog/items/add/ItemsAddPage.vue'),
    meta: { requiresAuth: true },
  },
]
