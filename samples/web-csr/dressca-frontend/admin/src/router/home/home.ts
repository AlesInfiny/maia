import type { RouteRecordRaw } from 'vue-router';

/**
 * '/'に割り当てるコンポーネントを定義します。
 */
export const homeRoutes: RouteRecordRaw[] = [
  {
    path: '/',
    name: 'home',
    component: () => import('@/views/home/HomeView.vue'),
    meta: { requiresAuth: true },
  },
];
