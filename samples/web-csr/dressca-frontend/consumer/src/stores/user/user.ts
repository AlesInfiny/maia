import { defineStore } from 'pinia'
import type { Address } from '@/stores/user/user.model'

export const useUserStore = defineStore('user', {
  state: () => ({
    address: {
      fullName: '国会　太郎',
      postalCode: '100-8924',
      todofuken: '東京都',
      shikuchoson: '千代田区',
      azanaAndOthers: '永田町1-10-1',
    } as Address,
  }),
  getters: {
    getAddress(state) {
      return state.address
    },
  },
})
