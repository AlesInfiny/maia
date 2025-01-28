import { defineStore } from 'pinia';

export const useRoutingStore = defineStore({
  id: 'routing',
  state: () => ({
    redirectFrom: null as null | string,
  }),
  actions: {
    setRedirectFrom(from: string) {
      this.redirectFrom = from;
    },
    deleteRedirectFrom() {
      this.redirectFrom = null;
    },
  },
  getters: {
    getRedirectFrom(state) {
      return state.redirectFrom;
    },
  },
});
