import type { RouteRecordRaw } from 'vue-router'

export const displayRoutes: RouteRecordRaw[] = [
  {
    path: '/',
    name: 'catalog',
    component: () => import('@/views/display/DisplayView.vue'),
    meta: { requiresAuth: false },
  },
]
