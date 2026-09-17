import type { RouteRecordRaw } from 'vue-router'
import { orderingRouteNames } from './ordering-route-names'

export const orderingRoutes: RouteRecordRaw[] = [
  {
    path: '/ordering/checkout',
    name: orderingRouteNames.checkout,
    component: () => import('@/pages/ordering/checkout/CheckoutPage.vue'),
    meta: { requiresAuth: true },
  },
  {
    path: '/ordering/done/:orderId',
    name: orderingRouteNames.done,
    component: () => import('@/pages/ordering/done/DonePage.vue'),
    meta: { requiresAuth: true },
    props: (route) => ({
      orderId: String(route.params.orderId),
    }),
  },
]
