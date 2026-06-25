---
title: Vue.js 開発手順 （CSR 編）
description: Vue.js を用いた フロントエンドアプリケーションの 開発手順を説明します。
---

# 入力値検証 {#top}

フロントエンドのアーキテクチャに基づき、入力値検証には VeeValidate と zod を使用します。
また、入力値検証失敗時のメッセージを管理するために、 Vue I18n を使用します。
メッセージ管理機能の実装方法の詳細に関しては、[こちら](./message-management.md) を確認してください。

## 必要なパッケージのインストール {#install-packages}

ターミナルを開き、対象プロジェクトのワークスペースフォルダーで以下のコマンドを実行します。

```shell
npm install vee-validate zod vue-i18n
```

## メッセージの定義 {#definition-messages}

入力値検証失敗時のメッセージを定義するため、`./src/locales` フォルダーに JSON ファイルを作成し、以下のように記述します。
メッセージを多言語対応する場合には、それぞれの言語の JSON ファイルを作成し、各言語のメッセージをフォルダーで分割して管理します。

```json title="validationTextList_jp.json"
https://github.com/AlesInfiny/maia/blob/main/samples/web-csr/dressca-frontend/consumer/src/locales/ja/validationTextList_ja.json
```

## 入力値検証時の設定 {#settings-validation}

各言語設定に基づいた、入力値検証メッセージを読み込みます。
共通スキーマをファイル `./src/validation/validation-items.ts` に以下のように定義し、 Vue I18n を使用してデフォルトのエラーメッセージを設定します。

```typescript title="validation-items.ts"
https://github.com/AlesInfiny/maia/blob/main/samples/web-csr/dressca-frontend/consumer/src/validation/validation-items.ts
```

作成したファイルを読み込むため、 入力値を検証する Vue ファイルのスクリプト構文に以下を記述します。

```vue title="example.vue"
<script setup lang="ts">
import { toTypedSchema } from '@vee-validate/zod'
import { z } from 'zod'

// フォーム固有のバリデーション定義
const { requiredEmail: requiredEmailRule, required: requiredRule } = ValidationItems()
const formSchema = toTypedSchema(
  z.object({
    email: requiredEmailRule(),
    password: requiredRule(),
  }),
)
</script>
```

## 入力値検証の実行 {#input-validation}

どのように入力値検証をコーディングするかは、[公式ドキュメント :material-open-in-new:](https://vee-validate.logaretm.com/v4/guide/components/validation/){ target=_blank }を参照してください。
