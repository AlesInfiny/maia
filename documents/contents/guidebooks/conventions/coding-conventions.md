---
title: コーディング規約
description: AlesInfiny Maia OSS Edition のコーディング規約に関する方針を示します。
---

# コーディング規約 {#top}

AlesInfiny Maia OSS Edition （以下 AlesInfiny Maia ）では、一般に広く採用されている規約に準拠し、必要に応じて最低限のカスタムルールを加えることを基本方針とします。
ゼロから独自規約を作成することは、以下のような問題があるため推奨しません。

- 規約作成にかかる負荷が大きい
- 必要な規約の漏れが発生しやすい
- 機械的なチェックの仕組みを作りにくい

## AlesInfiny Maia で採用している規約 {#style-guide}

Java アプリケーション、 Vue.js アプリケーションそれぞれで以下の内容を基本のコーディング規約としています。

- Java アプリケーション
    - [Google Java Style :material-open-in-new:](https://google.github.io/styleguide/javaguide.html){ target=_blank }

        Google が提供する、コードのフォーマットに対する規約です。
        改行やインデントの規則、メソッド名や変数名の命名規則などを定めています。

    - [Spotbugs :material-open-in-new:](https://spotbugs.github.io/){ target=_blank } のバグパターン

        Spotbugs は、コード中のバッドプラクティスや脆弱性をバグパターンとして検出します。
        Spotbugs によって検出されたコードを修正することで、より安全なコーディングを実現できます。

- Vue.js アプリケーション
    - TypeScript

        [Airbnb JavaScript Style Guide :material-open-in-new:](https://github.com/airbnb/javascript){ target=_blank }

    - Vue.js

        [Vue.js スタイルガイド :material-open-in-new:](https://ja.vuejs.org/style-guide/){ target=_blank }

    - CSS

        [CSS specifications :material-open-in-new:](https://www.w3.org/Style/CSS/current-work){ target=_blank }

上記のコーディング規約は静的コード解析ツールによって自動的にチェックできるようにします。
バックエンド側では、Checkstyleを使用して Google Java Style への準拠を自動チェックし、
Spotbugs を利用して、 Spotbugs が提供するバグパターンに合致するコードを検知します。
加えて、 VS Code の自動フォーマット機能を利用することで、コーディング中に Google Java Style に準拠したフォーマットへ自動でコードを修正します。
フロントエンド側では Prettier 、 ESLint 、 StyleLint を利用してコーディング規約の自動チェックを行っています。
コーディング規約の内容および静的コード解析ツールの詳しい設定方法については、以下のページとサンプルアプリの実装を確認してください。

- [Checkstyle プラグイン](../how-to-develop/java/common-project-settings.md#checkstyle-plugin)
- [SpotBugs プラグイン](../how-to-develop/java/common-project-settings.md#spotbugs-plugin)
- [Java formatting and linting :material-open-in-new:](https://code.visualstudio.com/docs/java/java-linting){ target=_blank }
- [静的コード分析とフォーマット(Vue.js)](../how-to-develop/vue-js/static-verification-and-format.md)