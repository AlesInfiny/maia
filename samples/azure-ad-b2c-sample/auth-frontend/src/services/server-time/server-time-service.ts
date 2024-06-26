import { useServerTimeStore } from '@/stores/serverTime/serverTime';

export async function fetchServerTime() {
  const serverTimeStore = useServerTimeStore();
  await serverTimeStore.fetchServerTimeResponse();
}
