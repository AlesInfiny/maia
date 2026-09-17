import type { RouteRecordRaw } from 'vue-router'
import { errorRouteNames } from './error-route-names'

export const errorRoutes: RouteRecordRaw[] = [
  {
    path: '/error',
    name: errorRouteNames.error,
    component: () => import('@/pages/ErrorPage.vue'),
    meta: { requiresAuth: false },
  },
  {
    path: '/:pathMatch(.*)*',
    name: errorRouteNames.notFound,
    component: () => import('@/pages/NotFoundPage.vue'),
    meta: { requiresAuth: false },
  },
]
