import { defineStore } from 'pinia';
import { UserResponse } from '@/generated/api-client';
import { userApi } from '@/api-client';

export const useUserStore = defineStore({
  id: 'user-id',
  state: (token: string) => ({
    accessToken: token,
    response: undefined as UserResponse | undefined,
  }),
  actions: {
    async fetchAuthResponse() {
      const response = await userApi.getUser(this.accessToken);
      this.response = response.data;
    },
  },
  getters: {
    getLoginStatus(state) {
      return state.response;
    },
  },
});
