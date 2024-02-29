import { defineStore } from 'pinia';
import { UserResponse } from '@/generated/api-client';
import { userApi } from '@/api-client';

export const useUserStore = defineStore({
  id: 'user-id',
  state: () => ({
    response: undefined as UserResponse | undefined,
  }),
  actions: {
    async fetchUserResponse() {
      const response = await userApi.getUser();
      this.response = response.data;
    },
  },
  getters: {
    getUserId(state) {
      return state.response;
    },
  },
});
