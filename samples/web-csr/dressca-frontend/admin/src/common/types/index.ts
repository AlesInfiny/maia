/**
 * 同期値または Promise にラップされた非同期値を表すユーティリティ型です。
 * @template T - 値の型。
 */
export type MaybePromise<T> = T | Promise<T>

/**
 * 引数なしの関数で、戻り値が同期/非同期のどちらでもよい関数を表すユーティリティ型です。
 * @template R - 解決後の戻り値の型。
 * @returns 同期値 `R` もしくは `Promise<R>`。
 * @see MaybePromise
 */
export type MaybeAsyncFunction<R> = () => MaybePromise<R>
