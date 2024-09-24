import { defineStore } from 'pinia';

export const useAuthenticationStore = defineStore({
  id: 'authentication',
  state: () => ({
    authenticationState: false,
  }),
  actions: {
    async signInAsync() {
      this.authenticationState = true;
    },
  },
  getters: {
    isAuthenticated(state) {
      return state.authenticationState;
    },
  },
});
