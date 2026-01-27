import { useServerTimeStore } from '@/stores/server-time/server-time'

/**
 * サーバー時刻を取得します。
 * @returns Promise<void>
 */
export async function fetchServerTime() {
  const serverTimeStore = useServerTimeStore()
  await serverTimeStore.fetchServerTimeResponse()
}
