import { defineStore } from 'pinia'
import type { SpecialContent } from '@/stores/special-content/special-content.model'

/**
 * 特別コンテンツ（キャンペーン・セール品など）を管理するストアです。
 *
 * 特定のキャンペーンやセール対象商品に関連する
 * アセット情報（画像やバナーなど）を保持します。
 */
export const useSpecialContentStore = defineStore('special-content', {
  state: () => ({
    contents: [
      {
        campaignCode: 'LTOX48Q',
        assetCode: 'b52dc7f712d94ca5812dd995bf926c04',
      },
      {
        campaignCode: 'EKHQGBB',
        assetCode: '05d38fad5693422c8a27dd5b14070ec8',
      },
      {
        productCode: 14,
        assetCode: '80bc8e167ccb4543b2f9d51913073492',
      },
    ] as SpecialContent[],
  }),
  actions: {},
  /**
   * 特別コンテンツ一覧を取得します。
   * @param state - 状態。
   * @returns 特別コンテンツの配列。
   */
  getters: {
    getSpecialContents(state) {
      return state.contents
    },
  },
})
