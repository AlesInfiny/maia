/**
 * 処理中の API リクエストを一括キャンセルするための AbortController です。
 * ログアウト時などに呼び出し中のリクエストを停止するために使用します。
 */
let abortController = new AbortController()

/**
 * 現在有効な AbortSignal を返します。
 * API リクエストの signal に設定してください。
 * @returns 現在の AbortController に紐づく AbortSignal。
 */
function getRequestAbortSignal(): AbortSignal {
  return abortController.signal
}

/**
 * 処理中のすべての API リクエストを中断し、AbortController を再生成します。
 * ログアウト時に呼び出してください。
 */
function abortAllRequests() {
  abortController.abort()
  abortController = new AbortController()
}

export { getRequestAbortSignal, abortAllRequests }
