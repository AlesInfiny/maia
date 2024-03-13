import { defineStore } from 'pinia';
import { timeApi } from '@/api-client';

export const useTimeStore = defineStore({
  id: 'time',
  state: () => ({
    time: '' as string,
  }),
  actions: {
    async fetchTimeResponse() {
      const response = await timeApi.getTime();
      this.time = response.data.time;
    },
  },
  getters: {
    getTime(state) {
      return state.time;
    },
  },
});
