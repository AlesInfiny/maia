---
title: Java アプリケーションの 処理方式
description: アプリケーションの形態によらず、 Java アプリケーションで 考慮すべき関心事について、実装方針を説明します。
---

<!-- cspell:ignore applicationcore systemcommon -->

# メッセージ管理方針 {#top}

フロントエンドアプリケーションのメッセージ管理方針については、以下を参照してください。

- [フロントエンドアプリケーションの処理方式 - メッセージ管理方針](../../client-side-rendering/global-function/message-management-policy.md)

メッセージ文字列は、表記の統一を図ることを目的にプロパティファイルで管理します。

## プロパティファイルの管理 {#property-file-management}

プロパティファイルでは、以下のようにメッセージ文字列を識別するメッセージコードとメッセージ文字列本体をペアで管理します。

```properties title="messages.properties の例"
errorOccurred=エラーが発生しました。
...
```

## エラーメッセージコードの統一 {#unification-of-message-codes}

同一の業務エラーやシステムエラーのメッセージコードは、バックエンド側とフロントエンド側で統一します。

詳細については、[こちら](../../client-side-rendering/global-function/message-management-policy.md#unification-of-message-codes) を確認してください。

## メッセージの管理単位 {#management-unit}

以下のように、ビジネスロジックで利用する業務メッセージと共通処理として利用する共通メッセージを分割して各サブプロジェクトで管理します。

```text linenums="0"
root/ ------------------------------------------- root フォルダー
 ├ application-core/src/main/resource
 │ └ applicationcore ---------------------------- 業務メッセージのプロパティファイルを一括管理するフォルダー
 │    └ messages.properties --------------------- 業務メッセージのプロパティファイル
 └ system-common/src/main/resource
   └ systemcommon ------------------------------- 共通メッセージのプロパティファイルを一括管理するフォルダー
      └ messages.properties --------------------- 共通メッセージのプロパティファイル
```

業務メッセージと共通メッセージとして格納するメッセージの例は以下の通りです。

- 業務メッセージの例

    - 各業務例外発生時にログ出力するためのエラーメッセージ
    - 業務完了時に正常終了した旨をログに出力するためのメッセージ

- 共通メッセージの例

    - システム例外発生時にログ出力するためのエラーメッセージ
    - 業務例外やシステム例外の既定メッセージ

## 多言語対応 {#localization}

メッセージを多言語対応する場合には、それぞれの言語のプロパティファイルを作成し、各言語のメッセージをファイルで分割して管理します。
以下に示すように、各ファイル名は [ISO-639 言語コード :material-open-in-new:](https://www.iso.org/iso-639-language-code){ target=_blank } に基づき、その言語を表す言語コードを末尾に付与します。

```text linenums="0"
root/ ------------------------------------------- root フォルダー
 ├ application-core/src/main/resource
 │ └ applicationcore ---------------------------- 業務メッセージのプロパティファイルを一括管理するフォルダー
 │    ├ messages_en.properties ------------------ 業務メッセージのプロパティファイル（英語）
 │    └ messages_ja.properties ------------------ 業務メッセージのプロパティファイル（日本語）
 └ system-common/src/main/resource
   └ systemcommon ------------------------------- 共通メッセージのプロパティファイルを一括管理するフォルダー
      ├ messages_en.properties ------------------ 共通メッセージのプロパティファイル（英語）
      └ messages_ja.properties ------------------ 共通メッセージのプロパティファイル（日本語）
```

アプリケーション起動時に使用するメッセージファイルを切り替えることで、開発者に応じた言語を設定します。

メッセージ管理方針に従った機能の実装方法などの詳細については、[こちら](../../../guidebooks/how-to-develop/java/sub-project-settings/message-management.md) を確認してください。
