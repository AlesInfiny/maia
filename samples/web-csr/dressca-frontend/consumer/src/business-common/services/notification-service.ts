import { useNotificationStore } from '@/business-common/stores/notification'
import { i18n } from '@/system-common/locales/i18n'
import type { ApiProblem } from '@/system-common/error-handler/unexpected-error-outcome'

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

/**
 * 操作に失敗したことをトースト通知で表示します。
 * API が問題の詳細を返した場合は、例外の ID に対応するメッセージと詳細を表示します。
 * それ以外の場合は、代わりのメッセージを表示します。
 * @param problem - API が返した問題の詳細。
 * @param fallbackMessage - 問題の詳細がない場合に表示するメッセージ。
 * @example
 * const outcome = await addToBasket(displayItemId)
 * if (outcome.kind === 'failed') {
 *   showFailureToast(outcome.problem, t('failedToAddItemToCarts'))
 * }
 */
export function showFailureToast(problem: ApiProblem | undefined, fallbackMessage: string) {
  if (!problem) {
    showToast(fallbackMessage)
    return
  }
  const { t } = i18n.global
  showToast(
    t(problem.exceptionId, problem.exceptionValues),
    problem.exceptionId,
    problem.title,
    problem.detail,
    problem.status,
    100000,
  )
}
