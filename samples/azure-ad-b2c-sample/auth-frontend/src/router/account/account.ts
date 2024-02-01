import type { RouteRecordRaw } from 'vue-router';

export const accountRoutes: RouteRecordRaw[] = [
  {
    path: '/account/login',
    name: 'account/login',
    component: () => import('@/views/account/LoginView.vue'),
  },
];
