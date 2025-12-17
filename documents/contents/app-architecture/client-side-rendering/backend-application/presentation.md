---
title: CSR 編 - Web API
description: バックエンドアプリケーションのアーキテクチャについて、 層ごとに詳細を説明します。
---

# プレゼンテーション層 {#top}

プレゼンテーション層で実装するべき共通処理の詳細を示します。

## 入力チェック ( 単項目チェックと項目間チェック ) {#validation}

[全体処理方式の入力値検査方針](../global-function/validation-policy.md) で説明した通り、プレゼンテーション層では入力値に対する単項目チェックと項目間チェックを実装します。
単項目チェックと項目間チェックを通じて、 Web API の入力値が正しい形式であるか確認します。

### 単項目チェック {#single-item-check}

単項目チェックは Bean Validation 仕様に準拠したバリデーションフレームワークである [Hibernate Validator :material-open-in-new:](https://hibernate.org/validator/){ target=_blank } を用いて実装します。
API モデルに対してアノテーションを付与し、チェックルールを定義します。
実装方法については [こちら :material-open-in-new:](https://docs.hibernate.org/stable/validator/reference/en-US/html_single/#chapter-bean-constraints){ target=_blank } を参照してください。

Hibernate Validator であらかじめ実装されているチェックルールは [こちら :material-open-in-new:](https://docs.hibernate.org/stable/validator/reference/en-US/html_single/#section-builtin-constraints){ target=_blank } を参照してください。
単項目チェックの多くは、これらの実装済みチェックルールを利用するだけで検証を行えます。
既存のチェックルールでは実現できない場合は、[こちら :material-open-in-new:](https://docs.hibernate.org/stable/validator/reference/en-US/html_single/#validator-customconstraints-simple){ target=_blank } を参照してカスタムルールを実装します。

単項目チェックは、コントローラーの引数である API モデルに対して一律実行されるように実装します。

### 項目間チェック {#cross-field-check}

Hibernate Validator で定義済みのチェックルールは、項目間のデータの整合性を確認する目的には利用できません。
そのため、項目間チェック処理は独自の実装が必要です。

項目間チェックの実装は、以下の 2 通りの方法があります。

- コントローラーの処理の中で実装する
  
- Bean Validation のカスタムルールとして実装する

項目間チェックをコントローラー内に実装してしまうと、同じような項目間チェックがある場合に再利用ができません。
汎用的なチェックルールは Bean Validation のカスタムルールとして実装することを推奨します。

汎用的でないチェックルールは、どちらの方法で実装してもかまいません。
Bean Validation のカスタムルールで実装すると処理方式の統一が図れる反面、カスタムルールの実装は若干煩雑です。

## 例外ハンドリング {#exception-handling}

業務例外は、業務ロジックに従って適切な層でハンドリングします。
どの層でハンドリングするかは業務ロジックに依存し、統一した取り扱いは行いません。

プレゼンテーション層まででハンドリングされなかった業務例外やシステム例外は、集約例外ハンドラーで処理します。

### 集約例外ハンドラーの実装 {#implement-exception-handler}

集約例外ハンドラーは Spring Framework の `#!java @ControllerAdvice` を利用して実装します。
`#!java @ControllerAdvice` はコントローラーの共通処理を定義できるもので、例外ハンドリングの実装もサポートしています。
集約例外ハンドラーで実装すべき内容は以下の通りです。

- エラーログの出力

    例外情報からメッセージを生成し、アプリケーションログを出力します。
    出力先や出力内容は [ログ出力方針](../../overview/java-application-processing-system/logging-policy.md) に従います。

- エラーレスポンスの生成

    Web API のエラーレスポンスは、 [RFC9457 :material-open-in-new:](https://datatracker.ietf.org/doc/html/rfc9457){ target=_blank } に準拠した形式で返却します。

    HTTP ステータスコードは発生したエラーの種類に応じて使い分けます。
    エラーレスポンスに使用する代表的な HTTP ステータスコードは以下の通りです。

    | エラーステータスコード    | 利用シーン                                                                 |
    | ------------------------- | -------------------------------------------------------------------------- |
    | 400 Bad Request           | リクエストデータの構造エラー・入力値検証エラー・業務エラーが発生したとき。 |
    | 404 Not Found             | リクエストされたリソースが見つからないとき。                               |
    | 409 Conflict              | リソースで競合が発生し、正常に処理できないとき。                           |
    | 500 Internal Server Error | 予期しないエラー ( システムエラー ) が発生したとき。                       |
