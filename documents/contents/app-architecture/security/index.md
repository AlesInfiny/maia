---
title: アプリケーション セキュリティ編
description: アプリケーションセキュリティを 担保するための方針を説明します。
---

# アプリケーションセキュリティ編 {#top}

本章では、 AlesInfiny Maia OSS Edition （以降『AlesInfiny Maia』）のアプリケーションにおいてセキュリティを担保するための対策について説明します。

<!-- textlint-disable ja-technical-writing/sentence-length -->

なお、セキュアな Web アプリケーションの作り方については、 [安全なウェブサイトの作り方 | 情報セキュリティ | IPA 独立行政法人 情報処理推進機構 :material-open-in-new:](https://www.ipa.go.jp/security/vuln/websecurity/about.html){ target=_blank } も併せて参照してください。

<!-- textlint-enable ja-technical-writing/sentence-length -->

!!! danger "注意事項"

    本章は、Web アプリケーションにおいて特に注意すべきセキュリティ対策について説明するものです。すべてのアプリケーションセキュリティ対策を網羅しているわけではありませんし、たとえばネットワークレベルの防御といったインフラレベルでの対策は別途考慮する必要があります。

    **本章に述べる対策のみでシステムのセキュリティ対策が完結するとは考えないでください。**

1. [CSRF （クロスサイト・リクエスト・フォージェリ）](./csrf.md)

    CSRF 攻撃への AlesInfiny Maia での対策を説明します。

1. [XSS （クロスサイト・スクリプティング）](./xss.md)

    XSS 攻撃への AlesInfiny Maia での対策を説明します。

1. [クリックジャッキング](./clickjacking.md)

    クリックジャッキング攻撃への AlesInfiny Maia での対策を説明します。
