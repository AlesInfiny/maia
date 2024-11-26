import type { RouteRecordRaw } from 'vue-router';

/**
 * '/error'に割り当てるコンポーネントを定義します。
 */
export const errorRoutes: RouteRecordRaw[] = [
  {
    path: '/error',
    name: 'error',
    component: () => import('@/views/error/ErrorView.vue'),
  },
];
