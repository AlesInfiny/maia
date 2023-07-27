import { defineStore } from 'pinia'
import { User } from '@/entities/User'

export const useUserStore = defineStore('user', {
  state: (): User => ({
    accountHomeId: '',
  }),
  getters: {
    isEmpty: (state) => state.accountHomeId === '',
  },
  actions: {
    setAccountHomeId(accountHomeId: string) {
      this.accountHomeId = accountHomeId
    },
    getAccountHomeId() {
      return this.accountHomeId
    },
  },
  // 永続化設定
  persist: {
    enabled: true,
    strategies: [{ storage: sessionStorage }],
  },
})
