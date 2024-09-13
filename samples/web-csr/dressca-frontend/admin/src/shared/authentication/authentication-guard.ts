import type { Router, RouteRecordName } from 'vue-router';
import { useAuthenticationStore } from '@/stores/authentication/authentication';
import { useRoutingStore } from '@/stores/routing/routing';

export const authenticationGuard = (router: Router) => {
  const authenticationStore = useAuthenticationStore();
  const routingStore = useRoutingStore();

  router.beforeEach((to) => {
    const ignoreAuthPaths: (RouteRecordName | null | undefined)[] = [
      'authentication/login',
      'error',
    ];
    if (ignoreAuthPaths.includes(to.name)) {
      return true;
    }

    if (authenticationStore.isAuthenticated) {
      return true;
    }

    const redirectFromPath: string = to.fullPath;
    routingStore.setRedirectFrom(redirectFromPath);
    return { name: 'authentication/login' };
  });
};
