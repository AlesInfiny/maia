import type { RouteRecordRaw } from 'vue-router'
import { basketRouteNames } from './basket-route-names'

export const basketRoutes: RouteRecordRaw[] = [
  {
    path: '/basket',
    name: basketRouteNames.basket,
    component: () => import('@/pages/basket/BasketPage.vue'),
    meta: { requiresAuth: false },
  },
]
