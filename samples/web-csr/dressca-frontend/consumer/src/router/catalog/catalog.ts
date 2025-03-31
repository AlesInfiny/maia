import type { RouteRecordRaw } from 'vue-router';

export const catalogRoutes: RouteRecordRaw[] = [
  {
    path: '/',
    name: 'catalog',
    component: () => import('@/views/catalog/CatalogView.vue'),
    meta: { requiresAuth: false },
  },
];
