import { useAuthenticationStore } from '@/stores/authentication/authentication'
import { useBasketStore } from '@/stores/basket/basket'
import { useCatalogStore } from '@/stores/catalog/catalog'
import { useNotificationStore } from '@/stores/notification/notification'
import { abortAllRequests } from '@/api-client/request-abort-manager'

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

  /**
   * サインアウト処理を実行します。
   * 以下の順序で処理を行います:
   * 1. 処理中の API リクエストを中断
   * 2. 認証状態を false に変更し、セッションストレージを削除
   * 3. 各ストア（basket, catalog）の状態をリセット
   * 4. 通知ストアの状態をリセット（最後に実行）
   */
  const signOut = () => {
    // 1. 処理中の API リクエスト／レスポンスを停止
    abortAllRequests()

    // 2. 認証状態を false に変更し、セッションストレージを削除
    const authenticationStore = useAuthenticationStore()
    authenticationStore.resetState()

    // 3. ストアの中身をリセット
    const basketStore = useBasketStore()
    basketStore.resetState()

    const catalogStore = useCatalogStore()
    catalogStore.resetState()

    // 4. エラーメッセージ通知等のストアの中身を消す（最後に実行）
    const notificationStore = useNotificationStore()
    notificationStore.resetState()
  }

  return {
    signIn,
    isAuthenticated,
    signOut,
  }
}
