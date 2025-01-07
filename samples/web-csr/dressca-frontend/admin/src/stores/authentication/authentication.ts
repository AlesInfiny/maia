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
    userRoles: JSON.parse(sessionStorage.getItem('userRoles') || '""'),
  }),
  actions: {
    /**
     * アプリケーションにログインします。
     * セッションストレージに認証状態を保存します。
     */
    async signInAsync() {
      const response = await UsersApi.getLoginUser();
      const { userName, roles } = response.data;
      this.userName = userName;
      this.userRoles = roles;
      this.authenticationState = true;
      sessionStorage.setItem('userName', JSON.stringify(this.userName));
      sessionStorage.setItem('userRoles', JSON.stringify(this.userRoles));
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
      this.userRoles = '';
      this.authenticationState = false;
      sessionStorage.removeItem('isAuthenticated');
      sessionStorage.removeItem('userName');
      sessionStorage.removeItem('userRoles');
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