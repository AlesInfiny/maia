import type { RouteRecordRaw } from 'vue-router'

/**
 * '/catalog/'に割り当てるコンポーネントを定義します。
 */
export const catalogRoutes: RouteRecordRaw[] = [
  {
    path: '/catalog/items',
    name: 'catalog/items',
    component: () => import('@/catalog/views/ItemsView.vue'),
    meta: { requiresAuth: true },
  },
  {
    path: '/catalog/items/edit/:itemId(\\d+)',
    name: 'catalog/items/edit',
    component: () => import('@/catalog/views/ItemsEditView.vue'),
    meta: { requiresAuth: true },
  },
  {
    path: '/catalog/items/add',
    name: 'catalog/items/add',
    component: () => import('@/catalog/views/ItemsAddView.vue'),
    meta: { requiresAuth: true },
  },
]
