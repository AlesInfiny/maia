import type { RouteRecordRaw } from 'vue-router'

/**
 * '/authentication/login'に割り当てるコンポーネントを定義します。
 */
export const authenticationRoutes: RouteRecordRaw[] = [
  {
    path: '/authentication/login',
    name: 'authentication/login',
    component: () => import('@/views/authentication/LoginView.vue'),
    meta: { requiresAuth: false },
  },
]
