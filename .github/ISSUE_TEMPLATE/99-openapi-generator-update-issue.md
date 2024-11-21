---
name: Open APIジェネレーターのアップデート
about: openapi-generatorのアップデートを促すIssueのテンプレートです。
title: "[{{ env.TARGET_APP_NAME }}]openapi-generatorをアップデートする from {{ env.CURRENT_VERSION }} to {{ env.LATEST_VERSION }}"
description: ''
labels: ''
assignees: ''
---

<!-- textlint-disable @textlint-rule/require-header-id -->

## 概要

[openapi-generator](https://github.com/OpenAPITools/openapi-generator) のバージョンアップを検知しました。内容を確認のうえ、下記の通り対応してください。

- openapitools.json のバージョン ：{{ env.CURRENT_VERSION }}
- ライブラリのバージョン ：{{ env.LATEST_VERSION }}

## 詳細

下記のコマンドを実行し、最新バージョンを選択します。
openapitools.json の version が選択したバージョンに更新されます。

```terminal
npx openapi-generator-cli version-manager list
```

クライアントコードを再生成します。

```terminal
npm run generate-client
```

自動生成されたクライアントコードに差分がある場合、
差分の内容の確認とアプリケーションの動作確認を行ってください。

## 完了条件

- [ ] openapitools.json の version が最新バージョンに更新されていること
- [ ] 生成されたクライアントコードに差分がないか、差分に問題がないこと
- [ ] アプリケーションの動作に問題がないこと

<!-- textlint-enable @textlint-rule/require-header-id -->
