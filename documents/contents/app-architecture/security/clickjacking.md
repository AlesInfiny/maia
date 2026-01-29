---
title: アプリケーション セキュリティ編
description: アプリケーションセキュリティを 担保するための方針を説明します。
---

<!-- cspell:ignore SAMEORIGIN -->

# クリックジャッキング {#top}

## クリックジャッキングとは {#what-is-clickjacking}

<!-- textlint-disable ja-technical-writing/sentence-length -->

[安全なウェブサイトの作り方 - 1.9 クリックジャッキング | 情報セキュリティ | IPA 独立行政法人 情報処理推進機構 :material-open-in-new:](https://www.ipa.go.jp/security/vuln/websecurity/clickjacking.html){ target=_blank } より CSRF の定義を以下に引用します。

<!-- textlint-enable ja-technical-writing/sentence-length -->

<!-- textlint-disable -->

> ウェブサイトの中には、ログイン機能を設け、ログインしている利用者のみが使用可能な機能を提供しているものがあります。該当する機能がマウス操作のみで使用可能な場合、細工された外部サイトを閲覧し操作することにより、利用者が誤操作し、意図しない機能を実行させられる可能性があります。このような問題を「クリックジャッキングの脆弱性」と呼び、問題を悪用した攻撃を、「クリックジャッキング攻撃」と呼びます。

<!-- textlint-enable -->

## AlesInfiny Maia OSS Edition でのクリックジャッキング対策 {#measures-against-clickjacking}

### `X-Frame-Options` {#x-frame-options}

HTTP レスポンスヘッダーに対して [`X-Frame-Options` ヘッダーフィールド :material-open-in-new:](https://www.ietf.org/rfc/rfc7034.txt){ target=_blank } を出力します。
これにより、他ドメインのサイトからの `<frame>` 要素や `<iframe>` 要素、 `<embed>` 要素、 `<object>` 要素による読み込みを制限します。

以下に示す `X-Frame-Options` で指定する設定値により、制限の範囲が異なります。

| 設定値                | 表示できる範囲                                       |
| --------------------- | ---------------------------------------------------- |
| DENY                  | すべてのドメインからのフレーム内の表示を禁止する     |
| SAMEORIGIN            | 同一オリジンからのフレーム内の表示のみを許可する     |
| ALLOW-FROM （非推奨） | 指定したオリジンからのフレーム内の表示のみを許可する |

なお、`ALLOW-FROM` の設定値は主要なブラウザーでは互換性がないため、非推奨です。
ブラウザーの互換性については、[こちら :material-open-in-new:](https://developer.mozilla.org/ja/docs/Web/HTTP/Reference/Headers/X-Frame-Options#%E3%83%96%E3%83%A9%E3%82%A6%E3%82%B6%E3%83%BC%E3%81%AE%E4%BA%92%E6%8F%9B%E6%80%A7){ target=_blank } を参照してください。

特定のオリジンのみ許可したい場合は、 `Content-Security-Policy` ヘッダーの `frame-ancestors` ディレクティブを使用します。

### `Content-Security-Policy : frame-ancestors` {#content-security-policy}

<!-- textlint-disable ja-technical-writing/sentence-length -->

HTTP レスポンスヘッダーに対して [`Content-Security-Policy` ヘッダーフィールドの `frame-ancestors` ディレクティブ :material-open-in-new:](https://developer.mozilla.org/ja/docs/Web/HTTP/Reference/Headers/Content-Security-Policy/frame-ancestors){ target=_blank } を出力します。

<!-- textlint-enable ja-technical-writing/sentence-length -->

`frame-ancestors` は、どのオリジンから当該コンテンツを `<frame>` 要素や `<iframe>` 要素、 `<embed>` 要素、 `<object>` 要素で読み込めるかを指定するためのディレクティブです。

`X-Frame-Options` と比較して、複数オリジンの指定やワイルドカード指定が可能であり、主要なブラウザーで広くサポートされています。
ブラウザーの互換性については、[こちら :material-open-in-new:](https://developer.mozilla.org/ja/docs/Web/HTTP/Reference/Headers/Content-Security-Policy/frame-ancestors#%E3%83%96%E3%83%A9%E3%82%A6%E3%82%B6%E3%83%BC%E3%81%AE%E4%BA%92%E6%8F%9B%E6%80%A7){ target=_blank } を参照してください。

以下に示す `frame-ancestors` の指定内容により、フレーム内表示の許可範囲が異なります。

| 設定値                                                         | 表示できる範囲                                         |
| -------------------------------------------------------------- | ------------------------------------------------------ |
| `frame-ancestors 'none';`                                      | すべてのオリジンからのフレーム内の表示を禁止する       |
| `frame-ancestors 'self';`                                      | 同一オリジンからのフレーム内の表示のみを許可する       |
| `frame-ancestors https://example.com;`                         | 指定したオリジンからのフレーム内の表示のみを許可する   |
| `frame-ancestors https://example.com https://sub.example.com;` | 複数の指定したオリジンからのフレーム内の表示を許可する |

### ブラウザーにおけるヘッダーの優先順位 {#priority}

`X-Frame-Options` と `frame-ancestors` の両方がヘッダーに存在する場合、最新のブラウザーは `frame-ancestors` を優先し、`X-Frame-Options` を無視します。
`frame-ancestors` がサポートされていない場合は、 `X-Frame-Options` にフォールバックします。

### アプリケーションの設定 {#application-settings}

<!-- textlint-disable ja-technical-writing/sentence-length -->

AlesInfiny Maia OSS Edition では、 [Spring Security :material-open-in-new:](https://spring.io/projects/spring-security){ target=_blank } を利用して `X-Frame-Options` および `frame-ancestors` を設定します。

<!-- textlint-enable ja-technical-writing/sentence-length -->

本設定では、後方互換性のために `X-Frame-Options` を設定しつつ、実際のフレーム制御は `frame-ancestors` によって行う構成としています。

```java "WebSecurityConfig.java" hl_lines="41-43"
https://github.com/AlesInfiny/maia/blob/main/samples/web-csr/dressca-backend/web-consumer/src/main/java/com/dressca/web/consumer/security/WebSecurityConfig.java
```

| 設定                      | 内容                                                                                     |
| ------------------------- | ---------------------------------------------------------------------------------------- |
| `frameOptions().deny()`   | `X-Frame-Options: DENY` を出力し、旧来のブラウザーに対する最低限のフレーム制御を行います |
| `frame-ancestors 'none';` | 同一オリジンからのフレーム内表示のみを許可します                                         |
