import { useAuthenticationStore } from '@/stores/authentication/authentication';
import { useUserStore } from '@/stores/user/user';

export const authenticationService = {
  async signIn() {
    const authenticationStore = useAuthenticationStore();
    const userStore = useUserStore();
    await authenticationStore.signIn();
    if (authenticationStore.isAuthenticated) {
      await userStore.fetchUserResponse();
    }
  },

  async fetchToken() {
    const authenticationStore = useAuthenticationStore();
    await authenticationStore.getToken();
  },
};
