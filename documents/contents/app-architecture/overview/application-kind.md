---
title: 概要編
description: AlesInfiny Maia OSS Edition の アプリケーションアーキテクチャ概要を解説します。
---

# 構築できるアプリケーション形態 {#top}

AlesInfiny Maia OSS Edition を利用することで構築できるアプリケーションの概要を、アプリケーション形態ごとに説明します。

## Web アプリケーション ( クライアントサイドレンダリング ) {#client-side-rendering}

HTML をクライアント側 JavaScript でレンダリングする方式の Web アプリケーションです。
画面初期表示時にはコンパイル済みの静的ファイルをダウンロードして、 JavaScript で動的に画面をレンダリングします。
業務データの取得、更新などの処理を行う際は、 Web API 経由でサーバー側の業務ロジックを呼び出します。

![クライアントサイドレンダリング](../../images/app-architecture/overview/client-side-rendering-light.png#only-light){ loading=lazy }
![クライアントサイドレンダリング](../../images/app-architecture/overview/client-side-rendering-dark.png#only-dark){ loading=lazy }

## Web アプリケーション ( サーバーサイドサイドレンダリング ) {#server-side-rendering}

サーバーサイドで構築した HTML をクライアント側のブラウザーでレンダリングする方式の Web アプリケーションです。

![サーバーサイドレンダリング](../../images/app-architecture/overview/server-side-rendering-light.png#only-light){ loading=lazy }
![サーバーサイドレンダリング](../../images/app-architecture/overview/server-side-rendering-dark.png#only-dark){ loading=lazy }

## バッチアプリケーション {#batch}

バッチ処理を実装した Java アプリケーションです。
スケジューラーや手動で起動し、 Web アプリケーションとは独立して業務処理を実行します。

![バッチアプリケーション](../../images/app-architecture/overview/batch-application-light.png#only-light){ loading=lazy }
![バッチアプリケーション](../../images/app-architecture/overview/batch-application-dark.png#only-dark){ loading=lazy }
