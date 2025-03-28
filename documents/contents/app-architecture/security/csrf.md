---
title: アプリケーション セキュリティ編
description: アプリケーションセキュリティを 担保するための方針を説明します。
---

# CSRF （クロスサイト・リクエスト・フォージュエリ） {#top}

## CSRF とは {#what-is-csrf}

<!-- textlint-disable ja-technical-writing/sentence-length -->

[安全なウェブサイトの作り方 - 1.6 CSRF（クロスサイト・リクエスト・フォージェリ） | 情報セキュリティ | IPA 独立行政法人 情報処理推進機構 :material-open-in-new:](https://www.ipa.go.jp/security/vuln/websecurity/csrf.html){ target=_blank } より CSRF の定義を以下に引用します。

<!-- textlint-enable ja-technical-writing/sentence-length -->

<!-- textlint-disable -->

> ウェブサイトの中には、サービスの提供に際しログイン機能を設けているものがあります。ここで、ログインした利用者からのリクエストについて、その利用者が意図したリクエストであるかどうかを識別する仕組みを持たないウェブサイトは、外部サイトを経由した悪意のあるリクエストを受け入れてしまう場合があります。このようなウェブサイトにログインした利用者は、悪意のある人が用意した罠により、利用者が予期しない処理を実行させられてしまう可能性があります。このような問題を「CSRF（Cross-Site Request Forgeries／クロスサイト・リクエスト・フォージェリ）の脆弱性」と呼び、これを悪用した攻撃を、「CSRF攻撃」と呼びます。

<!-- textlint-enable -->

## AlesInfiny Maia OSS Edition での CSRF 対策 {#measures-against-csrf}

原則として以下の方針をとります。

### プリフライトリクエストによるオリジンヘッダーの検証 {#verification-of-origin-header}

API リクエスト時にオリジンヘッダーを検証することで、異なるオリジンの Web サイト上にある更新系のリクエストをブロックします。
オリジンヘッダーの検証には CORS （オリジン間リソース共有）の機能を利用し、プリフライトリクエスト発生時にリクエストのオリジンヘッダーが許可されたものかどうか確認します。

<!-- textlint-disable ja-technical-writing/sentence-length -->

プリフライトリクエストについては、 [Preflight request (プリフライトリクエスト) - MDN Web Docs 用語集: ウェブ関連用語の定義 | MDN :material-open-in-new:](https://developer.mozilla.org/ja/docs/Glossary/Preflight_request){ target=_blank } を参照してください。

<!-- textlint-enable ja-technical-writing/sentence-length -->

!!! info "CORS とは"

    CORS については、[こちら](../../guidebooks/how-to-develop/cors/index.md#about-cors) を参照してください。

### 単純リクエストにおける更新系処理の実施禁止 {#prohibition-of-update-operations-on-get-requests}

GET リクエストをはじめとする [単純リクエスト :material-open-in-new:](https://developer.mozilla.org/en-US/docs/Web/HTTP/Guides/CORS#simple_requests){ target=_blank } は、 CORS におけるプリフライトリクエストを発生させません。
そのため、単純リクエストに対してはオリジンヘッダーの検証が行われず、単純リクエストに更新系の処理が含まれていると CSRF 攻撃の対象になってしまいます。

よって、単純リクエストに対しては更新系の処理を含まないように API を設計する必要があります。

### Cookie の属性付与 {#granting-cookie}

Cookie を使用する際には、悪意のあるサイトで Cookie にアクセスされたり、 Cookie が異なるドメインに送信されたりすることを防止する必要があります。
Cookie は、既定として `SameSite = Lax` が設定されていますが、 CSRF 対策を実現するためには以下のように属性を付与します。

    - `HttpOnly` を設定し、 JavaScript から Cookie へアクセスできないようにする。
    - アプリケーションがクロスオリジンの構成をとる場合は、別オリジンとの通信を許可するために `SameSite = None` を設定することが強制されるため、 `Secure` を必ず設定して HTTPS プロトコル上の暗号化されたリクエストのみで Cookie を送信するようにする（ [参照 :material-open-in-new:](https://developer.mozilla.org/ja/docs/Web/HTTP/Cookies){ target=_blank } ）。
    - アプリケーションがセイムオリジンの構成をとる場合は、認証情報などのセキュリティを格納する必要がある際には `SameSite = Strict` を設定する。

??? info "その他の CSRF 対策の方法"

    Open Web Application Security Project (OWASP) では、 CSRF 対策として上記以外の方法も提唱しています。
    詳細は [こちら :material-open-in-new:](https://cheatsheetseries.owasp.org/cheatsheets/Cross-Site_Request_Forgery_Prevention_Cheat_Sheet.html#General_Recommendation:_Synchronizer_Token_Pattern){ target=_blank } を参照してください。
    
    以下にいくつかの方法を示します。

    - CSRF トークンの付与

        クライアントがリクエストを発行する際に、サーバー側でそのリクエスト内のトークンの存在と有効性を検証することで、正しいクライアントからのリクエストであることを保証する方法です。
        実装方法については、[公式ページの実装例 :material-open-in-new:](https://spring.pleiades.io/spring-security/reference/servlet/exploits/csrf.html#csrf-integration-javascript-spa){ target=_blank } を参照してください。

    - カスタムヘッダーの付与

        固有のヘッダーを付与することで、全てのリクエストがクロスオリジンのプリフライトリクエストの対象となります。
        これにより、固有のヘッダーが存在しないオリジンからのリクエストはブロックされます。

### CSR アプリケーション {#csr-application}

バックエンドアプリケーションを Spring Boot で構築する場合、各方針に対して以下のような対策を実施します。

- オリジンヘッダーの検証

    Spring Security の CORS の機能を利用してオリジンヘッダーを検証します。
    クロスオリジンの場合、バックエンドアプリケーションにおいてフロントエンドアプリケーションのオリジンを許可しておく必要があります。
    実装方法については、[こちら](../../guidebooks/how-to-develop/cors/index.md) を参照してください。

- Cookie の属性付与

    Spring Boot で提供されている [ResponseCookie :material-open-in-new:](https://spring.pleiades.io/spring-framework/docs/current/javadoc-api/org/springframework/http/ResponseCookie.html){ target=_blank } を利用して、属性を付与します。
    実装方法については、 [こちら](../../guidebooks/how-to-develop/cors/cookie.md) を参照してください。

### SSR アプリケーション {#ssr-application}

（今後追加予定）
