import { defineStore } from 'pinia'

/**
 * 認証状態のストアです。
 */
export const useAuthenticationStore = defineStore('authentication', {
  state: () => ({
    authenticationState: JSON.parse(
      sessionStorage.getItem('isAuthenticated') || 'false',
    ) as boolean,
  }),
  actions: {
    /**
     * アプリケーションにサインインします。
     * ストアの `authenticationState` を `true` に変更し、
     * その状態をセッションストレージに保存します。
     */
    signIn() {
      this.authenticationState = true
      sessionStorage.setItem('isAuthenticated', JSON.stringify(this.authenticationState))
    },
  },
  getters: {
    /**
     * 現在の認証状態を取得します。
     * @param state 状態。
     * @returns 認証済みであれば `true`、それ以外は `false` 。
     */
    isAuthenticated(state) {
      return state.authenticationState
    },
  },
})
