import { useRouter } from 'vue-router';
import { showToast } from '@/services/notification/notificationService';
import { useRoutingStore } from '@/stores/routing/routing';
import {
  CustomErrorBase,
  UnauthorizedError,
  NetworkError,
  ServerError,
} from './custom-error';

export function errorHandler(
  error: unknown,
  callback: () => void,
  handlingUnauthorizedError: (() => void) | null = null,
  handlingNetworkError: (() => void) | null = null,
  handlingServerError: (() => void) | null = null,
) {
  // ハンドリングできるエラーの場合はコールバックを実行
  if (error instanceof CustomErrorBase) {
    callback();

    // エラーの種類によって共通処理を行う
    // switch だと instanceof での判定ができないため if 文で判定
    if (error instanceof UnauthorizedError) {
      if (handlingUnauthorizedError) {
        handlingUnauthorizedError();
      } else {
        const routingStore = useRoutingStore();
        const router = useRouter();
        routingStore.setRedirectFrom(router.currentRoute.value.fullPath);
        router.push({ name: 'authentication/login' });
        showToast('ログインしてください。');
      }
    } else if (error instanceof NetworkError) {
      if (handlingNetworkError) {
        handlingNetworkError();
      } else {
        showToast('ネットワークエラーが発生しました。');
      }
    } else if (error instanceof ServerError) {
      if (handlingServerError) {
        handlingServerError();
      } else {
        showToast('サーバーエラーが発生しました。');
      }
    }
  } else {
    // ハンドリングできないエラーの場合は上位にエラーを投げる
    throw error;
  }
}
