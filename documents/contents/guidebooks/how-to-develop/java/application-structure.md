---
title: Java 編
description: バックエンドで動作する Java アプリケーションの 開発手順を解説します。
---

# アプリケーションの全体構造 {#top}

AlesInfiny Maia OSS Edition において、バックエンドで動作する Java アプリケーションは複数の Gradle Groovy DSL プロジェクトを組み合わせたマルチプロジェクトとして構成します。

!!! info "Gradle Groovy DSL プロジェクトとは"

    ビルドツールとして Gradle を使用し、ビルドスクリプトが Groovy で記述されたプロジェクトのことを指します。

本ガイドでは Spring Initializr を用いてマルチプロジェクト構成の Java アプリケーションを作成します。
作成するルートプロジェクトの全体構成は以下の図の通りです。

![ルートプロジェクトの全体構成](../../../images/guidebooks/how-to-develop/java/rootproject-structure-light.png#only-light){ loading=lazy }
![ルートプロジェクトの全体構成](../../../images/guidebooks/how-to-develop/java/rootproject-structure-dark.png#only-dark){ loading=lazy }

各サブプロジェクトはアプリケーションコア層、プレゼンテーション層、インフラストラクチャ層、バッチ層、システム共通機能にそれぞれ対応します。
構成に必要なサブプロジェクトはアプリケーション形態によって異なるため、[各アプリケーション形態のアーキテクチャ](../../../app-architecture/index.md) を参照してください。
