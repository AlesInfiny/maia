import { defineStore } from 'pinia';
import { useUserStore } from '@/stores/user/user';

export const useAuthenticationStore = defineStore({
  id: 'authentication',
  state: () => ({
    homeAccountId: '',
    _isAuthenticated: false,
  }),
  actions: {
    setHomeAccountId(homeAccountId: string) {
      this.homeAccountId = homeAccountId;
    },
    async signInAsync() {
      this._isAuthenticated = true;
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
    isAuthenticated(state) {
      return state._isAuthenticated;
    },
  },
});
