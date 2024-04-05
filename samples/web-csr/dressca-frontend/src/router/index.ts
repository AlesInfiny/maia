import { createRouter, createWebHistory } from 'vue-router';
import { accountRoutes } from '@/router/authentication/authentication';
import { catalogRoutes } from '@/router/catalog/catalog';
import { basketRoutes } from '@/router/basket/basket';
import { orderingRoutes } from '@/router/ordering/ordering';
import { errorRoutes } from '@/router/error/error';

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    ...accountRoutes,
    ...catalogRoutes,
    ...basketRoutes,
    ...orderingRoutes,
    ...errorRoutes,
  ],
});

export default router;
