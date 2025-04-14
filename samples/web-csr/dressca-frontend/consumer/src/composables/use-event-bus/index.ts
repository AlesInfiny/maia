/**
 * 任意のイベント名と対応するペイロード型を定義するためのマップです。
 */
export type EventMap = Record<string, any>;

/**
 * 特定のペイロード型を受け取るリスナー関数です。
 */
export type Listener<Payload> = (payload: Payload) => void;

/**
 * イベントバスを提供するコンポーザブルです。
 *
 * @template Events イベント名とペイロードの型を定義したマップ型。
 * @returns イベントの購読・発行・解除を行うための関数。
 */
export function useEventBus<Events extends EventMap>() {
  /**
   * イベント名ごとに登録されたリスナー関数の集合を管理するマップです。
   */
  const listenersMap: {
    [K in keyof Events]?: Set<Listener<Events[K]>>;
  } = {};

  /**
   * 指定したイベントにリスナーを登録します。
   *
   * @param event 登録するイベント名。
   * @param listener イベント発火時に呼び出されるリスナー関数。
   */
  function on<K extends keyof Events>(
    event: K,
    listener: Listener<Events[K]>,
  ): void {
    if (!listenersMap[event]) {
      listenersMap[event] = new Set();
    }
    listenersMap[event]!.add(listener);
  }

  /**
   * 指定したイベントを発火し、登録された全てのリスナー関数をペイロードを引数にして実行します。
   *
   * @param event 発火するイベント名。
   * @param payload リスナーに渡すペイロード。
   */
  function emit<K extends keyof Events>(event: K, payload: Events[K]): void {
    listenersMap[event]?.forEach((listener) => listener(payload));
  }

  /**
   * 指定したイベントからリスナーを解除します。
   * イベントに紐づくリスナー関数が0になった場合、
   * マップそのものも削除します。
   * @param event 解除するイベント名
   * @param listener 解除するリスナー関数
   */
  function off<K extends keyof Events>(
    event: K,
    listener: Listener<Events[K]>,
  ): void {
    listenersMap[event]?.delete(listener);
    if (listenersMap[event]?.size === 0) {
      delete listenersMap[event];
    }
  }

  return {
    on,
    emit,
    off,
  };
}
