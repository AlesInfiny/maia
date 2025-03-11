import type { RouteRecordRaw } from 'vue-router';

/**
 * '/catalog/'に割り当てるコンポーネントを定義します。
 */
export const catalogRoutes: RouteRecordRaw[] = [
  {
    path: '/catalog/items',
    name: 'catalog/items',
    component: () => import('@/views/catalog/ItemsView.vue'),
  },
  {
    path: '/catalog/items/edit/:itemId',
    name: 'catalog/items/edit',
    component: () => import('@/views/catalog/ItemsEditView.vue'),
  },
  {
    path: '/catalog/items/add',
    name: 'catalog/items/add',
    component: () => import('@/views/catalog/ItemsAddView.vue'),
  },
];
