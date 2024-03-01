import { defineStore } from 'pinia';
import { string } from 'yup';
import { userApi } from '@/api-client';

export const useUserStore = defineStore({
  id: 'user-id',
  state: () => ({
    userId: string,
  }),
  actions: {
    async fetchUserResponse() {
      const response = await userApi.getUser();
      this.userId = response.data.userId;
    },
  },
  getters: {
    getUserId(state) {
      return state.userId;
    },
  },
});
