---
title: CORS 環境の構築
description: CORS （オリジン間リソース共有）環境での アプリケーションの構築方法を解説します。
---

# CORS 環境の構築 {#top}

## CORS （オリジン間リソース共有）とは {#about-cors}

CORS とは、いくつかの HTTP ヘッダーを使用することで、同一オリジンポリシーの制限を回避する仕組みです。

??? note "オリジンとは"

    オリジンとは、 URL のスキーム（プロトコル）、ホスト（ドメイン）、ポート番号の部分を指します（ [Origin (オリジン) - MDN Web Docs 用語集: ウェブ関連用語の定義 | MDN :material-open-in-new:](https://developer.mozilla.org/ja/docs/Glossary/Origin){ target=_blank } ）。

    - `http://localhost` と `https://localhost` はスキームが異なるので異なるオリジン
    - `https://www.example.com` と `https://www2.example.com` はホスト部分が異なるので異なるオリジン
    - `https://localhost:4431` と `https://localhost:4432` はポート番号が異なるので異なるオリジン

??? note "同一オリジンポリシーとは"

    ブラウザーは原則として「同一オリジンポリシー」で動作します（ [同一オリジンポリシー - ウェブセキュリティ | MDN :material-open-in-new:](https://developer.mozilla.org/ja/docs/Web/Security/Same-origin_policy){ target=_blank }   ）。

    > 同一オリジンポリシーは重要なセキュリティの仕組みであり、あるオリジンによって読み込まれた文書やスクリプトが、他のオリジンにあるリソースにアクセスできる方法を制限するものです。

    <!-- textlint-disable ja-technical-writing/sentence-length -->

    つまり、 `https://aaa.example.com` から取得したリソース（ HTML 文書や JavaScript ）から、 `https://bbb.example.net` のリソース（ Web API や HTML 文書）には原則としてアクセスできません。

    <!-- textlint-enable ja-technical-writing/sentence-length -->

本章で解説する CORS 環境とは、 CSR アプリケーションにおいて、フロントエンドアプリケーションとバックエンドアプリケーションの配置されるサーバーのオリジンが異なる環境を意味します。 CORS 環境で CSR アプリケーションを構築する場合、いくつかの考慮や追加の実装が必要です。

CORS の仕組みの詳細は「 [オリジン間リソース共有 (CORS) - HTTP | MDN :material-open-in-new:](https://developer.mozilla.org/ja/docs/Web/HTTP/CORS){ target=_blank } 」を参照してください。

## バックエンドアプリケーション（ Spring Boot ） {#backend}

Spring Boot アプリケーションでは、 [`SecurityFilterChain` :material-open-in-new:](https://spring.pleiades.io/spring-security/site/docs/current/api/org/springframework/security/web/SecurityFilterChain.html){ target=_blank } で CORS に関するポリシーを設定します。
AlesInfiny Maia OSS Edition （以降『 AlesInfiny Maia 』）では、許可するオリジンの一覧をアプリケーション設定ファイル `application.properties` から取得します。

### 許可するオリジンの追加 {#application-properties}

本番環境で許可するオリジンの一覧を Web 層のアプリケーション設定ファイル `application-prd.properties` に記述します。
許可するオリジンが複数ある場合には、 `,` で区切ります。

```properties title="application-prd.properties"
cors.allowed.origins=https://frontend.example.com,https://sub.frontend.example.com
```

なお、開発時にのみ使用する設定は、 `application-dev.properties` に記述します。

```properties title="application-dev.properties"
cors.allowed.origins=https://dev.frontend.example.com
```

### 許可するオリジンの読み込み {#reading-allowed-origins}

まず、 CORS による設定を有効化するために、[`#!java @EnableWebSecurity` :material-open-in-new:](https://spring.pleiades.io/spring-security/site/docs/current/api/org/springframework/security/config/annotation/web/configuration/EnableWebSecurity.html){ target=_blank } を記述します。
また、`application.properties` で許可したオリジンを読み込むために、 [`#!java @Value` :material-open-in-new:](https://spring.pleiades.io/spring-framework/reference/core/beans/annotation-config/value-annotations.html){ target=_blank } を利用します。
なお、`#!java @Value` 内で記述したプロパティ名は `application.properties` で設定した名称と一致させる必要があります。

```java title="WebSecurityConfig.java"
@Configuration(proxyBeanMethods = false)
@EnableWebSecurity
public class WebSecurityConfig {

  @Value("${cors.allowed.origins:}")
  private String[] allowedOrigins;

  ...
}
```

!!! note "プロパティ名の後に `:` を記述して空の配列を設定"
    `#!java @Value` 内に記述したプロパティ名が application.properties に記述されていない場合、エラーが発生します。
    AlesInfiny Maia のサンプルアプリケーションでは、プロパティ名の後に `:` を記述することで初期値に空の配列を設定し、エラーを回避しています。

### CORS ポリシーの設定 {#configure-cors-policy}

Spring Boot では、 CORS に関する設定を `SecurityFilterChain` を利用して実装します。

以下は、サンプルアプリケーションにおける CORS の設定を実現する `WebSecurityConfig.java` の実装例です。

??? example "`SecurityFilterChain` の CORS 設定例"

    ```java title="WebSecurityConfig.java"　hl_lines="30-31 37-46"
    https://github.com/AlesInfiny/maia/blob/main/samples/web-csr/dressca-backend/web-consumer/src/main/java/com/dressca/web/consumer/security/WebSecurityConfig.java
    ```

### CORS のポリシー設定についての詳細 {#detail-of-cors-policy}

上のコード例「 `WebSecurityConfig.java` 」における CORS の設定に関するメソッドについて説明します。

- `setAllowCredentials` メソッド

    許可したオリジンのクライアントに Cookie 等の認証情報を送信することを許可します。
    アプリケーションで Cookie や認証を使用する場合、このメソッドの呼び出しが必要です。

- `setAllowedOrigins` メソッド

    CORS でリソースへのアクセスを許可するオリジンを設定します。
    AlesInfiny Maia ではアプリケーション設定ファイルから値を取得して引数に渡します。

- `setAllowedMethods` メソッド

    <!-- textlint-disable ja-technical-writing/sentence-length -->

    許可したオリジンのクライアントが使用可能な HTTP メソッドを設定します。アプリケーションで許可する HTTP メソッド名を指定してください。なお、 CORS 環境の場合プリフライトリクエストが使用する `OPTIONS` は必須です。詳細は [Preflight request (プリフライトリクエスト) - MDN Web Docs 用語集: ウェブ関連用語の定義 | MDN :material-open-in-new:](https://developer.mozilla.org/ja/docs/Glossary/Preflight_request){ target=_blank } を参照してください。

    <!-- textlint-enable ja-technical-writing/sentence-length -->

- `setAllowedHeaders` メソッド

    許可したオリジンのクライアントに許可する HTTP リクエストヘッダーを設定します。

- `addExposedHeader` メソッド

    許可したオリジンのクライアントに対して公開する必要がある HTTP レスポンスヘッダーを設定します。
    アプリケーションで許可する HTTP レスポンスヘッダー名を指定してください。
    `addExposedHeader` メソッドで設定していないレスポンスヘッダーはクライアントに公開されません。

!!! danger "Cookie を使用する場合の注意事項"

    CORS 環境においてアプリケーションで Cookie を使用する場合、 `SameSite` 属性に `None` を明示的に指定する必要があります。設定しない場合、別オリジンへ Cookie を送信できません。
    なお、 Cookie の仕様上 `SameSite` 属性に `None` を設定する場合は必ず `Secure` 属性も設定する必要があります（ [HTTP Cookie の使用 - HTTP | MDN :material-open-in-new:](https://developer.mozilla.org/ja/docs/Web/HTTP/Cookies){ target=_blank } ）。

    > Cookie に SameSite=None が付いた場合は、 Secure 属性も指定することになりました（安全なコンテキストが必要になりました）。

    実装方法については、[Cookie の設定](./cookie.md) を参照してください。

## フロントエンドアプリケーション（ Vue.js ） {#frontend}

### Web API 呼び出し時の HTTP ヘッダーの設定 {#http-request-header}

AlesInfiny Maia では Web API 呼び出しの共通処理用に `./src/api-client/index.ts` という設定ファイルを作成する（ [参照](../vue-js/create-api-client-code.md#set-client-code) ）ので、ここで HTTP ヘッダーを設定します。

```typescript title="index.ts" hl_lines="11"
import axios from 'axios'
import * as apiClient from '@/generated/api-client'

// （中略）

/** axios の共通の設定があればここに定義します。 */
const axiosInstance = axios.create({
  headers: {
    'Content-Type': 'application/json',
  },
  withCredentials: true,
})

const exampleApi = new apiClient.ExampleApi(createConfig(), '', axiosInstance)

export { exampleApi }
```

<!-- textlint-disable @textlint-ja/no-synonyms -->

`withCredentials: true`

<!-- textlint-enable @textlint-ja/no-synonyms -->

:   CORS 環境でのリクエストが Cookie 、認証ヘッダー、 TLS クライアント証明書などの資格情報を使用して行われるべきかを示します。既定値は `false` なので、 `true` を明示的に設定します。
