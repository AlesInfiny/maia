import type { Router, RouteRecordName } from 'vue-router';
import { useAuthenticationStore } from '@/stores/authentication/authentication';
import { useRoutingStore } from '@/stores/routing/routing';

export const authenticationGuard = (router: Router) => {
  router.beforeEach((to, from) => {
    const authenticationStore = useAuthenticationStore();
    const routingStore = useRoutingStore();

    const orderingPaths: (RouteRecordName | null | undefined)[] = [
      'ordering/checkout',
      'ordering/done',
    ];
    if (orderingPaths.includes(to.name) && !from.name) {
      return { name: 'catalog' };
    }

    if (to.meta.requiresAuth && !authenticationStore.isAuthenticated) {
      const redirectFromPath: string = to.name?.toString() ?? '';
      routingStore.setRedirectFrom(redirectFromPath);
      return { name: 'authentication/login' };
    }

    return true;
  });
};
