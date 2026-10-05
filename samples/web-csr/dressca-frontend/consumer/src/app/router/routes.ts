import type { RouteRecordRaw } from 'vue-router'
import { routeNames } from './route-names'

/**
 * アプリケーションのルート表です。
 * 画面（ src/pages ）の配置と URL の対応、認証の要否などのルートの属性をここで一覧できます。
 * `meta.requiresAuth` を指定しないルートは、認証が必要な画面として扱います。
 */
export const routes: RouteRecordRaw[] = [
  {
    path: '/',
    name: routeNames.displayItem,
    component: () => import('@/pages/DisplayItemPage.vue'),
    meta: { requiresAuth: false },
  },
  {
    path: '/authentication/login',
    name: routeNames.login,
    component: () => import('@/pages/authentication/login/LoginPage.vue'),
    meta: { requiresAuth: false },
  },
  {
    path: '/basket',
    name: routeNames.basket,
    component: () => import('@/pages/basket/BasketPage.vue'),
    meta: { requiresAuth: false },
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
    meta: { requiresAuth: false },
  },
  {
    path: '/:pathMatch(.*)*',
    name: routeNames.notFound,
    component: () => import('@/pages/NotFoundPage.vue'),
    meta: { requiresAuth: false },
  },
]
