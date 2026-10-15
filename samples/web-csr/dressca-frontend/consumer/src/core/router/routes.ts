import type { RouteRecordRaw } from 'vue-router'
import { routeNames } from './route-names'

export const routes: RouteRecordRaw[] = [
  {
    path: '/',
    name: routeNames.displayItem,
    component: () => import('@/pages/DisplayItemPage.vue'),
    meta: { allowAnonymous: true },
  },
  {
    path: '/authentication/login',
    name: routeNames.login,
    component: () => import('@/pages/authentication/login/LoginPage.vue'),
    meta: { allowAnonymous: true },
  },
  {
    path: '/basket',
    name: routeNames.basket,
    component: () => import('@/pages/basket/BasketPage.vue'),
    meta: { allowAnonymous: true },
  },
  {
    path: '/ordering/checkout',
    name: routeNames.checkout,
    component: () => import('@/pages/ordering/checkout/CheckoutPage.vue'),
    meta: { requiresInAppNavigation: true },
  },
  {
    path: '/ordering/done/:orderId',
    name: routeNames.done,
    component: () => import('@/pages/ordering/done/DonePage.vue'),
    meta: { requiresInAppNavigation: true },
  },
  {
    path: '/error',
    name: routeNames.error,
    component: () => import('@/pages/ErrorPage.vue'),
    meta: { allowAnonymous: true },
  },
  {
    path: '/:pathMatch(.*)*',
    name: routeNames.notFound,
    component: () => import('@/pages/NotFoundPage.vue'),
    meta: { allowAnonymous: true },
  },
]
