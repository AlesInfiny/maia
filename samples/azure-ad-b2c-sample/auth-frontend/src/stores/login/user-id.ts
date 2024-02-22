import { defineStore } from 'pinia';
import { AuthResponse } from '@/generated/api-client';
import { authApi } from '@/api-client';

export const useUserStore = defineStore({
  id: 'user-id',
  state: (token: string) => ({
    accessToken: token,
    response: undefined as AuthResponse | undefined,
  }),
  actions: {
    async fetchAuthResponse() {
      const response = await authApi.getConnection(this.accessToken);
      this.response = response.data;
    },
  },
  getters: {
    getLoginStatus(state) {
      return state.response;
    },
  },
});
