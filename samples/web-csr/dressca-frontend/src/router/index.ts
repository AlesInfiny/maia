import { createRouter, createWebHistory } from 'vue-router';
import { accountRoutes } from '@/router/account/account';
import { catalogRoutes } from '@/router/catalog/catalog';
import { basketRoutes } from '@/router/basket/basket';
import { orderingRoutes } from '@/router/ordering/ordering';

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    ...accountRoutes,
    ...catalogRoutes,
    ...basketRoutes,
    ...orderingRoutes,
  ],
});

export default router;
