import { defineStore } from 'pinia';
import { HomeAccountId } from '@/stores/account/home-account-id.model';

export const useHomeAccountIdStore = defineStore('home-account-id', {
  state: (): HomeAccountId => ({
    homeAccountId: '',
  }),
  getters: {
    isEmpty: (state) => state.homeAccountId === '',
    getHomeAccountId(state) {
      state.homeAccountId;
    },
  },
  actions: {
    setHomeAccountId(homeAccountId: string) {
      this.homeAccountId = homeAccountId;
    },
  },
  persist: {
    enabled: true,
    strategies: [{ storage: sessionStorage }],
  },
});
