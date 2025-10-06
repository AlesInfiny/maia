import { useAuthenticationStore } from '@/stores/authentication/authentication'

/**
 * 認証関連のサービスを提供します。
 * 認証ストアを利用してサインイン処理や認証状態の判定を行います。
 * @returns 認証サービスオブジェクト
 */
export function authenticationService() {
  /**
   * サインイン処理を実行します。
   * 認証ストアの `signIn` を呼び出します。
   */
  const signIn = () => {
    const authenticationStore = useAuthenticationStore()
    authenticationStore.signIn()
  }

  /**
   * 現在のユーザーが認証済みかどうかを判定します。
   * @returns 認証されていれば true、されていなければ false
   */

  const isAuthenticated = (): boolean => {
    const authenticationStore = useAuthenticationStore()
    const result = authenticationStore.isAuthenticated
    return result
  }

  return {
    signIn,
    isAuthenticated,
  }
}
