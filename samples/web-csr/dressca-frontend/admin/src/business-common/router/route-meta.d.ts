/*
 * ルート表（ routes.ts ）で宣言するルートの属性の型です。
 */
import 'vue-router'

declare module 'vue-router' {
  interface RouteMeta {
    /**
     * 認証が必要な画面かどうかです。
     * 指定しない場合は、認証が必要な画面として扱います。
     */
    requiresAuth?: boolean
  }
}

export {}
