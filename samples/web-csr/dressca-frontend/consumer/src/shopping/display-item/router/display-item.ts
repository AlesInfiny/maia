import type { RouteRecordRaw } from 'vue-router'

export const displayItemRoutes: RouteRecordRaw[] = [
  {
    path: '/',
    name: 'display-item',
    component: () => import('@/shopping/display-item/views/DisplayItemView.vue'),
    meta: { requiresAuth: false },
  },
]
