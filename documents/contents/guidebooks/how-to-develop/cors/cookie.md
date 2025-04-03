---
title: CORS 環境の構築
description: CORS （オリジン間リソース共有）環境での アプリケーションの構築方法を解説します。
---

# Cookie の設定 {#top}

## Cookie とは {#about-cookie}

[HTTP Cookie の仕様 - MDN Web Docs :material-open-in-new:](https://developer.mozilla.org/ja/docs/Web/HTTP/Guides/Cookies){ target=_blank } より Cookie の定義を以下に引用します。

> Cookie（ウェブ Cookie、ブラウザー Cookie とも呼ぶ）は、サーバーがユーザーのウェブブラウザーに送信する小さなデータです。ブラウザーは Cookie を保存したり、新しい Cookie を作成したり、既存の Cookie を変更したり、後でリクエストされたときに同じサーバーにそれらを送り返したりすることができます。 Cookie により、ウェブアプリケーションは限られた量のデータを格納し、状態についての情報を記憶することができます。

Cookie を使用する際には、 CSRF 攻撃のような脆弱性を悪用した脅威に対しアプリケーション側で対策を施す必要があります。
AlesInfiny Maia OSS Edition での CSRF 攻撃への対策の方針については、[こちら](../../../app-architecture/security/csrf.md) を参照してください。

本章では、上記方針に基づき Cookie を利用する場合の設定方法について解説します。

## バックエンドアプリケーションの設定 {#backend-settings}

本章では、サンプルアプリケーションにて実装している、購入者 ID を Cookie へ付与する機能を設定例として解説します。

まず、 Cookie に付与する属性を `application.properties` に定義します。
以下は、クロスオリジンの場合の方針に基づき `Secure` と `HttpOnly` 、 `SameSite = None` を定義する例です。

??? example "Cookie の属性の設定を定義する application.properties の設定例"

    ```java title="application.properties"
    https://github.com/AlesInfiny/maia/blob/main/samples/web-csr/dressca-backend/web-consumer/src/main/resources/application-prd.properties#L11-L14
    ```

その後、 `#!java @ConfigurationProperties` を利用して `application.properties` に記述した Cookie の設定値を読み込みます。
各フィールドには初期値が設定されていますが、 `application.properties` に記述された設定が優先されます。

??? example "Cookie の設定値を保持する CookieSettings.java"

    ```java title="CookieSettings.java"
    https://github.com/AlesInfiny/maia/blob/main/samples/web-csr/dressca-backend/web-consumer/src/main/java/com/dressca/web/consumer/security/CookieSettings.java
    ```

次に、 Spring Boot で提供されている [ResponseCookie :material-open-in-new:](https://spring.pleiades.io/spring-framework/docs/current/javadoc-api/org/springframework/http/ResponseCookie.html){ target=_blank } を利用して Cookie に設定値を反映させます。

具体的には、以下のように設定した Cookie の情報（購入者 ID ）と設定値を `Set-Cookie` のヘッダーに付与するフィルターを実装します。

??? example "ResponseCookie を利用したサンプルアプリケーションの実装例"

    ```java title="BuyerIdFilter.java" hl_lines="49-54"
    https://github.com/AlesInfiny/maia/blob/main/samples/web-csr/dressca-backend/web-consumer/src/main/java/com/dressca/web/consumer/filter/BuyerIdFilter.java
    ```

最後に、上記で実装したフィルターを Bean 登録します。

??? example "Cookie の設定を設定するフィルタークラスである DresscaWebConfig.java"

    ```java title=""
    https://github.com/AlesInfiny/maia/blob/main/samples/web-csr/dressca-backend/web-consumer/src/main/java/com/dressca/web/consumer/config/DresscaWebConfig.java
    ```

これにより、 Cookie に CSRF 対策を施すことができます。

## フロントエンドアプリケーションの設定 {#frontend-settings}

クロスオリジンの環境の場合、フロントエンドアプリケーションでは、リクエスト発行時に Cookie をヘッダーに含めることを許可する設定が必要となります。
実装例は [CORS 環境の構築](./index.md#http-request-header) を参照してください。
