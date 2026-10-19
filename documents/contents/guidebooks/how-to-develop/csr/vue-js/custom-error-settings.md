---
title: Vue.js 開発手順 （CSR 編）
description: Vue.js を用いた フロントエンドアプリケーションの 開発手順を説明します。
---

# カスタムエラーの設定 {#top}

下記の手順では、 TypeScript 標準の `Error` 型の限界をカバーするために、業務例外を表現するための独自例外クラスを実装します。
加えて、 HTTP 通信に由来する業務例外について、外部ライブラリである Axios に直接依存せず、独自例外クラスに正規化してから扱うための設定例を示します。

## カスタムエラーの必要性 {#why-custom-errors}

TypeScript には標準の `Error` 型が用意されていますが、一般に下記のような欠点があります。

1. 主要なプロパティ( `name` 、 `message`　、`stack` )が、エラーの情報を型レベルで構造化して表現するには不十分です。たとえば HTTP ステータスコードやアプリケーション固有のエラーコードなどの情報を表現できません。

1. `Error` 型に対して `JSON.stringify()` を実行した際に、 `name`, `message`, `stack` がシリアライズされないため、ログ出力に必要な情報が欠落します。

これらの欠点をカバーするため、 `Error` 型を拡張して、業務例外を表す独自例外クラス（カスタムエラー）を実装する必要があります。

