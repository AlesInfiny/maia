import type { Router, RouteRecordName } from 'vue-router'
import { useAuthenticationStore } from '@/authentication/stores/authentication'
import { displayItemRouteNames, orderingRouteNames } from '@/system-common/router/route-names'
import { authenticationRouteNames } from './authentication-route-names'

export const authenticationGuard = (router: Router) => {
  router.beforeEach((to, from) => {
    const authenticationStore = useAuthenticationStore()

    const orderingPaths: (RouteRecordName | null | undefined)[] = [
      orderingRouteNames.checkout,
      orderingRouteNames.done,
    ]
    if (orderingPaths.includes(to.name) && !from.name) {
      return { name: displayItemRouteNames.displayItem }
    }

    if (to.meta.requiresAuth && !authenticationStore.isAuthenticated) {
      return {
        name: authenticationRouteNames.login,
        query: {
          redirectName: to.name?.toString(),
          redirectParams: JSON.stringify(to.params),
          redirectQuery: JSON.stringify(to.query),
        },
      }
    }

    return true
  })
}
