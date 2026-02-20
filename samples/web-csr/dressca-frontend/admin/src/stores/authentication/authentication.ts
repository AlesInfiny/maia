import { defineStore } from 'pinia'
import { usersApi } from '@/api-client'

/**
 * 認証状態のストアです。
 */
export const useAuthenticationStore = defineStore('authentication', {
  state: () => ({
    authenticationState: JSON.parse(
      sessionStorage.getItem('isAuthenticated') || 'false',
    ) as boolean,
    userName: JSON.parse(sessionStorage.getItem('userName') || '""'),
    userRoles: JSON.parse(sessionStorage.getItem('userRoles') || '""'),
  }),
  actions: {
    /**
     * アプリケーションにログインします。
     * セッションストレージに認証状態を保存します。
     */
    async signInAsync() {
      const response = await usersApi().getLoginUser()
      const { userName, roles } = response.data
      this.userName = userName
      this.userRoles = roles
      this.authenticationState = true
      sessionStorage.setItem('userName', JSON.stringify(this.userName))
      sessionStorage.setItem('userRoles', JSON.stringify(this.userRoles))
      sessionStorage.setItem('isAuthenticated', JSON.stringify(this.authenticationState))
    },
    /**
     * 認証ストアの状態を初期値にリセットします。
     * セッションストレージから認証情報を削除した後、
     * `$reset()` で state を再初期化します。
     * state ファクトリが `sessionStorage` を参照するため、
     * 先に `sessionStorage` をクリアする必要があります。
     */
    resetState() {
      sessionStorage.removeItem('isAuthenticated')
      sessionStorage.removeItem('userName')
      sessionStorage.removeItem('userRoles')
      this.$reset()
    },
  },
  getters: {
    /**
     * ユーザーが認証済みかどうかを取得します。
     * @param state 状態。
     * @returns 認証済みかどうかを表す真理値。
     */
    isAuthenticated(state) {
      return state.authenticationState
    },
    /**
     * ユーザーが特定のロールに属するかどうかを判定する関数を取得します。
     * ストアのゲッターには直接パラメーターを渡すことができないので、
     * 関数を経由してパラメーターを受け取る必要があります。
     * サンプルアプリでは、必ず Admin ロールを持つユーザーとしてログインするようになっています。
     * @param state 状態。
     * @returns ユーザーが特定のロールに属するかどうかを判定する関数。
     */
    isInRole(state) {
      return (role: string) => state.userRoles.includes(role)
    },
  },
})
