import { createRouter, createWebHistory } from 'vue-router'
import { authenticationRoutes } from '@/authentication/router/authentication'
import { displayItemRoutes } from '@/shopping/display-item/router/display-item'
import { basketRoutes } from '@/shopping/basket/router/basket'
import { orderingRoutes } from '@/shopping/ordering/router/ordering'
import { errorRoutes } from '@/system-common/router/error'

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
