import type { RouteRecordRaw } from 'vue-router'
import { homeRouteNames } from './home-route-names'

/**
 * '/'に割り当てるコンポーネントを定義します。
 */
export const homeRoutes: RouteRecordRaw[] = [
  {
    path: '/',
    name: homeRouteNames.home,
    component: () => import('@/system-common/views/HomeView.vue'),
    meta: { requiresAuth: true },
  },
]
