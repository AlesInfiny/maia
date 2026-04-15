import type { RouteRecordRaw } from 'vue-router'

/**
 * '/authentication/login'に割り当てるコンポーネントを定義します。
 */
export const authenticationRoutes: RouteRecordRaw[] = [
  {
    path: '/authentication/login',
    name: 'authentication/login',
    component: () => import('@/authentication/views/LoginView.vue'),
    meta: { requiresAuth: false },
  },
]
