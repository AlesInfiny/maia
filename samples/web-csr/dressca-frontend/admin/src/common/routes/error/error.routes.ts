import type { RouteRecordRaw } from 'vue-router'

/**
 * '/error'に割り当てるコンポーネントを定義します。
 */
export const errorRoutes: RouteRecordRaw[] = [
  {
    path: '/error',
    name: 'error',
    component: () => import('@/common/routes/error/ErrorView.vue'),
    meta: { requiresAuth: true },
  },
  {
    path: '/:pathMatch(.*)*',
    name: 'notFound',
    component: () => import('@/common/routes/error/NotFoundView.vue'),
    meta: { requiresAuth: true },
  },
]
