import type { RouteRecordRaw } from 'vue-router'

export const errorRoutes: RouteRecordRaw[] = [
  {
    path: '/error',
    name: 'error',
    component: () => import('@/common/routes/error/ErrorView.vue'),
    meta: { requiresAuth: false },
  },
  {
    path: '/:pathMatch(.*)*',
    name: 'notFound',
    component: () => import('@/common/routes/error/NotFoundView.vue'),
    meta: { requiresAuth: false },
  },
]
