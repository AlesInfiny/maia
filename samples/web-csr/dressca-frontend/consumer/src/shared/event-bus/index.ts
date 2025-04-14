import { useEventBus } from '@/composables/use-event-bus';

type NotificationPayload = {
  message: string;
  id?: string;
  title?: string;
  detail?: string;
  status?: number;
  timeout?: number;
};

/**
 * ユーザーへの通知イベント。
 */
export type NotificationEvent = {
  notification: NotificationPayload;
};

type UnauthorizedPayload = {
  details: string;
};

/**
 * 未認証イベント。
 */
export type UnauthorizedEvent = {
  unAuthorized: UnauthorizedPayload;
};

export const notificationEventBus = useEventBus<NotificationEvent>();
export const unAuthorizedEventBus = useEventBus<UnauthorizedEvent>();
