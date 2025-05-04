import type { EventBusKey } from '@vueuse/core';

/**
 * 想定外のエラーの発生を示すイベントのペイロードです。
 * ユーザーへのトースト通知などに使用します。
 */
type UnHandledErrorEventPayload = {
  /** ユーザーへ通知するメッセージ。 */
  message: string;
  /** エラーの ID （オプション） */
  id?: string;
  /** エラーのタイトル（オプション） */
  title?: string;
  /** エラーの詳細（オプション） */
  detail?: string;
  /** HTTPステータスコード（オプション） */
  status?: number;
  /** 通知のタイムアウト（ミリ秒、オプション） */
  timeout?: number;
};

/**
 * 未認証エラーの発生を示すイベントのペイロードです。
 */
type UnAuthorizedErrorEventPayload = {
  /** 詳細情報（例：リダイレクト先の情報やガイダンス） */
  details: string;
};

/**
 * 想定外のエラーの発生を示すイベントのキー値です。
 * @example
 * eventBus.emit(unHandledErrorEventKey, { message: 'エラーが発生しました。' });
 */
export const unHandledErrorEventKey: EventBusKey<UnHandledErrorEventPayload> =
  Symbol('unHandledErrorEventKey');

/**
 * 未認証エラーの発生を示すイベントのキー値です。
 * @example
 * eventBus.emit(unAuthorizedErrorEventKey, { details: 'ログインしてください。' });
 */
export const unAuthorizedErrorEventKey: EventBusKey<UnAuthorizedErrorEventPayload> =
  Symbol('unAuthorizedErrorEventKey');
