import { defineStore } from 'pinia'
import {
  msalInstance,
  loginRequest,
  tokenRequest,
} from '@/services/authentication/authentication-config'
// IIFE 構文が行頭に来る場合、自動セミコロン挿入( ASI )の誤動作防止のため Prettier がセミコロンを挿入します。
// https://prettier.io/docs/options#semicolons
void (async function () {
  await msalInstance.initialize()
})()

export const useAuthenticationStore = defineStore('authentication', {
  state: () => ({
    authenticated: false as boolean,
  }),
  actions: {
    async signIn() {
      const response = await msalInstance.loginPopup(loginRequest)
      msalInstance.setActiveAccount(response.account)
    },
    async signOut() {
      await msalInstance.logoutPopup()
      msalInstance.setActiveAccount(null)
    },
    async getTokenSilent() {
      const account = msalInstance.getActiveAccount()
      tokenRequest.account = account ?? undefined
      const tokenResponse = await msalInstance.acquireTokenSilent(tokenRequest)
      return tokenResponse.accessToken
    },
    async getTokenPopup() {
      const tokenResponse = await msalInstance.acquireTokenPopup(tokenRequest)
      return tokenResponse.accessToken
    },
    updateAuthenticated() {
      const result = msalInstance.getActiveAccount() !== null
      this.authenticated = result
    },
  },
  getters: {
    isAuthenticated(state) {
      return state.authenticated
    },
  },
})
