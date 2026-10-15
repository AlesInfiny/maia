import { useNotificationStore } from '@/business-common/stores/notification'

/**
 * トースト通知の操作です。
 */
export interface ToastNotification {
  /**
   * トーストを画面に表示します。
   * @param message 表示するメッセージ。
   * @param timeout タイムアウト値（ミリ秒）。デフォルトは5000。
   */
  showToast: (message: string, timeout?: number) => void
}

/**
 * 通知ストアを通じてトースト通知を表示する操作を提供します。
 * 通知ストアを setup の中で一度だけ取り出すため、コンポーネントの setup の中で呼び出します。
 * @returns トースト通知の操作。
 */
export function useNotification(): ToastNotification {
  const notificationStore = useNotificationStore()

  const showToast: ToastNotification['showToast'] = (message, timeout = 5000) => {
    notificationStore.setMessage(message, timeout)
  }

  return { showToast }
}
