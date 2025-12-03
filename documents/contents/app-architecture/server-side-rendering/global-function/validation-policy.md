---
title: SSR 編 - 全体処理方式
description: SSR アプリケーション全体で考慮すべきアーキテクチャについて、 その実装方針を説明します。
---

# 入力値検査方針 {#top}

入力チェックには以下の種類があります。

- 単項目チェック
- 項目間チェック
- 複合チェック

それぞれについて処理方式を説明します。

## 単項目チェック {#single-item-validation}

入力必須チェック、桁チェック、型チェックなど、単一の項目のみで完結する入力チェックです。
SSR アプリケーションでは、誤った入力情報がシステムに投入されることを防止する目的に行います。

<!-- textlint-disable ja-technical-writing/sentence-length -->

単項目チェックには、データ注釈（ DataAnnotation ）検証および Blazor が提供する `EditForm` を使用します。
詳細は [フォーム検証 :material-open-in-new:](https://learn.microsoft.com/ja-jp/aspnet/core/blazor/forms/validation?view=aspnetcore-10.0#form-validation){ target=_blank } および [データ注釈検証コンポーネントとカスタム検証 :material-open-in-new:](https://learn.microsoft.com/ja-jp/aspnet/core/blazor/forms/validation?view=aspnetcore-10.0#data-annotations-validator-component-and-custom-validation){ target=_blank } を参照してください。

<!-- textlint-enable ja-technical-writing/sentence-length -->

## 項目間チェック {#cross-item-validation}

パスワードと確認用パスワードが等しいかのチェック、タスク終了日が開始日より後であることのチェックなど、複数の項目を比較する入力チェックです。
SSR アプリケーションでは、誤った入力情報がシステムに投入されることを防止する目的に行います。

項目間チェックには通常 `EditForm` の `OnValidationRequested` イベントを使用します。
詳細は [OnValidationRequested イベントを使用した手動検証 :material-open-in-new:](https://learn.microsoft.com/ja-jp/aspnet/core/blazor/forms/validation?view=aspnetcore-10.0#manual-validation-using-the-onvalidationrequested-event){ target=_blank } を参照してください。

ただし、繰り返し使用する項目間チェックには、 `CompareAttribute` を使用する（同値比較の場合）か、独自の検証コンポーネントを作成して汎用化します。

`CompareAttribute` の詳細は [Microsoft の Web サイト :material-open-in-new:](https://learn.microsoft.com/ja-jp/dotnet/api/system.componentmodel.dataannotations.compareattribute) を参照してください。
独自の検証コンポーネントの詳細は [検証コンポーネントを使用したビジネス ロジック検証 :material-open-in-new:](https://learn.microsoft.com/ja-jp/aspnet/core/blazor/forms/validation?view=aspnetcore-10.0#business-logic-validation-with-a-validator-component){ target=_blank } を参照してください。

## 複合チェック {#complex-validation}

ID の重複チェック、注文数が在庫数以内であるかのチェックなど、システムの状態によって入力値が妥当かどうかが変わる入力チェックです。
多くの場合、データストア内のデータと突き合わせて入力値の妥当性を確認します。

複合チェックはアプリケーションコア層の業務ロジック内で実装します。
複合チェックでエラーがある場合は、業務例外を用いてアプリケーションコア層からビューモデルにエラーを通知します。
ビューモデルでは業務例外をキャッチし、ビューにエラーメッセージを設定します。
