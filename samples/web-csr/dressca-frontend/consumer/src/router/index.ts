import { createRouter, createWebHistory } from 'vue-router'
import { authenticationRoutes } from '@/router/authentication/authentication'
import { displayItemRoutes } from '@/router/displayitem/displayitem'
import { basketRoutes } from '@/router/basket/basket'
import { orderingRoutes } from '@/router/ordering/ordering'
import { errorRoutes } from '@/router/error/error'

export const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    ...authenticationRoutes,
    ...displayItemRoutes,
    ...basketRoutes,
    ...orderingRoutes,
    ...errorRoutes,
  ],
})
