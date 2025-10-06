import { defineStore } from 'pinia'
import type { Address } from '@/stores/user/user.model'

/**
 * ユーザー情報を管理する Pinia ストアです。
 *
 * ユーザーの氏名や住所の情報を保持します。
 */
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
    /**
     * ユーザーの住所情報を取得します。
     * @param state - 状態。
     * @returns ユーザーの住所 (`Address`)。
     */
    getAddress(state) {
      return state.address
    },
  },
})
