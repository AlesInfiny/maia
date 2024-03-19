import { defineStore } from 'pinia';
import { getServerTimeApi } from '@/api-client';

export const useServerTimeStore = defineStore({
  id: 'time',
  state: () => ({
    serverTime: '' as string,
  }),
  actions: {
    async fetchServerTimeResponse() {
      const api = await getServerTimeApi();
      const response = await api.getServerTime();
      this.serverTime = response.data.serverTime;
    },
  },
  getters: {
    getServerTime(state) {
      return state.serverTime;
    },
  },
});
