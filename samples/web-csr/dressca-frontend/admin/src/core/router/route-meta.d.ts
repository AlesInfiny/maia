import 'vue-router'

declare module 'vue-router' {
  interface RouteMeta {
    allowAnonymous?: true
  }
}

export {}
