import type { Router, RouteRecordName } from 'vue-router'
import { useAuthenticationStore } from '@/stores/authentication/authentication'

export const authenticationGuard = (router: Router) => {
  router.beforeEach((to, from) => {
    const authenticationStore = useAuthenticationStore()

    const orderingPaths: (RouteRecordName | null | undefined)[] = [
      'ordering/checkout',
      'ordering/done',
    ]
    if (orderingPaths.includes(to.name) && !from.name) {
      return { name: 'catalog' }
    }

    if (to.meta.requiresAuth && !authenticationStore.isAuthenticated) {
      return {
        name: 'authentication/login',
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
