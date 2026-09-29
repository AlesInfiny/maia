import type { RouteRecordRaw } from 'vue-router'
import { catalogRouteNames } from './catalog-route-names'

/**
 * '/catalog/'に割り当てるコンポーネントを定義します。
 */
export const catalogRoutes: RouteRecordRaw[] = [
  {
    path: '/catalog/items',
    name: catalogRouteNames.items,
    component: () => import('@/catalog-management/catalog/views/ItemsView.vue'),
    meta: { requiresAuth: true },
  },
  {
    path: '/catalog/items/edit/:itemId',
    name: catalogRouteNames.itemsEdit,
    component: () => import('@/catalog-management/catalog/views/ItemsEditView.vue'),
    meta: { requiresAuth: true },
  },
  {
    path: '/catalog/items/add',
    name: catalogRouteNames.itemsAdd,
    component: () => import('@/catalog-management/catalog/views/ItemsAddView.vue'),
    meta: { requiresAuth: true },
  },
]
