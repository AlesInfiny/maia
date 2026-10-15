import { useNotificationStore } from '@/business-common/stores/notification'
import { i18n } from '@/system-common/locales/i18n'
import type { ApiProblem } from '@/system-common/error-handler/common-error-outcome'

/**
 * トースト通知の操作です。
 */
export interface ToastNotification {
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
  showToast: (
    message: string,
    id?: string,
    title?: string,
    detail?: string,
    status?: number,
    timeout?: number,
  ) => void
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
  showFailureToast: (problem: ApiProblem | undefined, fallbackMessage: string) => void
}

/**
 * 通知ストアを通じてトースト通知を表示する操作を提供します。
 * 通知ストアを setup の中で一度だけ取り出すため、コンポーネントの setup の中で呼び出します。
 * @returns トースト通知の操作。
 */
export function useNotification(): ToastNotification {
  const notificationStore = useNotificationStore()

  const showToast: ToastNotification['showToast'] = (
    message,
    id = '',
    title = '',
    detail = '',
    status = 0,
    timeout = 5000,
  ) => {
    notificationStore.setMessage(message, id, title, detail, status, timeout)
  }

  const showFailureToast: ToastNotification['showFailureToast'] = (problem, fallbackMessage) => {
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

  return { showToast, showFailureToast }
}
