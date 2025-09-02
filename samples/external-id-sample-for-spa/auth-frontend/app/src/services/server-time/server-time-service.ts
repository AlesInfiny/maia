import { useServerTimeStore } from '@/stores/server-time/server-time'

export async function fetchServerTime() {
  const serverTimeStore = useServerTimeStore()
  await serverTimeStore.fetchServerTimeResponse()
}
