---
title: 概要編
description: AlesInfiny Maia OSS Edition の アプリケーションアーキテクチャ概要を解説します。
---

# Java アプリケーションのアーキテクチャ選定基準 {#top}

一般に、システム化対象の業務は複数の業務領域で構成されます。
業務領域には、「中核の業務領域」とその他の領域があります。

![業務領域のカテゴリー](../../images/app-architecture/overview/business-domain-categories-dark.png#only-dark){ loading=lazy }
![業務領域のカテゴリー](../../images/app-architecture/overview/business-domain-categories-light.png#only-light){ loading=lazy }

まずは業務領域とカテゴリーを組み合わせて確認しながら、システムを設計するための境界を定め、分割します。
境界ごとに分けられた領域をコンテキストと呼びます。

コンテキストは、業務領域単位となることもあれば、複数の業務領域をまたいで 1 つとすることもあります。
システムの都合や、業務領域同士の関係の深さによって、適切なコンテキストを設計します。
定めたコンテキストごとに、「トランザクションスクリプト」「ドメインモデル」のいずれかの設計・実装手法を選択します。

つまり、 1 つのシステムの中に、複数のアプリケーションアーキテクチャが共存しうるということです。

アプリケーションアーキテクチャおよび実現方針の選定フローは以下のとおりです。

![アプリケーションアーキテクチャの選定フロー](../../images/app-architecture/overview/application-architecture-selection-flow-dark.png#only-dark){ loading=lazy }
![アプリケーションアーキテクチャの選定フロー](../../images/app-architecture/overview/application-architecture-selection-flow-light.png#only-light){ loading=lazy }

中核の業務領域は、複雑な業務ロジックを持ちます。 そのため、複雑な業務ロジックの設計に適したドメインモデルを採用します。

中核ではない業務領域は、比較的単純な業務ロジックを持つことが多いため、トランザクションスクリプトを採用します。
