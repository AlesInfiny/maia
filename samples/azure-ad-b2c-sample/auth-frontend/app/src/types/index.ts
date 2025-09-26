export type MaybePromise<T> = T | Promise<T>
export type MaybeAsyncFunction<R> = () => MaybePromise<R>
