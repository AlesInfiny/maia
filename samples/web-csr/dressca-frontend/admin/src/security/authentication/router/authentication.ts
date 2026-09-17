import type { RouteRecordRaw } from 'vue-router'
import { authenticationRouteNames } from './authentication-route-names'

/**
 * '/authentication/login'に割り当てるコンポーネントを定義します。
 */
export const authenticationRoutes: RouteRecordRaw[] = [
  {
    path: '/authentication/login',
    name: authenticationRouteNames.login,
    component: () => import('@/pages/authentication/login/LoginPage.vue'),
    meta: { requiresAuth: false },
  },
]
