import { defineStore } from 'pinia';
import { string } from 'yup';
import { useUserStore } from '@/stores/user/user';
import {
  signInAzureADB2C,
  getTokenAzureADB2C,
  AuthenticationResult,
} from '@/shared/authentication/authentication-adb2c';

export const useAuthenticationStore = defineStore({
  id: 'authentication',
  state: () => ({
    homeAccountId: string,
    accessToken: string,
    idToken: string,
    authenticated: false,
  }),
  actions: {
    async signIn() {
      const result = (await signInAzureADB2C()) as AuthenticationResult;
      this.homeAccountId = result.homeAccountId;
      this.idToken = result.idToken;
      this.authenticated = result.isAuthenticated;
    },
    async getToken() {
      const result = (await getTokenAzureADB2C(
        this.homeAccountId,
      )) as AuthenticationResult;
      this.accessToken = result.accessToken;
      this.homeAccountId = result.homeAccountId;
      this.idToken = result.idToken;
      this.authenticated = result.isAuthenticated;
    },
    async getUserId() {
      const loginElem = document.getElementById('login');
      if (loginElem) {
        try {
          const userStore = useUserStore();
          await userStore.fetchUserResponse();
          const userIdRes = userStore.getUserId;
          loginElem.innerText = userIdRes?.userId ?? 'No UserID';
        } catch (err) {
          loginElem.innerText = 'error occurred';
          throw err;
        }
      }
    },
  },
  getters: {
    getHomeAccountId(state) {
      return state.homeAccountId;
    },
    getAccessToken(state) {
      return state.accessToken;
    },
    getIdToken(state) {
      return state.idToken;
    },
    isAuthenticated(state) {
      return state.authenticated;
    },
  },
});
