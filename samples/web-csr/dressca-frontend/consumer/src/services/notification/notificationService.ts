import { useNotificationStore } from '@/stores/notification/notification';

export function showToast(message: string, timeout: number = 5000) {
  const notificationStore = useNotificationStore();
  notificationStore.setMessage(message, timeout);
}
