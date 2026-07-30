import type { RouteRecordRaw } from 'vue-router'

export const displayItemRoutes: RouteRecordRaw[] = [
  {
    path: '/display-item',
    name: 'displayItem',
    component: () => import('@/views/displayitem/DisplayItemView.vue'),
    meta: { requiresAuth: false },
  },
]
