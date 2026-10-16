import 'vue-router'

declare module 'vue-router' {
  interface RouteMeta {
    allowAnonymous?: true
    /** `true` の画面に URL を直接指定してアクセスした場合は、既定の画面へ遷移します。 */
    requiresInAppNavigation?: boolean
  }
}

export {}
