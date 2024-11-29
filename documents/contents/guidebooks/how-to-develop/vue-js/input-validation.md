---
title: Vue.js 開発手順
description: Vue.js を用いた クライアントサイドアプリケーションの 開発手順を説明します。
---

# 入力値検証 {#top}

フロントエンドのアーキテクチャに基づき、入力値検証には VeeValidate と yup を使用します。
また、入力値検証失敗時のメッセージを管理するために、 Vue I18n を使用します。
メッセージ管理機能の実装方法の詳細に関しては、[こちら](./message-management.md) を確認してください。

## 必要なパッケージのインストール {#install-packages}

ターミナルを開き、以下のコマンドを実行します。

```terminal
npm install vee-validate yup vue-i18n
```

## メッセージの定義 {#definition-messages}

入力値検証メッセージを定義するため、`./src/locales` フォルダーに JSON ファイルを作成し、以下のように記述します。
メッセージを多言語対応する場合には、それぞれの言語の JSON ファイルを作成し、各言語のメッセージをフォルダーで分割して管理します。

```json title="validationTextList_jp.json"
{
  "email": "メールアドレスの形式で入力してください",
  "required": "値を入力してください"
}
```

## 入力値検証時の設定 {#settings-validation}

各言語設定に基づいた、入力値検証メッセージを読み込みます。
[アーキテクチャ定義](../../../app-architecture/client-side-rendering/frontend-application/index.md#project-structure) では設定ファイルは `./src/config` フォルダーに集約されるため、ファイル `./src/config/yup.config.ts` を作成し、以下のように記述します。

```typescript title="yup.config.ts"
import { i18n } from '@/locales/i18n';
import { setLocale } from 'yup';

export function configureYup(): void {
  const { t } = i18n.global;
  setLocale({
    mixed: {
      required: t('required'),
    },
    string: {
      email: t('email'),
    },
  });
}
```

作成したファイルを読み込むため、 入力値検証する Vue ファイルのスクリプト構文に以下を記述します。

```typescript title="main.ts"
import { configureYup } from '@/config/yup.config';

// yup設定の有効化
configureYup();

// フォーム固有のバリデーション定義
const formSchema = yup.object({
  email: ValidationItems().email.required(),
  password: yup.string().required(),
});
```

## 入力値検証の実行 {#input-validation}

どのように入力値検証をコーディングするかは、[公式ドキュメント :material-open-in-new:](https://vee-validate.logaretm.com/v4/guide/components/validation/){ target=_blank }を参照してください。
