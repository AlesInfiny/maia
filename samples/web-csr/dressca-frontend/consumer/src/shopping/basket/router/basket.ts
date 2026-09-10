import type { RouteRecordRaw } from 'vue-router'
import { basketRouteNames } from './basket-route-names'

export const basketRoutes: RouteRecordRaw[] = [
  {
    path: '/basket',
    name: basketRouteNames.basket,
    component: () => import('@/shopping/basket/views/BasketView.vue'),
    meta: { requiresAuth: false },
  },
]
