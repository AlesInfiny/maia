---
title: CSR 編 - Web API
description: バックエンドアプリケーションのアーキテクチャについて、 層ごとに詳細を説明します。
---

# アプリケーションコア層 {#top}

アプリケーションコア層で実装する機能の詳細を示します。
アプリケーションコア層ではエンティティや値オブジェクト、業務処理を実装します。

## エンティティや値オブジェクト {#entity-and-value-object}

エンティティと値オブジェクトに関しては、 CSR アプリケーションと共通です。
詳しくは [こちら](../../client-side-rendering/backend-application/application-core.md#entity-and-value-object) を参照してください。

## 業務処理 {#business-logic}

その他の業務処理は、 [SSR アーキテクチャ概要 - アプリケーションコア層](../ssr-architecture-overview.md#application-core) で示した通り、各コンポーネントへ役割に応じた処理を実装します。
いずれも依存関係逆転の法則に従い、インフラストラクチャ層への依存と、外部リソースへの直接アクセスを避けるよう注意してください。
