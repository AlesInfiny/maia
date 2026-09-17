import type { RouteRecordRaw } from 'vue-router'
import { errorRouteNames } from './error-route-names'

/**
 * '/error'に割り当てるコンポーネントを定義します。
 */
export const errorRoutes: RouteRecordRaw[] = [
  {
    path: '/error',
    name: errorRouteNames.error,
    component: () => import('@/pages/ErrorPage.vue'),
    meta: { requiresAuth: true },
  },
  {
    path: '/:pathMatch(.*)*',
    name: errorRouteNames.notFound,
    component: () => import('@/pages/NotFoundPage.vue'),
    meta: { requiresAuth: true },
  },
]
