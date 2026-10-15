import { useNotificationStore } from '@/business-common/stores/notification'

/**
 * トーストを画面に表示します。
 * @param message 表示するメッセージ。
 * @param timeout タイムアウト値（ミリ秒）。デフォルトは5000。
 */
function showToast(message: string, timeout: number = 5000) {
  const notificationStore = useNotificationStore()
  notificationStore.setMessage(message, timeout)
}

/**
 * トースト通知の操作です。
 */
export interface ToastNotification {
  showToast: typeof showToast
}

/**
 * 通知ストアを通じてトースト通知を表示する操作を提供します。
 * @returns トースト通知の操作。
 */
export function useNotification(): ToastNotification {
  return { showToast }
}
