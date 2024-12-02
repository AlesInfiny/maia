---
title: CSR 編
description: クライアントサイドレンダリングを行う Web アプリケーションの アーキテクチャについて解説します。
---

# 認証・認可方針 {#top}

## 認証の方針 {#authentication-policy}

認証とは、ユーザー名とパスワードのペアや生体情報、認証トークンなどを利用して、システムやネットワークにアクセスしようとしている主体の身元を確認するプロセスのこと指します。
認証が成功すると、システムは主体の真正性を認識し、次のステップに進むことができます。

AlesInfiny Maia OSS Edition では、認証機能の実装を [OpenID Connect :material-open-in-new:](https://openid.net/developers/how-connect-works/){ target=_blank } の標準に則って行います。
OpenID Connect は、 OAuth 2.0 に基づいた認証プロトコルであり、ユーザーの認証情報を安全にやり取りするための標準を提供します。

<!-- textlint-disable ja-technical-writing/sentence-length -->
認証機能の実装には、 [Azure AD B2C :material-open-in-new:](https://learn.microsoft.com/ja-jp/azure/active-directory-b2c/overview){ target=_blank } などのサードパーティ製の認証プロバイダーや、
Spring プロジェクトの１つである [Spring Security :material-open-in-new:](https://spring.io/projects/spring-security){ target=_blank } などが候補になります。
<!-- textlint-enable ja-technical-writing/sentence-length -->

## 認可の方針 {#authorization-policy}

認可とは、システムやネットワークにアクセスしようとしているユーザーが、特定のリソースや機能にアクセスする権利を有しているかどうかを確認し、適切な権限を付与するプロセスのことです。
基本的に認可は認証が成功した後に行われ、システムはセキュリティを維持しつつ、ユーザーに必要なアクセス権を提供します。

AlesInfiny Maia OSS Edition では、認可機能の実装を [OAuth 2.0 :material-open-in-new:](https://oauth.net/2/){ target=_blank } の標準に則って行います。
OAuth 2.0 は、ユーザーが自分の認証情報（ユーザー名やパスワード）を第三者と共有することなく、第三者のアプリケーションやサービスに対して自分のリソースへのアクセスを制御するための認可フレームワークです。

機能の実装には、 Spring プロジェクトの１つである [Spring Security :material-open-in-new:](https://spring.io/projects/spring-security){ target=_blank } などが候補になります。
