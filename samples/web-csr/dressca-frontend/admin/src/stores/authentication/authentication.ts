import { defineStore } from 'pinia';
import { UsersApi } from '@/api-client';

export const useAuthenticationStore = defineStore({
  id: 'authentication',
  state: () => ({
    authenticationState: false,
    userName: '',
    userRole: '',
  }),
  actions: {
    async signInAsync() {
      const response = await UsersApi.getLoginUser();
      const { userName, role } = response.data;
      this.userName = userName;
      this.userRole = role;
      this.authenticationState = true;
    },
    async signOutAsync() {
      this.userName = '';
      this.userRole = '';
      this.authenticationState = false;
    },
  },
  getters: {
    isAuthenticated(state) {
      return state.authenticationState;
    },
  },
});
