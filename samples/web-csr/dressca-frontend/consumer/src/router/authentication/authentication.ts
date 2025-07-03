import type { RouteRecordRaw } from 'vue-router'

export const authenticationRoutes: RouteRecordRaw[] = [
  {
    path: '/authentication/login',
    name: 'authentication/login',
    component: () => import('@/views/authentication/LoginView.vue'),
    meta: { requiresAuth: false },
  },
]
