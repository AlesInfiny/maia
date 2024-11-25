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

メッセージファイルを作成します。
アーキテクチャ定義では、メッセージ管理に関するファイルは `./src/locales` フォルダーに集約されます。
以下のように、メッセージ本体を格納する JSON ファイルを作成します。

``` json title="messageList_jp.json"
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

### メッセージファイルの読込 {#reading-message-files}

メッセージ本体を格納する JSON ファイルを読み込むために、以下のように記載します。

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

``` ts title="main.ts"
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

### メッセージの利用 {#using-message}

メッセージファイルの読込設定をしたのちに、実際にメッセージを利用します。

#### 結果メッセージの利用 {#using-result-message}

#### 入力値検証用メッセージの利用 {#using-validation-message}

詳細については、[こちら](./input-validation.md) をご覧ください。
