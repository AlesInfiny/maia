import type { RouteRecordRaw } from 'vue-router';

export const orderingRoutes: RouteRecordRaw[] = [
  {
    path: '/ordering/checkout',
    name: 'ordering/checkout',
    component: () => import('@/views/ordering/CheckoutView.vue'),
  },
  {
    path: '/ordering/done',
    name: 'ordering/done',
    component: () => import('@/views/ordering/DoneView.vue'),
  },
];
