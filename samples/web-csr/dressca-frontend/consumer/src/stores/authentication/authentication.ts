import { defineStore } from 'pinia';

export const useAuthenticationStore = defineStore('authentication', {
  state: () => ({
    authenticationState: JSON.parse(
      sessionStorage.getItem('isAuthenticated') || 'false',
    ) as boolean,
  }),
  actions: {
    async signInAsync() {
      this.authenticationState = true;
      sessionStorage.setItem(
        'isAuthenticated',
        JSON.stringify(this.authenticationState),
      );
    },
  },
  getters: {
    isAuthenticated(state) {
      return state.authenticationState;
    },
  },
});
