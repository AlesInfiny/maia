import { useAuthenticationStore } from '@/stores/authentication/authentication'
import { useNotificationStore } from '@/stores/notification/notification'
import { abortAllRequests } from '@/api-client/request-abort-manager'

/**
 * アプリケーションにログインします。
 */
export async function loginAsync() {
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
export function logout() {
  // 1. 処理中の API リクエスト／レスポンスを停止
  abortAllRequests()

  // 2. 認証状態を false に変更し、セッションストレージを削除
  const authenticationStore = useAuthenticationStore()
  authenticationStore.resetState()

  // 3. エラーメッセージ通知等のストアの中身を消す（最後に実行）
  const notificationStore = useNotificationStore()
  notificationStore.$reset()
}
