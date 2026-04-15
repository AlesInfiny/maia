import type { RouteRecordRaw } from 'vue-router'

export const catalogRoutes: RouteRecordRaw[] = [
  {
    path: '/',
    name: 'catalog',
    component: () => import('@/catalog/views/CatalogView.vue'),
    meta: { requiresAuth: false },
  },
]
