import type { RouteRecordRaw } from 'vue-router'
import { errorRouteNames } from './error-route-names'

export const errorRoutes: RouteRecordRaw[] = [
  {
    path: '/error',
    name: errorRouteNames.error,
    component: () => import('@/system-common/views/ErrorView.vue'),
    meta: { requiresAuth: false },
  },
  {
    path: '/:pathMatch(.*)*',
    name: errorRouteNames.notFound,
    component: () => import('@/system-common/views/NotFoundView.vue'),
    meta: { requiresAuth: false },
  },
]
