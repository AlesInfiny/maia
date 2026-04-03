import { InteractionRequiredAuthError } from '@azure/msal-browser'
import { useAuthenticationStore } from '@/stores/authentication/authentication'
import { useLogger } from '@/composables/use-logger'

const logger = useLogger()

/**
 * 認証関連のサービスを提供します。
 * 認証ストアを利用してサインイン処理や認証状態の判定を行います。
 * @returns 認証サービスオブジェクト
 */
export function authenticationService() {
  const signIn = async () => {
    const authenticationStore = useAuthenticationStore()
    await authenticationStore.signIn()
    authenticationStore.updateAuthenticated()
  }

  const isAuthenticated = (): boolean => {
    const authenticationStore = useAuthenticationStore()
    authenticationStore.updateAuthenticated()
    return authenticationStore.isAuthenticated
  }

  const getToken = async () => {
    const authenticationStore = useAuthenticationStore()
    try {
      const accessToken = await authenticationStore.getTokenSilent()

      if (!accessToken || accessToken === '') {
        throw new InteractionRequiredAuthError('accessToken is null or empty.')
      }
      return accessToken
    } catch (error) {
      if (error instanceof InteractionRequiredAuthError) {
        // ユーザーによる操作が必要な場合にスローされるエラーがスローされた場合、トークン呼び出しポップアップ画面を表示する。
        const accessToken = await authenticationStore.getTokenPopup()
        return accessToken
      }
      logger.error(error)
      throw error
    }
  }

  return {
    signIn,
    isAuthenticated,
    getToken,
  }
}
