import { createRouter, createWebHistory } from 'vue-router'
import { catalogRoutes } from '@/catalog/routes/catalog.routes'
import { authenticationRoutes } from '@/authentication/routes/authentication.routes'
import { homeRoutes } from '@/common/routes/home/home.routes'
import { errorRoutes } from '@/common/routes/error/error.routes'

/**
 * vue-routerを作成します。
 */
export const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [...errorRoutes, ...homeRoutes, ...catalogRoutes, ...authenticationRoutes],
})
