import { useNotificationStore } from '@/business-common/stores/notification'

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
 * @returns トースト通知の操作。
 */
export function useNotification(): ToastNotification {
  const notificationStore = useNotificationStore()

  const showToast: ToastNotification['showToast'] = (message, timeout = 5000) => {
    notificationStore.setMessage(message, timeout)
  }

  return { showToast }
}
