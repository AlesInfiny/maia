import { defineStore } from 'pinia';
import { HomeAccountId } from '@/stores/account/account-home-id.model';

export const useAccountHomeIdStore = defineStore('account-home-id', {
  state: (): HomeAccountId => ({
    accountHomeId: '',
  }),
  getters: {
    isEmpty: (state) => state.accountHomeId === '',
    getAccountHomeId(state) {
      state.accountHomeId;
    },
  },
  actions: {
    setAccountHomeId(accountHomeId: string) {
      this.accountHomeId = accountHomeId;
    },
  },
  persist: {
    enabled: true,
    strategies: [{ storage: sessionStorage }],
  },
});
