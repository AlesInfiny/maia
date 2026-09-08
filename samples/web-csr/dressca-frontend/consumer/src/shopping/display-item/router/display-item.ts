import type { RouteRecordRaw } from 'vue-router'
import { displayItemRouteNames } from './display-item-route-names'

export const displayItemRoutes: RouteRecordRaw[] = [
  {
    path: '/',
    name: displayItemRouteNames.displayItem,
    component: () => import('@/shopping/display-item/views/DisplayItemView.vue'),
    meta: { requiresAuth: false },
  },
]
