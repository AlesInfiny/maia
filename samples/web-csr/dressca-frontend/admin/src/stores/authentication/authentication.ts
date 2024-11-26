import { defineStore } from 'pinia';
import { UsersApi } from '@/api-client';

/**
 * 認証状態のストアです。
 */
export const useAuthenticationStore = defineStore({
  id: 'authentication',
  state: () => ({
    authenticationState: JSON.parse(
      sessionStorage.getItem('isAuthenticated') || 'false',
    ) as boolean,
    userName: JSON.parse(sessionStorage.getItem('userName') || '""'),
    userRole: JSON.parse(sessionStorage.getItem('userRole') || '""'),
  }),
  actions: {
    /**
     * アプリケーションにログインします。
     * セッションストレージに認証状態を保存します。
     */
    async signInAsync() {
      const response = await UsersApi.getLoginUser();
      const { userName, role } = response.data;
      this.userName = userName;
      this.userRole = role;
      this.authenticationState = true;
      sessionStorage.setItem('userName', JSON.stringify(this.userName));
      sessionStorage.setItem('userRole', JSON.stringify(this.userRole));
      sessionStorage.setItem(
        'isAuthenticated',
        JSON.stringify(this.authenticationState),
      );
    },
    /**
     * アプリケーションからログアウトします。
     * セッションストレージから認証状態を削除します。
     */
    async signOutAsync() {
      this.userName = '';
      this.userRole = '';
      this.authenticationState = false;
      sessionStorage.removeItem('isAuthenticated');
      sessionStorage.removeItem('userName');
      sessionStorage.removeItem('userRole');
    },
  },
  getters: {
    /**
     * ユーザーが認証済みかどうかを取得します。
     * @param state 状態。
     * @returns 認証済みかどうかを表す真理値。
     */
    isAuthenticated(state) {
      return state.authenticationState;
    },
  },
});
