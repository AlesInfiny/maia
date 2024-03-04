import type { Router, RouteRecordName } from 'vue-router';
import { InteractionRequiredAuthError } from '@azure/msal-browser';
import { useAuthenticationStore } from '@/stores/authentication/authentication';
import { useUserStore } from '@/stores/user/user';
import { useRoutingStore } from '@/stores/routing/routing';

export const authenticationGuard = (router: Router) => {
  const authenticationStore = useAuthenticationStore();
  const userStore = useUserStore();
  const routingStore = useRoutingStore();

  router.beforeEach(async (to, from) => {
    const ignoreAuthPaths: (RouteRecordName | null | undefined)[] = [
      'account/login',
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
    await authenticationStore.signIn();
    if (authenticationStore.isAuthenticated) {
      await userStore.fetchUserResponse();
    } else {
      throw new InteractionRequiredAuthError('access denied');
    }
  });
};
