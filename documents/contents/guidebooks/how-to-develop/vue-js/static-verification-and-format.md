---
title: Vue.js 開発手順
description: Vue.js を用いた フロントエンドアプリケーションの 開発手順を説明します。
---

<!-- cspell:ignore parens rushstack stylelintrc -->

# 静的コード分析とフォーマット {#top}

静的コード分析とフォーマットには .editorconfig 、 ESLint 、 Stylelint 、および Prettier を使用します。
これらの設定はアプリケーション間で共通することが多いため、ルートプロジェクトに配置して共通化します。
下記の手順を実行後の設定ファイルの配置例を示します。

```terminal linenums="0"
<root-project-name> ------ ルートプロジェクト
├ .editorconfig
├ .eslint.config.ts
├ .stylelintrc.js
├ .prettierrc.json
└ <workspace-name> ------- ワークスペース/プロジェクト
  └ .stylelintrc.js
```

## .editorconfigの追加 {#add-editorconfig}

[.editorconfig :material-open-in-new:](https://editorconfig.org/){ target=_blank }  を用いることで、 IDE 上で追加されるファイルにフォーマットルールを課すことが可能になります。
[ブランクプロジェクトの作成](./create-vuejs-blank-project.md) 時に、各ワークスペースの直下に .editorconfig が自動的に作成されているので、ルートプロジェクトに移動してください。

<!-- textlint-disable ja-technical-writing/sentence-length -->
Visual Studio Code の推奨プラグインである [EditorConfig for Visual Studio Code :material-open-in-new:](https://github.com/editorconfig/editorconfig-vscode){ target=_blank } を使用すると、以下のような設定が可能です。
<!-- textlint-enable ja-technical-writing/sentence-length -->

- エンコード
- 改行コード
- 文末に空白行を追加
- インデントのサイズ
- インデントの形式
- 行末の空白を削除

開発時に統一する必要がある項目を .editorconfig に定義します。特に開発者によって差が出やすいエンコード、改行コードやインデントのサイズなどを定めておくと良いでしょう。

.editorconfig の設定には、自動的に適用されるものと、違反すると IDE のエディター上に警告として表示されるものがあります。詳細は [公式ドキュメント :material-open-in-new:](https://github.com/editorconfig/editorconfig-vscode){ target=_blank } を参照してください。

??? example ".editorconfig の設定例"

    デフォルトでは上位のフォルダ階層に対して可能なかぎり .editorconfig ファイルを探索し、複数見つかった場合は上位の階層の設定を引き継ぎつつ、
    キーが重複したプロパティについては下位の階層の設定でオーバーライドします。
    しかし、 `root = true` が設定された .editorconfig が見つかった時点で探索を停止します。
    そのため、意図せず同じリポジトリ内の別の .editorconfig を参照することがないように、ルートプロジェクトの .editorconfig には `root = true` を設定しておくとよいでしょう。

    ```text title="サンプルアプリケーションの .editorconfig" hl_lines="1"
    https://github.com/AlesInfiny/maia/blob/main/samples/web-csr/dressca-frontend/.editorconfig
    ```

## Prettier {#prettier}

Prettier は [ブランクプロジェクトの作成](./create-vuejs-blank-project.md) 時にオプションとしてインストールしているため、追加でインストールする必要はありません。
ただし、設定ファイルがワークスペースの直下に作成されているため、ルートプロジェクトの直下に移動します。

### Prettier の設定 {#settings-prettier}

設定ファイル prettierrc.json で行います。

```json title=".prettierrc.json の設定例"
https://github.com/AlesInfiny/maia/blob/main/samples/web-csr/dressca-frontend/.prettierrc.json
```

既定の設定を上書きする場合は、設定値を記述します。
全ての設定可能な値は [Options - Prettier :material-open-in-new:](https://prettier.io/docs/en/options.html){ target=_blank } を参照してください。また、設定方法は [Configuration File - Prettier :material-open-in-new:](https://prettier.io/docs/en/configuration.html){ target=_blank } を参照してください。

一部の設定値は、既定で .editorconfig に記述している値が適用されます。したがって、.prettierrc.json では、 .editorconfig では設定できないもののみ設定すると良いでしょう。

ワークスペースの直下にいることを確認し、下記のコマンドを実行します。

```terminal
npm run format
```

Prettier がルートプロジェクトの設定ファイルを自動的に認識し、フォーマット処理が正常に実行できることを確認してください。

## ESLint {#eslint}

ESLint は Vue.js のブランクプロジェクト作成時にオプションとしてインストールしているため、追加でインストールする必要はありません。

### ESLint の設定 {#settings-eslint}

設定ファイル .eslint.config.ts で行います。このファイルはインストール時にワークスペースの直下に自動的に追加されているので、ルートプロジェクトの直下にコピーします。

??? info "eslint.config.ts の初期設定"

      ```typescript title="初期設定時の eslint.config.ts"
      import { globalIgnores } from 'eslint/config'
      import { defineConfigWithVueTs, vueTsConfigs } from '@vue/eslint-config-typescript'
      import pluginVue from 'eslint-plugin-vue'
      import pluginVitest from '@vitest/eslint-plugin'
      // eslint-disable-next-line @typescript-eslint/ban-ts-comment
      // @ts-ignore
      import pluginCypress from 'eslint-plugin-cypress'
      import skipFormatting from '@vue/eslint-config-prettier/skip-formatting'

      // To allow more languages other than `ts` in `.vue` files, uncomment the following lines:
      // import { configureVueProject } from '@vue/eslint-config-typescript'
      // configureVueProject({ scriptLangs: ['ts', 'tsx'] })
      // More info at https://github.com/vuejs/eslint-config-typescript/#advanced-setup

      export default defineConfigWithVueTs(
        {
          name: 'app/files-to-lint',
          files: ['**/*.{ts,mts,tsx,vue}'],
        },

        globalIgnores(['**/dist/**', '**/dist-ssr/**', '**/coverage/**']),

        pluginVue.configs['flat/essential'],
        vueTsConfigs.recommended,

        {
          ...pluginVitest.configs.recommended,
          files: ['src/**/__tests__/*'],
        },

        {
          ...pluginCypress.configs.recommended,
          files: [
            'cypress/e2e/**/*.{cy,spec}.{js,ts,jsx,tsx}',
            'cypress/support/**/*.{js,ts,jsx,tsx}'
          ],
        },
        skipFormatting,
      )
      ```

[コーディング規約](../../conventions/coding-conventions.md) に沿うように設定を追加・変更します。
最終的な設定例を示します。

!!! example "eslint.config.ts の設定例"

    ```typescript title="サンプルアプリケーションの eslint.config.ts"
    https://github.com/AlesInfiny/maia/blob/main/samples/web-csr/dressca-frontend/eslint.config.ts
    ```

その他の設定方法については [公式ドキュメント :material-open-in-new:](https://eslint.org/docs/latest/user-guide/configuring/){ target=_blank } を参照してください。

## Stylelint {#stylelint}

CSS ファイルおよび、 Vue ファイルの`<template>`ブロック、`<style>`ブロックに記述する CSS に対して静的解析をするため、 StyleLint を導入します。
[ブランクプロジェクトの作成](./create-vuejs-blank-project.md) 時には追加されないため、手動でインストールする必要があります。

### Stylelint のインストール {#install-stylelint}

ワークスペースの直下で下記のコマンドを実行してください。

```terminal
npm install -D stylelint \
  stylelint-config-standard \
  stylelint-config-recommended-vue
```

Stylelint および、標準の設定や vue ファイルで使用する設定等をインストールします。
サンプルアプリケーションでは以下をインストールしています。

| パッケージ名                     | 使用目的                               |
| -------------------------------- | -------------------------------------- |
| stylelint                        | cssファイルの構文検証                  |
| stylelint-config-standard        | Stylelint の標準設定                   |
| stylelint-config-recommended-vue | Stylelint の .vue ファイル向け推奨設定 |

### Stylelint の設定 {#settings-stylelint}

ルートプロジェクトの直下に設定ファイル .stylelintrc.js を作成し、設定を記述します。

```javascript title=".stylelintrc.js"
https://github.com/AlesInfiny/maia/blob/main/samples/web-csr/dressca-frontend/.stylelintrc.js
```

各ワークスペースでは、ルートプロジェクトの設定ファイルを継承し、必要に応じて設定を追加します。

```javascript title=".stylelintrc.js"
import stylelintConfigBase from '../.stylelintrc.js'

export default {
  extends: stylelintConfigBase
};
```

`extends`

:   既存の構成を拡張します。

`rules`

:   使用するルールを宣言します。

`ignoreFiles`

:   分析の対象外とするファイルまたはフォルダーを設定します。

`overrides`

:   特定のファイルにのみ別のルールを設定したい場合に使用します。

具体的な設定方法や設定値については [公式ドキュメント :material-open-in-new:](https://stylelint.io/user-guide/configure){ target=_blank } を参照してください。

## 静的コード分析とフォーマットの実行 {#static-code-analysis-and-format}

各ワークスペースの package.json には ESLint および Prettier を実行するための scripts がデフォルトで定義されています。

```json title="サンプルアプリケーションの package.json"
https://github.com/AlesInfiny/maia/blob/main/samples/web-csr/dressca-frontend/consumer/package.json#L18-L25
```

ターミナルを開き、コマンドを実行します。

```terminal
npm run lint
```

ESLint 、 Stylelint 、 Prettier が順に実行されることを確認してください。

Stylelint を vue ファイルと css ファイルに対して実行するように設定しています。
ESLint および Stylelint のオプション引数に `--fix` を設定しているため、フォーマットが自動的に実行されます。フォーマットできない違反については、ターミナル上で結果が表示されます。
