import { useAuthenticationStore } from '../../authorization/stores/authentication'
import { useNotificationStore } from '@/business-common/stores/notification'
import { abortAllRequests } from '@/system-common/api-client/request-abort-manager'

/**
 * アプリケーションにログインします。
 */
async function loginAsync() {
  const authenticationStore = useAuthenticationStore()
  await authenticationStore.signInAsync()
}

/**
 * アプリケーションからログアウトします。
 * 以下の順序で処理を行います:
 * 1. 処理中の API リクエストを中断
 * 2. 認証状態を false に変更し、セッションストレージを削除
 * 3. 通知ストアの状態をリセット（最後に実行）
 */
function logout() {
  // 1. 処理中の API リクエスト／レスポンスを停止
  abortAllRequests()

  // 2. 認証状態を false に変更し、セッションストレージを削除
  const authenticationStore = useAuthenticationStore()
  authenticationStore.resetState()

  // 3. エラーメッセージ通知等のストアの中身を消す（最後に実行）
  const notificationStore = useNotificationStore()
  notificationStore.$reset()
}

/**
 * 認証の操作です。
 */
export interface Authentication {
  /** アプリケーションにログインします。 */
  loginAsync: () => Promise<void>
  /** アプリケーションからログアウトします。 */
  logout: () => void
}

/**
 * 認証ストアを利用して、ログインとログアウトを行う操作を提供します。
 * @returns 認証の操作。
 */
export function useAuthentication(): Authentication {
  return { loginAsync, logout }
}
