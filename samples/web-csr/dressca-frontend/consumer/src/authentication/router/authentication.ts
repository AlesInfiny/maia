import type { RouteRecordRaw } from 'vue-router'

export const authenticationRoutes: RouteRecordRaw[] = [
  {
    path: '/authentication/login',
    name: 'authentication/login',
    component: () => import('@/authentication/views/LoginView.vue'),
    meta: { requiresAuth: false },
  },
]
