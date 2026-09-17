import type { RouteRecordRaw } from 'vue-router'
import { orderingRouteNames } from './ordering-route-names'

export const orderingRoutes: RouteRecordRaw[] = [
  {
    path: '/ordering/checkout',
    name: orderingRouteNames.checkout,
    component: () => import('@/shopping/ordering/views/CheckoutView.vue'),
    meta: { requiresAuth: true },
  },
  {
    path: '/ordering/done/:orderId',
    name: orderingRouteNames.done,
    component: () => import('@/shopping/ordering/views/DoneView.vue'),
    meta: { requiresAuth: true },
    props: (route) => ({
      orderId: String(route.params.orderId),
    }),
  },
]
