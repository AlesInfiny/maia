import { createRouter, createWebHistory } from 'vue-router'
import { authenticationRoutes } from '@/router/authentication/authentication'
import { displayRoutes } from '@/router/display/display'
import { basketRoutes } from '@/router/basket/basket'
import { orderingRoutes } from '@/router/ordering/ordering'
import { errorRoutes } from '@/router/error/error'

export const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    ...authenticationRoutes,
    ...displayRoutes,
    ...basketRoutes,
    ...orderingRoutes,
    ...errorRoutes,
  ],
})
