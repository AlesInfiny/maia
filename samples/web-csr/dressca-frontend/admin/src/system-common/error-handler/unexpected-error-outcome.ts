import { useCustomErrorHandler } from './custom-error-handler'

/**
 * 想定外のエラーが発生した操作の結果です。
 * `canceled` は、ログアウトなどの利用者の操作で通信が中断されたことを表します。
 */
export type UnexpectedErrorOutcome = { kind: 'failed' } | { kind: 'canceled' }

/**
 * 想定外のエラーを共通のエラー処理に渡し、操作の結果に変換する関数を取得します。
 * 共通のエラー処理が扱えないエラーは、そのまま上位へ送出されます。
 * @returns エラーを受け取り、通信が中断された場合は canceled 、それ以外は failed を返す関数。
 */
export function useUnexpectedErrorOutcome(): (error: unknown) => Promise<UnexpectedErrorOutcome> {
  const handleErrorAsync = useCustomErrorHandler()
  return async (error: unknown) => {
    let outcome: UnexpectedErrorOutcome = { kind: 'canceled' }
    await handleErrorAsync(error, () => {
      outcome = { kind: 'failed' }
    })
    return outcome
  }
}
