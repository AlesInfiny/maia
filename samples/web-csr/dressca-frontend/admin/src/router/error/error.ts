import type { RouteRecordRaw } from 'vue-router'

/**
 * '/error'に割り当てるコンポーネントを定義します。
 */
export const errorRoutes: RouteRecordRaw[] = [
  {
    path: '/error',
    name: 'error',
    component: () => import('@/views/error/ErrorView.vue'),
    meta: { requiresAuth: true },
  },
  {
    path: '/:pathMatch(.*)*',
    name: 'notFound',
    component: () => import('@/views/error/NotFoundView.vue'),
    meta: { requiresAuth: true },
  },
]
