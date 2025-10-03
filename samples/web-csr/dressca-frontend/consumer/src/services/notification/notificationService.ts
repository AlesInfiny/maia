import { useNotificationStore } from '@/stores/notification/notification'

/**
 * トースト通知を表示します。
 * 通知ストアを通じてメッセージを登録し、UI に表示させます。
 * @param message - 表示するメインメッセージ（必須）
 * @param id - 通知を識別する任意の ID（デフォルト: 空文字）
 * @param title - 通知のタイトル（デフォルト: 空文字）
 * @param detail - 通知の詳細メッセージ（デフォルト: 空文字）
 * @param status - 通知のステータスコード（例: HTTP ステータスなど、デフォルト: 0）
 * @param timeout - 通知を自動で閉じるまでの時間（ミリ秒、デフォルト: 5000）
 * @example
 * // 基本的なトースト通知
 * showToast('保存に成功しました')
 * @example
 * // 詳細つきの通知
 * showToast(
 *   '保存に失敗しました',
 *   'ERR001',
 *   '保存エラー',
 *   'サーバーに接続できませんでした',
 *   500,
 *   8000
 * )
 */
export function showToast(
  message: string,
  id: string = '',
  title: string = '',
  detail: string = '',
  status: number = 0,
  timeout: number = 5000,
) {
  const notificationStore = useNotificationStore()
  notificationStore.setMessage(message, id, title, detail, status, timeout)
}
