import { useServerTimeStore } from '@/stores/server-time/server-time'

/**
 * サーバーから現在時刻を取得し、サーバータイムストアに反映します。
 * @returns 非同期処理の完了を表す Promise。
 */
export async function fetchServerTime() {
  const serverTimeStore = useServerTimeStore()
  await serverTimeStore.fetchServerTimeResponse()
}
