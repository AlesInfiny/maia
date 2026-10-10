/**
 * ログイン後などに戻る画面のパスを扱うヘルパーです。
 * 外部のサイトへ誘導されること（オープンリダイレクト）を防ぐため、
 * アプリケーション内のパスだけを戻り先として受け入れます。
 * @returns 戻り先のパスを扱う関数群
 */
export function redirectHelper() {
  /**
   * 戻り先として指定された値を検証し、アプリケーション内のパスであれば返します。
   * `/` で始まり、 `//` や `/\` で始まらない文字列だけをアプリケーション内のパスとみなします。
   * @param value クエリパラメーターなどから取得した戻り先の値。
   * @returns アプリケーション内のパス。受け入れられない値の場合は `undefined` 。
   */
  const toSafeRedirectPath = (value: unknown): string | undefined => {
    if (typeof value !== 'string') {
      return undefined
    }
    if (!value.startsWith('/') || value.startsWith('//') || value.startsWith('/\\')) {
      return undefined
    }
    return value
  }

  return { toSafeRedirectPath }
}
