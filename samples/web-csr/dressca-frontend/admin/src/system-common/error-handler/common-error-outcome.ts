import { useCustomErrorHandler } from './custom-error-handler'

/**
 * ユースケースが個別の結果に分類しないエラーを、共通のエラー処理に通した操作の結果です。
 * 業務例外（ HTTP ステータスコード 400 番台）とシステム例外（ 500 番台）のどちらも `failed` になります。
 * `canceled` は、ログアウトなどの利用者の操作で通信が中断されたことを表します。
 */
export type CommonErrorOutcome = { kind: 'failed' } | { kind: 'canceled' }

/**
 * ユースケースが個別の結果に分類しないエラーを共通のエラー処理に渡し、操作の結果に変換する関数を取得します。
 * 共通のエラー処理が扱えないエラーは、そのまま上位へ送出されます。
 * @returns エラーを受け取り、通信が中断された場合は canceled 、それ以外は failed を返す関数。
 */
export function useCommonErrorOutcome(): (error: unknown) => Promise<CommonErrorOutcome> {
  const handleErrorAsync = useCustomErrorHandler()
  return async (error: unknown) => {
    let outcome: CommonErrorOutcome = { kind: 'canceled' }
    await handleErrorAsync(error, () => {
      outcome = { kind: 'failed' }
    })
    return outcome
  }
}
