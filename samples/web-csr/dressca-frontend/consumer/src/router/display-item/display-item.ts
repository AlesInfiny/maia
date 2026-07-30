import type { RouteRecordRaw } from 'vue-router'

export const displayItemRoutes: RouteRecordRaw[] = [
  {
    path: '/display-item',
    name: 'display-item',
    component: () => import('@/views/display-item/DisplayItemView.vue'),
    meta: { requiresAuth: false },
  },
]
