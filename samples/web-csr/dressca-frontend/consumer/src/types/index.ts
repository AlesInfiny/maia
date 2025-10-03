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

/**
 * 引数が 1 つ(unary)で、戻り値が同期/非同期のいずれでもよい関数を表すユーティリティ型です。
 * @template T - 引数（`source`）の型。
 * @template R - 解決後の戻り値の型。
 * @param source - 関数の引数。
 * @returns `R` または `Promise<R>`。
 */
export type MaybeAsyncUnaryFunction<T, R> = (source: T) => MaybePromise<R>
