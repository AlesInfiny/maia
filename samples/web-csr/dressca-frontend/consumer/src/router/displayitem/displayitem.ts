import type { RouteRecordRaw } from 'vue-router'

export const displayRoutes: RouteRecordRaw[] = [
  {
    path: '/',
    name: 'display',
    component: () => import('@/views/display/DisplayView.vue'),
    meta: { requiresAuth: false },
  },
]
