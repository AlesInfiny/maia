import type { Router, RouteRecordName } from 'vue-router'
import { useAuthenticationStore } from '@/stores/authentication/authentication'
import { authenticationService } from '@/services/authentication/authentication-service'

export const authenticationGuard = (router: Router) => {
  router.beforeEach(async (to, from) => {
    const authenticationStore = useAuthenticationStore()

    if (to.meta.requiresAuth && !authenticationStore.isAuthenticated) {
      try {
        await authenticationService().signIn()
      } catch {
        return false
      }
    }

    const orderingPaths: (RouteRecordName | null | undefined)[] = [
      'ordering/checkout',
      'ordering/done',
    ]
    if (orderingPaths.includes(to.name) && !from.name) {
      return { name: 'catalog' }
    }
    return true
  })
}
