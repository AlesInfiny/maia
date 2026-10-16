import { useAuthenticationStore } from '../stores/authentication'
import { useNotificationStore } from '@/business-common/stores/notification'
import { abortAllRequests } from '@/system-common/api-client/request-abort-manager'

export interface Authentication {
  signIn: () => void
  isAuthenticated: () => boolean
  signOut: () => void
}

/**
 * 認証ストアを利用して、サインインやサインアウト、認証状態の判定を行う操作を提供します。
 * @returns 認証の操作。
 */
export function useAuthentication(): Authentication {
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
    return authenticationStore.isAuthenticated
  }

  /**
   * サインアウト処理を実行します。
   * 以下の順序で処理を行います:
   * 1. 処理中の API リクエストを中断
   * 2. 認証状態を false に変更し、セッションストレージを削除
   * 3. 通知ストアの状態をリセット（最後に実行）
   */
  const signOut = () => {
    // 1. 処理中の API リクエスト／レスポンスを停止
    abortAllRequests()

    // 2. 認証状態を false に変更し、セッションストレージを削除
    const authenticationStore = useAuthenticationStore()
    authenticationStore.resetState()

    // 3. エラーメッセージ通知等のストアの中身を消す（最後に実行）
    const notificationStore = useNotificationStore()
    notificationStore.$reset()
  }

  return {
    signIn,
    isAuthenticated,
    signOut,
  }
}
