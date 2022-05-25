import { defineStore } from 'pinia';

export const useAuthenticationStore = defineStore({
  id: 'authentication',
  state: () => ({
    _isAuthenticated: false,
  }),
  actions: {
    async signInAsync() {
      this._isAuthenticated = true;
    },
  },
  getters: {
    isAuthenticated(state) {
      return state._isAuthenticated;
    },
  },
});
