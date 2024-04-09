import type { Router, RouteRecordName } from 'vue-router';
import { useAuthenticationStore } from '@/stores/authentication/authentication';
import { useRoutingStore } from '@/stores/routing/routing';

export const authenticationGuard = (router: Router) => {
  const authenticationStore = useAuthenticationStore();
  const routingStore = useRoutingStore();

  router.beforeEach((to, from) => {
    const ignoreAuthPaths: (RouteRecordName | null | undefined)[] = [
      'authentication/login',
      'catalog',
      'basket',
      'error',
    ];
    if (ignoreAuthPaths.includes(to.name)) {
      return true;
    }

    const orderingPaths: (RouteRecordName | null | undefined)[] = [
      'ordering/checkout',
      'ordering/done',
    ];
    if (orderingPaths.includes(to.name) && !from.name) {
      return { name: 'catalog' };
    }

    if (authenticationStore.isAuthenticated) {
      return true;
    }

    const redirectFromPath: string = to.name?.toString() ?? '';
    routingStore.setRedirectFrom(redirectFromPath);
    return { name: 'authentication/login' };
  });
};
