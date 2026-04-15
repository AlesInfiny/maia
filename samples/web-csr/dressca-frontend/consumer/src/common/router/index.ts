import { createRouter, createWebHistory } from 'vue-router'
import { authenticationRoutes } from '@/authentication/routes/authentication.routes'
import { catalogRoutes } from '@/catalog/routes/catalog.routes'
import { basketRoutes } from '@/basket/routes/basket.routes'
import { orderingRoutes } from '@/ordering/routes/ordering.routes'
import { errorRoutes } from '@/common/routes/error/error.routes'

export const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    ...authenticationRoutes,
    ...catalogRoutes,
    ...basketRoutes,
    ...orderingRoutes,
    ...errorRoutes,
  ],
})
