import { useNotificationStore } from '@/stores/notification/notification';

export function showToast(
  message: string,
  id: string = '',
  title: string = '',
  detail: string = '',
  timeout: number = 5000,
) {
  const notificationStore = useNotificationStore();
  notificationStore.setMessage(message, id, title, detail, timeout);
}
