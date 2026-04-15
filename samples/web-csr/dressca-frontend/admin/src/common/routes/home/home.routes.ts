import type { RouteRecordRaw } from 'vue-router'

/**
 * '/'に割り当てるコンポーネントを定義します。
 */
export const homeRoutes: RouteRecordRaw[] = [
  {
    path: '/',
    name: 'home',
    component: () => import('@/common/routes/home/HomeView.vue'),
    meta: { requiresAuth: true },
  },
]
