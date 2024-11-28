---
title: Vue.js 開発手順
description: Vue.js を用いた クライアントサイドアプリケーションの 開発手順を説明します。
---

# メッセージ管理機能の設定 {#top}

フロントエンドのメッセージ管理方針に関するアーキテクチャについては、[こちら](../../../app-architecture/client-side-rendering/global-function/message-management-policy.md) をご確認ください。
本アーキテクチャに基づき、メッセージ管理機能のライブラリは Vue I18n を使用します。

## 必要なパッケージのインストール {#install-packages}

ターミナルを開き、以下のコマンドを実行します。

```terminal
npm install vue-i18n
```

## 設定方法 {#settings}

本設定で利用するフォルダーの構成は以下の通りです。

``` terminal linenums="0"
<workspace-name>
  └ src/ ------------------------------------------- アプリケーションのソースコードが配置されるフォルダー
    ├ locales/ ------------------------------------- メッセージ管理を行うコードが配置されるフォルダー
    │ ├ en/ ---------------------------------------- 英語メッセージの管理を行うフォルダー
    │ │ ├ messageList_en.json ---------------------- 処理の成功や失敗などの結果メッセージを格納する JSON ファイル（英語）
    │ │ └ validationTextList_en.json --------------- 入力値検証用のメッセージを格納する JSON ファイル（英語）
    │ ├ ja/ ---------------------------------------- 日本語メッセージの管理を行うフォルダー
    │ │ ├ messageList_ja.json ---------------------- 処理の成功や失敗などの結果メッセージを格納する JSON ファイル（日本語）
    │ │ └ validationTextList_ja.json --------------- 入力値検証用のメッセージを格納する JSON ファイル（日本語）
    │ └ i18n.ts ------------------------------------ メッセージ管理の設定に関するコード
    ├ shared/ -------------------------------------- 共通処理を行うコードが配置されるフォルダー
    │ └ helpers/ ----------------------------------- 特定のタスクを実行するための再利用可能なコードが配置されるフォルダー
    │   └ languageHelper.ts ------------------------ 言語設定を行うコード
    └ main.ts -------------------------------------- 各ライブラリ等を読み込むためのコード
```

### メッセージファイルの作成 {#creating-message-files}

アーキテクチャ定義では、メッセージに関するファイルは `./src/locales` フォルダーに集約されます。
以下のように、メッセージ本体を格納する JSON ファイルを作成します。

``` json title="messageList_jp.json の例"
{
  "errorOccurred": "エラーが発生しました。",
  "businessError": "業務エラーが発生しました。",
  "loginRequiredError": "ログインしてください。",
  ...
}
```

前述のフォルダー構成の通り、メッセージ管理方針に従って JSON ファイルは以下の 2 つで分割します。

- messageList.json

    処理の成功や失敗を示す処理メッセージを格納する

- validationTextList.json

    入力値検証用のメッセージを格納する

JSON ファイルでメッセージを管理する際は、メッセージコードとメッセージ本体を key-value で管理します。

### メッセージファイルの読込 {#reading-message-files}

メッセージ本体を格納する JSON ファイルを読み込むために、以下のように `i18n.ts` を実装します。

``` ts title="i18n.ts"
import { createI18n } from 'vue-i18n';
import messageListEN from '@/locales/en/messageList_en.json';
import messageListJP from '@/locales/ja/messageList_jp.json';
import validationTextListJP from '@/locales/ja/validationTextList_jp.json';
import validationTextListEN from '@/locales/en/validationTextList_en.json';
import { languageHelper } from '@/shared/helpers/languageHelper';

const langPackage = {
  ja: {
    ...messageListJP,
    ...validationTextListJP,
  },
  en: {
    ...messageListEN,
    ...validationTextListEN,
  },
};

const i18n = createI18n({
  legacy: false,
  locale: languageHelper().toConfigureLocale(),
  messages: langPackage,
});

export { i18n };
```

メッセージ管理機能を導入するための `createI18n` の引数の役割は以下の通りです。

- legacy

    <!-- textlint-disable ja-technical-writing/sentence-length -->
    createI18n のインスタンスとして、 [Legacy API :material-open-in-new:](https://vue-i18n.intlify.dev/api/legacy.html){ target=_blank } と [Composition API :material-open-in-new:](https://vue-i18n.intlify.dev/api/composition.html){ target=_blank } のどちらを利用するか選択します。
    本実装では、 Composition API を利用するため、 legacy を false に設定します。
    <!-- textlint-enable ja-technical-writing/sentence-length -->

- locale

    使用する言語を指定します。

    本実装では、以下のようなブラウザーの言語設定を読み込む `languageHelper.ts` を作成し、インポートして利用します。

    ``` ts title="languageHelper.ts"
    export function languageHelper() {
      const toConfigureLocale = () => {
        const browserLanguage = window.navigator.language;
        let language = 'ja';
        if (browserLanguage !== 'ja' && browserLanguage !== 'en') {
          language = 'en';
        } else {
          language = browserLanguage;
        }
        return language;
      };
      return {
        toConfigureLocale,
      };
    }
    ```

- messages

    locale の言語設定に基づき、利用するメッセージを指定します。

`i18n.ts` の設定をアプリケーションに反映させるため、 `main.ts` に以下のように実装します。

``` ts title="main.ts" hl_lines="8 16"
import { createApp } from 'vue';
import { createPinia } from 'pinia';
import { authenticationGuard } from '@/shared/authentication/authentication-guard';
import { globalErrorHandler } from '@/shared/error-handler/global-error-handler';
import { createCustomErrorHandler } from '@/shared/error-handler/custom-error-handler';
import App from './App.vue';
import { router } from './router';
import { i18n } from './locales/i18n';

import '@/assets/base.css';

const app = createApp(App);

app.use(createPinia());
app.use(router);
app.use(i18n);
app.use(globalErrorHandler);
app.use(createCustomErrorHandler());

authenticationGuard(router);

app.mount('#app');
```

### メッセージの取得 {#using-message}

読み込んだメッセージを取得するためには、 `i18n.ts` を各ファイルでインポートして利用します。
実装例は以下の通りです。

``` ts title="メッセージ利用例"
<script setup lang="ts">
import { i18n } from '@/locales/i18n';

const { t } = i18n.global;

// TypeScript 上で利用する場合
showToast(t('errorOccurred'));

</script>

// テンプレート構文上で利用する場合
<template>
  <span class="text-lg font-medium text-green-500">
    {{ t('errorOccurred') }}
  </span>
</template>
```

メッセージ関数 `t()` を利用してメッセージを取得します。
メッセージ関数 `t()` の引数には、 JSON ファイルのメッセージコードを指定してメッセージ本体を呼び出します。
