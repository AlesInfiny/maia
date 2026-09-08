import type { RouteRecordRaw } from 'vue-router'

export const basketRoutes: RouteRecordRaw[] = [
  {
    path: '/basket',
    name: 'basket',
    component: () => import('@/shopping/basket/views/BasketView.vue'),
    meta: { requiresAuth: false },
  },
]
