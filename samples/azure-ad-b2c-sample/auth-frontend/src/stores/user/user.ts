import { defineStore } from 'pinia';
import { getUserApi } from '@/api-client';

export const useUserStore = defineStore({
  id: 'user-id',
  state: () => ({
    userId: '' as string,
  }),
  actions: {
    async fetchUserResponse() {
      const userApi = await getUserApi();
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
