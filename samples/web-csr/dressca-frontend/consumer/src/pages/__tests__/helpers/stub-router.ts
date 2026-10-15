import { defineComponent } from 'vue'
import { createMemoryHistory, createRouter, type Router, type RouteRecordRaw } from 'vue-router'
import { router as appRouter } from '@/core/router'

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
 * @returns 画面を置き換えたルーター。
 */
export function createStubRouter(): Router {
  return createRouter({
    history: createMemoryHistory(),
    routes: appRouter.options.routes.map(toStubRoute),
  })
}
