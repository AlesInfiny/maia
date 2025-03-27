import { defineStore } from 'pinia';

/**
 * ルーティング情報のストア。
 */
export const useRoutingStore = defineStore('routing', {
  state: () => ({
    redirectFrom: null as null | string,
  }),
  actions: {
    /**
     * リダイレクト元の URL を設定します。
     * @param from リダイレクト元のURL。
     */
    setRedirectFrom(from: string) {
      this.redirectFrom = from;
    },
    /**
     * リダイレクト元の URL を削除します。
     */
    deleteRedirectFrom() {
      this.redirectFrom = null;
    },
  },
  getters: {
    /**
     * リダイレクト元の URL を取得します。
     * @param state 状態。
     * @returns リダイレクト元のURL。
     */
    getRedirectFrom(state) {
      return state.redirectFrom;
    },
  },
});
