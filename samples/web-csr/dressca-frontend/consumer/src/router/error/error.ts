import type { RouteRecordRaw } from 'vue-router';

export const errorRoutes: RouteRecordRaw[] = [
  {
    path: '/error',
    name: 'error',
    component: () => import('@/views/error/ErrorView.vue'),
    meta: { requiresAuth: false },
  },
];
