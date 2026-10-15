import { defineComponent } from 'vue'
import { createRouter, createWebHistory, type Router, type RouteRecordRaw } from 'vue-router'
import { router as appRouter } from '@/core/router'

/**
 * 遷移先の画面の代わりに表示する、何も描画しないコンポーネントです。
 */
const StubPage = defineComponent({ name: 'StubPage', render: () => null })

/**
 * ルートの画面を、何も描画しないコンポーネントに置き換えます。
 * @param route ルート。
 * @returns 画面を置き換えたルート。
 */
function toStubRoute(route: RouteRecordRaw): RouteRecordRaw {
  return {
    ...route,
    component: StubPage,
    children: route.children?.map(toStubRoute),
  } as RouteRecordRaw
}

/**
 * アプリケーションのルート表のまま、画面だけを何も描画しないコンポーネントに置き換えたルーターを生成します。
 * 遷移先の画面を遅延読み込みしないため、遷移は画面の読み込み時間に左右されずに完了します。
 * 前のテストの遷移の状態を引き継がないよう、テストごとに生成します。
 * @returns 画面を置き換えたルーター。
 */
export function createStubRouter(): Router {
  return createRouter({
    history: createWebHistory(),
    routes: appRouter.options.routes.map(toStubRoute),
  })
}
