import type { RouteRecordRaw } from 'vue-router';

export const orderingRoutes: RouteRecordRaw[] = [
  {
    path: '/ordering/checkout',
    name: 'ordering/checkout',
    component: () => import('@/views/ordering/CheckoutView.vue'),
    meta: { requiresAuth: true },
  },
  {
    path: '/ordering/done/:orderId(\\d+)',
    name: 'ordering/done',
    component: () => import('@/views/ordering/DoneView.vue'),
    meta: { requiresAuth: true },
    props: (route) => ({
      orderId: Number(route.params.orderId),
    }),
  },
];
