import { useCustomErrorHandler } from './custom-error-handler'

/**
 * API が返した問題の詳細のうち、利用者への通知に使う項目です。
 */
export interface ApiProblem {
  /** 例外を識別する ID です。メッセージのキーとして使用します。 */
  exceptionId: string
  /** メッセージに埋め込む値です。 */
  exceptionValues: string[]
  title: string
  detail: string
  status: number
}

/**
 * 想定外のエラーが発生した操作の結果です。
 * `failed` の `problem` は、 API が問題の詳細を返した場合にだけ設定されます。
 * `canceled` は、ログアウトなどの利用者の操作で通信が中断されたことを表します。
 */
export type UnexpectedErrorOutcome = { kind: 'failed'; problem?: ApiProblem } | { kind: 'canceled' }

/**
 * 想定外のエラーを共通のエラー処理に渡し、操作の結果に変換する関数を取得します。
 * 共通のエラー処理が扱えないエラーは、そのまま上位へ送出されます。
 * @returns エラーを受け取り、通信が中断された場合は canceled 、それ以外は failed を返す関数。
 */
export function useUnexpectedErrorOutcome(): (error: unknown) => Promise<UnexpectedErrorOutcome> {
  const handleErrorAsync = useCustomErrorHandler()
  return async (error: unknown) => {
    let outcome: UnexpectedErrorOutcome = { kind: 'canceled' }
    await handleErrorAsync(
      error,
      () => {
        outcome = { kind: 'failed' }
      },
      (httpError) => {
        const response = httpError.response
        if (response?.exceptionId) {
          outcome = {
            kind: 'failed',
            problem: {
              exceptionId: response.exceptionId,
              exceptionValues: response.exceptionValues,
              title: response.title,
              detail: response.detail,
              status: response.status,
            },
          }
        }
      },
    )
    return outcome
  }
}
