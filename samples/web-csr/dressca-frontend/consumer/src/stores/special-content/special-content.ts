import { defineStore } from 'pinia';
import type { SpecialContent } from '@/stores/special-content/special-content.model';

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
  getters: {
    getSpecialContents(state) {
      return state.contents;
    },
  },
});
