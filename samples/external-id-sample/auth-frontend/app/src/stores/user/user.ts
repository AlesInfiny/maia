import { defineStore } from 'pinia'
import { getUsersApi } from '@/api-client'

export const useUserStore = defineStore('user-id', {
  state: () => ({
    userId: '' as string,
  }),
  actions: {
    async fetchUserResponse() {
      const usersApi = await getUsersApi()
      const response = await usersApi.getUser()
      this.userId = response.data.userId
    },
  },
  getters: {
    getUserId(state) {
      return state.userId
    },
  },
})