他方で、[OpenAPI 仕様書からのクライアントコード生成](./create-api-client-code.md) で導入した Axios には、 HTTP 通信で発生するエラーを表現するために、 `Error` 型を拡張した `AxiosError` 型が用意されています。
`AxiosError` 型には、リクエスト設定 ( `config` )、レスポンス ( `response` )、エラーコード ( `code` ) などが含まれ、 `toJSON()` メソッドでこれらを JSON 形式に変換できます。
`AxiosError` の一般的な構造については、 Axios 公式ドキュメントの [Handling Errors :material-open-in-new:](https://axios.rest/pages/advanced/error-handling){ target=_blank } を参照ください。

エラーハンドリングに `AxiosError` 型をそのまま用いることも考えられますが、業務例外の構造が直接外部ライブラリに依存しないクリーンなコードを保つため、 `AxiosError` 型をカスタムエラーへ変換します。

業務例外は、発生した箇所と扱う箇所で、次のように役割を分けます。

- サービスは、業務例外をカスタムエラーとして送出します。
- ユースケースコンポーザブルは、 `try-catch` と `instanceof` でカスタムエラーを捕捉し、結果（ Outcome ）で返します。
- 画面は結果を受け取り、通知の文言と遷移先を決めます。画面ではカスタムエラーを判定しません。

例外を送出する側は、例外の構造を型で表現でき、例外が発生しうる処理を呼び出すたびに戻り値を検査する必要がありません。
一方、画面は結果の種類（ `kind` ）で分岐するので、どの結果を扱うべきかを型検査で確認できます。
ユースケースコンポーザブル、結果、画面の関係は、 [アーキテクチャ解説 - フォルダー構成](../../../../app-architecture/client-side-rendering/frontend-application/index.md#project-structure) を参照してください。

## カスタムエラーを定義する {#define-custom-error}

標準の `Error` 型を拡張して、カスタムエラーの基底クラスとなる抽象クラスを実装します。
これを継承するカスタムエラーの具象クラスには、ログ出力に必要な情報を取り出すために、 `toJSON()` メソッドの実装を強制します。

```typescript title="カスタムエラーの基底クラス"
export abstract class CustomErrorBase extends Error {
  cause?: Error | null

  constructor(message: string, cause?: Error) {
    super(message)
    // ラップ前のエラーを cause として保持
    this.cause = cause
  }

  abstract toJSON(): Record<string, unknown>
}
```

### HTTPError クラスの定義 {#define-http-error}

`CustomError` クラスを拡張して、 HTTP 通信に起因する業務エラーを定義します。

[集約例外ハンドラーの設定](../java/sub-project-settings/exception-handling.md) で設定した通り、バックエンドから返却されるエラーレスポンスには、 `ProblemDetails` に基づくレスポンスボディーを含みます。
よって、 HTTP 通信によって発生しうるエラーを表す `HttpError` クラスには、`AxiosError.response.data` を経由して `ProblemDetails` の情報を格納します。

[例外処理方針 - API 通信のエラーレスポンス](../../../../app-architecture/client-side-rendering/global-function/exception-handling.md#error-response) で述べているように、拡張メンバー `exceptionId` と `exceptionValues` を追加で定義しています。

加えて、これらの情報を構造化して出力できるように、 `toJSON()` メソッドを実装します。

```typescript title="基底クラスを継承した HttpError クラスの例"
export class HttpError extends CustomErrorBase {
  response?: ProblemDetails | null

  constructor(message: string, cause?: Error & { response?: { data?: ProblemDetails } }) {
    super(message, cause)
    this.response = cause?.response?.data ?? null
    this.name = 'HttpError'
  }

  toJSON() {
    return {
      timestamp: new Date().toISOString(),
      name: this.name,
      message: this.message,
      stack: this.stack,
      response: this.response ?? null,
      cause: this.cause ?? null,
    }
  }
}
```

```typescript title="ProblemDetails の型定義"
export interface ProblemDetails {
  detail: string
  exceptionId: string
  exceptionValues: string[]
  instance: string
  status: number
  title: string
  type: string
}
```

### HTTPError の具象クラスの定義 {#define-concrete-http-error}

HttpError を継承して、 発生しうるエラーを表現する具象クラスを定義します。
バックエンドアプリケーションから連携された OpenAPI 定義書に従って、 必要な具象クラスを定義してください。
下記の例は、 HTTP ステータスコード 404 Not Found に対応する具象クラス `NotFoundError` の実装例です。

```typescript title="HttpError を継承した具象クラスの例"
export class NotFoundError extends HttpError {
  constructor(message: string, cause?: Error) {
    super(message, cause)
    this.name = 'NotFoundError'
  }
}
```

## AxiosError をカスタムエラーに変換する {#convert-axios-error}

Axios の [Interceptors :material-open-in-new:](https://axios.rest/pages/advanced/interceptors){ target=_blank } を用いて、 `AxiosError` をカスタムエラーに変換します。 Axios の Interceptors は、 HTTP リクエストおよびレスポンスが処理される前に、任意のハンドリング処理を挟み込んで実行できる機能です。

この機能を用いて、 HTTP ステータスコードに対応した適切なカスタムエラーへ変換する処理を一箇所に集約して実装します。
HTTP ステータスコードは `AxiosError` の `response.status` へ格納されています。

`axiosInstance.interceptors.response` の実装例を下記に示します。
たとえば下記の例では、`response.status` が 404 である `AxiosError` を、`NotFoundError` に変換して返却しています。

```typescript title="サンプルアプリケーションの Interceptors の実装例" hl_lines="14-16"
axiosInstance.interceptors.response.use(
  (response) => response,
  (error) => {
    if (axios.isAxiosError(error)) {
      if (!error.response) {
        return Promise.reject(new NetworkError(error.message, error))
      }
      if (error.response.status === Number(HttpStatusCode.InternalServerError)) {
        return Promise.reject(new ServerError(error.message, error))
      }
      if (error.response.status === Number(HttpStatusCode.Unauthorized)) {
        return Promise.reject(new UnauthorizedError(error.message, error))
      }
      if (error.response.status === Number(HttpStatusCode.NotFound)) {
        return Promise.reject(new NotFoundError(error.message, error))
      }
      if (error.response.status === Number(HttpStatusCode.Conflict)) {
        return Promise.reject(new ConflictError(error.message, error))
      }
      return Promise.reject(new HttpError(error.message, error))
    }
    return Promise.reject(new UnknownError('Unknown Error', error))
  },
)
```

## 業務例外が発生しうる処理の呼び出し {#calling-a-process-where-exceptions-are-expected}

業務例外が発生する可能性のある処理は、ユースケースコンポーザブルから呼び出し、 `try-catch` で囲みます。
例外を `catch` したら、 `instanceof` を使用して、どのカスタムエラーに該当するか検出し、検出したエラーに応じた結果を返します。

### 共通の失敗の結果 {#common-error-outcome}

画面ごとに扱いを変える業務例外だけを、個別の結果にします。
それ以外のエラーは、共通の失敗の結果（ `CommonErrorOutcome` ）にまとめます。
共通の失敗の結果は、 `failed` と `canceled` の 2 種類です。
`canceled` は、ログアウトなどの利用者の操作で通信が中断されたことを表し、画面は何もしません。

`useCommonErrorOutcome` が提供する関数は、受け取ったエラーをカスタムエラーハンドラー（ `useCustomErrorHandler` ）に渡し、共通の失敗の結果に変換します。
カスタムエラーハンドラーは、ログの出力や、認証エラーとサーバーエラーの通知など、エラーの種類ごとの共通の処理を行います。
カスタムエラーでないエラーは、そのまま上位へ送出され、グローバルエラーハンドラーで処理されます。
以下は consumer の実装です。
API が問題の詳細を返した場合は、 `failed` の `problem` に格納し、画面の通知に使用します。
admin の `CommonErrorOutcome` は `problem` を持たず、 `{ kind: 'failed' } | { kind: 'canceled' }` です。

```typescript title="サンプルアプリケーションの common-error-outcome.ts"
--8<-- "samples/web-csr/dressca-frontend/consumer/src/system-common/error-handler/common-error-outcome.ts"
```

### ユースケースコンポーザブルの実装例 {#usecase-composable-example}

下記は、 admin のカタログアイテムを更新する操作で `NotFoundError` と `ConflictError` を個別の結果にし、それ以外のエラーを共通の失敗の結果にする例です。

```typescript title="ユースケースコンポーザブルの実装例（抜粋）"
export type UpdateOutcome =
  | { kind: 'updated' }
  | { kind: 'conflict' }
  | { kind: 'notFound' }
  | { kind: 'failed' }
  | { kind: 'canceled' }

export function useCatalogItemEditor(itemId: MaybeRefOrGetter<string>): CatalogItemEditor {
  const handleCommonError = useCommonErrorOutcome()
  // (中略)
  async function update(): Promise<UpdateOutcome> {
    try {
      await updateCatalogItem(/* (中略) */)
    } catch (error) {
      if (error instanceof NotFoundError) {
        return { kind: 'notFound' }
      }
      if (error instanceof ConflictError) {
        return { kind: 'conflict' }
      }
      return handleCommonError(error)
    }
    return { kind: 'updated' }
  }
  // (中略)
}
```

### 画面の実装例 {#page-example}

画面は、ユースケースコンポーザブルの操作の結果を `kind` で分岐し、通知の文言と遷移先を決めます。
下記は、更新の対象が見つからなかった場合に、ユーザーへトースト通知し、一覧画面へ遷移する例です。

```typescript title="画面の実装例（抜粋）"
const updateItemAsync = async () => {
  const outcome = await update()
  switch (outcome.kind) {
    case 'updated':
      showUpdateNotice.value = true
      break
    case 'notFound':
      showToast('更新対象のカタログアイテムが見つかりませんでした。')
      router.push({ name: routeNames.catalogItems })
      break
    case 'conflict':
      showToast('カタログアイテムの更新が競合しました。もう一度更新してください。')
      break
    case 'failed':
      showToast('カタログアイテムの更新に失敗しました。')
      break
    case 'canceled':
      break
  }
}
```
