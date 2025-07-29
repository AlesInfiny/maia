/* eslint @typescript-eslint/no-floating-promises: ["error", { "ignoreIIFE": true }] */
// Safari および Safari on iOS で top-level await が Partial support のため、
// 代替として即時実行関数式 ( IIFE ) で記述を許可するよう ESLint の設定を変更します。
// https://developer.mozilla.org/en-US/docs/Web/JavaScript/Reference/Operators/await#browser_compatibility
import { InteractionRequiredAuthError } from '@azure/msal-browser'
import {
  msalInstance,
  loginRequest,
  tokenRequest,
} from '@/services/authentication/authentication-config'
import { useAuthenticationStore } from '@/stores/authentication/authentication'

// IIFE 構文が行頭に来る場合、自動セミコロン挿入( ASI )の誤動作防止のため Prettier がセミコロンを挿入します。
// https://prettier.io/docs/options#semicolons
;(async function () {
  await msalInstance.initialize()
})()

export const authenticationService = {
  async signInAzureADB2C() {
    const authenticationStore = useAuthenticationStore()
    const response = await msalInstance.loginPopup(loginRequest)
    msalInstance.setActiveAccount(response.account)
    authenticationStore.updateAuthenticated(true)
  },

  isAuthenticated(): boolean {
    const result = msalInstance.getActiveAccount() !== null
    const authenticationStore = useAuthenticationStore()
    authenticationStore.updateAuthenticated(result)
    return result
  },

  async getTokenAzureADB2C() {
    const account = msalInstance.getActiveAccount()

    tokenRequest.account = account ?? undefined
    try {
      const tokenResponse = await msalInstance.acquireTokenSilent(tokenRequest)

      if (!tokenResponse.accessToken || tokenResponse.accessToken === '') {
        throw new InteractionRequiredAuthError('accessToken is null or empty.')
      }
      return tokenResponse.accessToken
    } catch (error) {
      if (error instanceof InteractionRequiredAuthError) {
        // ユーザーによる操作が必要な場合にスローされるエラーがスローされた場合、トークン呼び出しポップアップ画面を表示する。
        const tokenResponse = await msalInstance.acquireTokenPopup(tokenRequest)
        return tokenResponse.accessToken
      }
      // eslint-disable-next-line no-console
      console.error(error)
      throw error
    }
  },
}
