---
title: Azure AD B2C を 利用した ユーザー認証
description: Azure AD B2C による認証を利用するためのサンプルと、 その使い方を解説します。
---

# Azure Active Directory B2C を利用したユーザー認証 {#top}

## 概要 {#about-this-sample}

Azure Active Directory B2C （以降、 Azure AD B2C ）を利用したユーザー認証の簡単な実装サンプルを提供します。

本サンプルは、クライアントサイドレンダリングアプリケーションにおいて Azure AD B2C を利用する場合のコード例として利用できます。
また、 SPA アプリケーション（ AlesInfiny Maia OSS Edition （以降、 AlesInfiny Maia ）のアーキテクチャに準拠したアプリケーション）に本サンプルのファイルやコードをコピーしてください。
これにより、 SPA アプリケーションに Azure AD B2C を利用したユーザー認証機能を組み込めます。

## 本サンプルを利用するための前提 {#prerequisites}

本サンプルを動作させるためには、以下が必要です。

- Azure サブスクリプション
- サブスクリプション内、またはサブスクリプション内のリソース グループ内で共同作成者以上のロールが割り当てられている Azure アカウント

## 本サンプルを利用する前の準備 {#preparations}

本サンプルを動作させるまでの流れは以下のとおりです。

1. Azure AD B2C テナントを作成する
1. Azure AD B2C テナントを利用するアプリを登録する
1. ユーザーフローを作成する
1. 本サンプルの設定ファイルに各手順で作成した設定内容を記入する
1. 本サンプルを動作させる

具体的な手順は、[サンプルアプリケーション](#download) に付属する README.md を参照してください。

## 本サンプルで利用する OSS {#oss-libraries}

本サンプルでは以下の OSS ライブラリを使用しています。
他の OSS ライブラリについては、 [サンプルアプリケーションをダウンロード](#download) して確認してください。

- フロントエンド
    - [MSAL.js :material-open-in-new:](https://www.npmjs.com/package/@azure/msal-browser){ target=_blank }
- バックエンド
    - [spring-cloud-azure-starter :material-open-in-new:](https://central.sonatype.com/artifact/com.azure.spring/spring-cloud-azure-starter){ target=_blank }
    - [spring-cloud-azure-starter-active-directory-b2c :material-open-in-new:](https://central.sonatype.com/artifact/com.azure.spring/spring-cloud-azure-starter-active-directory-b2c){ target=_blank }
    - [spring-cloud-azure-dependencies :material-open-in-new:](https://central.sonatype.com/artifact/com.azure.spring/spring-cloud-azure-dependencies){ target=_blank }

## 本サンプルを利用する際の検討事項 {#consideration}

- [MSAL.js で提供される秘密情報のキャッシュ保存先](./azure-ad-b2c-consideration.md)

## ダウンロード {#download}

サンプルアプリケーションと詳細な解説は以下からダウンロードできます。

- [サンプルアプリケーションのダウンロード](../../downloads/azure-ad-b2c.zip)
