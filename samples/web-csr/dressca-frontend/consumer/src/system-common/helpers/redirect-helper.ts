/**
 * ログイン後などに戻る画面のパスを扱うヘルパーです。
 * 外部のサイトへ誘導されること（オープンリダイレクト）を防ぐため、
 * アプリケーション内のパスだけを戻り先として受け入れます。
 * @returns 戻り先のパスを扱う関数群
 */
export function redirectHelper() {
  /**
   * 戻り先として指定された値を検証し、アプリケーション内のパスであれば返します。
   * `/` で始まり、ブラウザーの URL パーサーで解釈してもアプリケーションのオリジンを離れない値だけを受け入れます。
   * URL パーサーはタブ・改行・復帰を取り除き、 `\` を `/` とみなすため、先頭の文字だけでは判定しません。
   * @param value クエリパラメーターなどから取得した戻り先の値。
   * @returns アプリケーション内のパス。受け入れられない値の場合は `undefined` 。
   */
  const toSafeRedirectPath = (value: unknown): string | undefined => {
    if (typeof value !== 'string') {
      return undefined
    }
    if (!value.startsWith('/')) {
      return undefined
    }
    const appOrigin = window.location.origin
    if (new URL(value, appOrigin).origin !== appOrigin) {
      return undefined
    }
    return value
  }

  return { toSafeRedirectPath }
}
