import type { Router } from 'vue-router';
import { useAuthenticationStore } from '@/stores/authentication/authentication';
import { useRoutingStore } from '@/stores/routing/routing';

/**
 * ナビゲーションガードです。
 * 画面遷移する際に共通して実行する処理を定義します。
 * たとえば、未認証の場合はログイン画面に遷移させます。
 * @param router vue-router。
 */
export const authenticationGuard = (router: Router) => {
  router.beforeEach((to) => {
    const authenticationStore = useAuthenticationStore();
    const routingStore = useRoutingStore();

    if (to.meta.requiresAuth && !authenticationStore.isAuthenticated) {
      const redirectFromPath: string = to.fullPath;
      routingStore.setRedirectFrom(redirectFromPath);
      return { name: 'authentication/login' };
    }

    return true;
  });
};
