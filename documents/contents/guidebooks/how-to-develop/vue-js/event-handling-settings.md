---
title: Vue.js 開発手順
description: Vue.js を用いた フロントエンドアプリケーションの 開発手順を説明します。
---

# イベントハンドリングの設定 {#top}

## イベント駆動の必要性 {#why-event-driven}

## VueUseの導入 {#install-vue-use}

イベントバス構造を表現するライブラリとして、
[useEventBus](https://vueuse.org/core/useEventBus/#useeventbus){ target=_blank }を利用します。
下記のコマンドを実行して、依存関係に VueUse のコアパッケージを追加します。

```shell
npm install @vueuse/core
```

package.json の Dependencies に @vueuse/core が追加されたことを確認してください。
