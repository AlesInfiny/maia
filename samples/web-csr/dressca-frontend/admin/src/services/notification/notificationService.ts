import { useNotificationStore } from '@/stores/notification/notification';

/**
 * トーストを画面に表示します。
 * @param message 表示するメッセージ。
 * @param timeout タイムアウト値（ミリ秒）。デフォルトは5000。
 */
export function showToast(message: string, timeout: number = 5000) {
  const notificationStore = useNotificationStore();
  notificationStore.setMessage(message, timeout);
}
