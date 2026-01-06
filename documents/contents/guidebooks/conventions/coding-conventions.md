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

    - [SpotBugs :material-open-in-new:](https://spotbugs.github.io/){ target=_blank } のバグパターン

        SpotBugs は静的解析ツールであるとともに、バグパターンと呼ばれるバッドプラクティスや脆弱性に関する定義を持ち、それらのコード中での出現を検知します。
        SpotBugs によって検出された、バグパターンに該当するコードを修正することで、より安全なコーディングを実現できます。

- Vue.js アプリケーション
    - [Vue.js スタイルガイド :material-open-in-new:](https://ja.vuejs.org/style-guide/){ target=_blank }

        Vue.js が公式に提供するスタイルガイドです。
        TypeScript に対する規約ではカバーできない Vue 固有の記法について、エラーの発生やアンチパターンを避けるための規約を優先度別に定めています。
        <!-- textlint-disable ja-technical-writing/sentence-length -->
        Vue.js ではこれらの規約への違反を検出するための ESLint のプラグイン [eslint-plugin-vue :material-open-in-new:](https://eslint.vuejs.org/){ target=_blank } を提供しており、 [Bundle Configurations :material-open-in-new:](https://eslint.vuejs.org/user-guide/#bundle-configurations-eslint-config-js){ target=_blank } としていくつかの定義済み構成が公開されています。
        <!-- textlint-enable ja-technical-writing/sentence-length -->
        AlesInfiny Maia では、一般的に必須と考えられるルールに Vue.js コミュニティーの慣例に従ったルールを加えた構成である flat/recommended を使用します。

    - [typescript-eslint の推奨構成 :material-open-in-new:](https://typescript-eslint.io/users/configs/#recommended-configurations){ target=_blank }

        [typescript-eslint :material-open-in-new:](https://typescript-eslint.io/){ target=_blank } プロジェクトが提供する推奨設定です。
        <!-- textlint-disable ja-technical-writing/sentence-length -->
        AlesInfiny Maia では、公開されている推奨構成のうち、一般的に推奨されるルールに TypeScript の型情報を使用するルールを加えた [recommended-type-checked :material-open-in-new:](https://typescript-eslint.io/users/configs/#recommended-type-checked){ target=_blank } を使用します。
        <!-- textlint-enable ja-technical-writing/sentence-length -->

    - [CSS specifications :material-open-in-new:](https://www.w3.org/Style/CSS/current-work){ target=_blank }

        W3C が策定する CSS の標準仕様です。 Stylelint では、この標準仕様に従うための設定が公開されています。

上記のコーディング規約は静的コード解析ツールによって自動的にチェックできるようにします。
バックエンド側では、 Checkstyle を使用して Google Java Style への準拠を自動チェックし、
SpotBugs を利用して、 SpotBugs が提供するバグパターンに該当するコードを自動検知します。
加えて、 Visual Studio Code の自動フォーマット機能を利用することで、コーディング中に Google Java Style へ準拠したフォーマットへと自動でコードを修正します。
フロントエンド側では Prettier 、 ESLint 、 Stylelint を利用してコーディング規約の自動チェックを行っています。
コーディング規約の内容および静的コード解析ツールの詳しい設定方法については、以下のページとサンプルアプリの実装を確認してください。

- [Checkstyle プラグイン](../how-to-develop/csr/java/common-project-settings.md#checkstyle-plugin)
- [SpotBugs プラグイン](../how-to-develop/csr/java/common-project-settings.md#spotbugs-plugin)
- [Java formatting and linting :material-open-in-new:](https://code.visualstudio.com/docs/java/java-linting){ target=_blank }
- [静的コード分析とフォーマット(Vue.js)](../how-to-develop/csr/vue-js/static-verification-and-format.md)

## AlesInfiny Maia でカスタマイズしている規約 {#custom-conventions}

AlesInfiny Maia では上記に示した基本のコーディング規約に加えて、以下に示すカスタマイズした規約を採用しています。

- Java アプリケーション

    - Checkstyle プラグイン

        Checkstyle の規約をカスタマイズする場合、 Checkstyle プラグインが読み込むインプットファイルを編集します。具体的な方法については [こちら](../how-to-develop/csr/java/common-project-settings.md#checkstyle-plugin) を参照ください。
        Checkstyle プラグインでカスタマイズする規約は以下の通りです。

        - [IllegalCatch :material-open-in-new:](https://checkstyle.sourceforge.io/checks/coding/illegalcatch.html){ target=_blank }

            汎用検査例外を含む特定の例外のキャッチを禁止します。

            汎用的な例外をキャッチしてしまうと、具体的な例外が隠蔽されてしまい、原因の特定が難しくなります。
            汎用的な例外ではなく、具体的な例外のみをキャッチするように本規約を設けます。
