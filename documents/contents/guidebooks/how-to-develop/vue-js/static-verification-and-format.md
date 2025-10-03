---
title: Vue.js 開発手順
description: Vue.js を用いた フロントエンドアプリケーションの 開発手順を説明します。
---

<!-- cspell:ignore parens rushstack stylelintrc -->

# 静的コード分析とフォーマット {#top}

静的コード分析とフォーマットには .editorconfig 、 ESLint 、 Stylelint 、および Prettier を使用します。
これらの設定はアプリケーション間で共通することが多いため、ルートプロジェクトに配置して共通化します。
下記の手順を実行後の設定ファイルの配置例を示します。

```text linenums="0"
<root-project-name> ------ ルートプロジェクト
├ .editorconfig
├ eslint.config.ts
├ .prettierrc.js
├ .stylelintrc.js
├ tsconfig.json
└ <workspace-name> ------- ワークスペース/プロジェクト
  └ .prettierrc.js
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
ただし、 mono-repo 構成で使用するための追加の設定をします。

### Prettier の設定 {#settings-prettier}

設定ファイルが prettierrc.json がワークスペースの直下に作成されているため、ルートプロジェクトの直下にコピーします。
ワークスペース側で import するために、 prettierrc.js に拡張子を変更し、 JavaScript 形式に書き換えます。

```javascript title="ルートプロジェクトの .prettierrc.js の設定例"
/**
 * @see https://prettier.io/docs/configuration
 * @type {import("prettier").Config}
 */
const config = {
  $schema: 'https://json.schemastore.org/prettierrc',
  semi: false,
  singleQuote: true,
  printWidth: 100,
}

export default config
```

ワークスペース直下の prettierrc.json についても同様に拡張子を変更し、 JavaScript 形式に書き換えます。

```javascript title="ワークスペースの .prettierrc.js の設定例"
import prettierConfigBase from '../.prettierrc.js'
/**
 * @see https://prettier.io/docs/configuration
 * @type {import("prettier").Config}
 */
const config = {
  ...prettierConfigBase,
  // ワークスペースに固有の設定があれば、追加で設定します。
}

export default config
```

既定の設定を上書きする場合は、設定値を記述します。
全ての設定可能な値は [Options - Prettier :material-open-in-new:](https://prettier.io/docs/en/options.html){ target=_blank } を参照してください。また、設定方法は [Configuration File - Prettier :material-open-in-new:](https://prettier.io/docs/en/configuration.html){ target=_blank } を参照してください。

一部の設定値は、既定で .editorconfig に記述している値が適用されます。したがって、.prettierrc.json では、 .editorconfig では設定できないもののみ設定すると良いでしょう。

ワークスペースの直下にいることを確認し、下記のコマンドを実行します。

```shell linenums="0"
npm run format
```

Prettier が設定ファイルを認識し、フォーマット処理が正常に実行できることを確認してください。

## ESLint {#eslint}

ESLint および ESLint の実行に必要なパッケージは、 [ブランクプロジェクトの作成](./create-vuejs-blank-project.md) 時にオプションとしてインストールしているため、追加でインストールする必要はありません。

### ESLint の設定 {#settings-eslint}

#### 設定前の動作確認 {#pre-config-check}

ESLint の設定は、設定ファイル eslint.config.ts で行います。
このファイルはインストール時にワークスペースの直下に自動的に追加されています。
ワークスペースの直下にいることを確認し、下記のコマンドを実行します。

```shell linenums="0"
npm run lint
```

設定の変更前に、 ESLint が正常に実行できることを確認してください。

#### 設定例の確認 {#check-config-examples}

[コーディング規約](../../conventions/coding-conventions.md) に沿うように設定を追加・変更します。
初期設定からの変更点をハイライトで示します。

```typescript title="サンプルアプリケーションの eslint.config.ts" hl_lines="2 10 12-15 23-24 29 33 36-43 46-49 53-69 74 80-83"
https://github.com/AlesInfiny/maia/blob/main/samples/web-csr/dressca-frontend/eslint.config.ts
```

#### mono-repo 用の設定 {#mono-repo-config}

mono-repo 用に、設定ファイルの配置と設定を変更します。
eslint.config.ts を、ルートプロジェクトの直下に移動してください。
設定後のファイルの配置は下記の通りです。

```text linenums="0"
<root-project-name> ------ ルートプロジェクト
├ eslint.config.ts
├ tsconfig.json
├ <workspace-name> ------- ワークスペース/プロジェクト
```

eslint.config.ts に、下記の設定を追加してください。

```typescript
{
  languageOptions: {
    parserOptions: {
      projectService: true,
      tsconfigRootDir: import.meta.dirname
    },
  },
},
```

.vue ファイルを探すルートディレクトリを変更するよう下記の設定を追加します。
`configureVueProject()` の詳細は [Advanced Setup :material-open-in-new:](https://github.com/vuejs/eslint-config-typescript?tab=readme-ov-file#advanced-setup){ target=_blank } を参照してください。

```typescript
import { configureVueProject } from '@vue/eslint-config-typescript'
configureVueProject({
  rootDir: import.meta.dirname,
})
```

src フォルダーが eslint.config.ts の直下ではなくなるので、ワークスペース配下を検索するようにパスを修正します。

```typescript hl_lines="3 8-9"
{
  ...pluginVitest.configs.recommended,
  files: ['**/src/**/__tests__/**/*'],
},
{
  ...pluginCypress.configs.recommended,
  files: [
    '**/cypress/e2e/**/*.{cy,spec}.{js,ts,jsx,tsx}',
    '**/cypress/support/**/*.{js,ts,jsx,tsx}',
  ],
},
```

ルートプロジェクトの直下に、 eslint.config.ts 用の tsconfig.json ファイルを作成します。

```json title="eslint.config.ts 用の tsconfig.json" hl_lines="3"
https://github.com/AlesInfiny/maia/blob/main/samples/web-csr/dressca-frontend/tsconfig.json
```

ワークスペースの直下にいることを確認し、再度下記のコマンドを実行します。

```shell linenums="0"
npm run lint
```

ESLint がルートプロジェクトの設定ファイルを自動的に認識し、正常に実行できることを確認してください。

#### 適用ルールの変更 {#change-applied-rules}

Vue ファイルに適用するルールを、 `flat/essential` から `flat/recommended` に変更します。

```typescript
pluginVue.configs['flat/recommended'],
```

TypeScript の型情報を使用するルールを使用するため、 `vueTsConfigs.recommended` を `vueTsConfigs.recommendedTypeChecked` に変更します。

```typescript
vueTsConfigs.recommendedTypeChecked,
```

TypeScript 以外のファイルに対して、型情報を利用したルールの Lint を試みるとエラーが発生します。
そのため、 JavaScript ファイルに対して型情報を使用した Lint ルールを無効化するように、下記の設定を追加します。

```typescript
import tseslint from 'typescript-eslint'

{
  files: ['**/*.js'],
  extends: [tseslint.configs.disableTypeChecked],
},
```

プロジェクト固有のルールを追加します。
ESLint は eslint.config.ts の先頭から設定の内容をマージするので、重複する設定は後から配置されたもので上書きされます。
そのため、プロジェクト固有のルールは、推奨ルールの設定よりも後に配置するように気を付けてください。

```typescript
{
  name: 'app/additional-rules',
  files: ['**/*.{ts,mts,tsx,vue}'],
  rules: {
    'no-console': 'warn',
    'no-alert': 'warn',
    '@typescript-eslint/no-floating-promises': [
      'error',
      {
        // 戻り値の Promise を await 不要とみなすメソッドを例外登録します。
        allowForKnownSafeCalls: [
          { from: 'package', name: ['push', 'replace'], package: 'vue-router' },
        ],
      },
    ],
  },
},
```

ESLint の対象外とするファイルを追加します。
サンプルアプリケーションでは、[OpenAPI 仕様書からのクライアントコード生成](./create-api-client-code.md) で自動生成するファイルと、
[モックモードの設定](./mock-mode-settings.md) で追加するパッケージに由来するファイルは Lint 処理によって変更したくないので、 対象外にします。

```typescript hl_lines="5-6"
globalIgnores([
  '**/dist/**',
  '**/dist-ssr/**',
  '**/coverage/**',
  '**/src/generated/**',
  '**/mockServiceWorker.js',
]),
```

その他の設定については [公式ドキュメント :material-open-in-new:](https://eslint.org/docs/latest/user-guide/configuring/){ target=_blank } を参照してください。

??? info "ESLint Config Inspector で設定を可視化する"
      [ESLint Config Inspector :material-open-in-new:](https://github.com/eslint/config-inspector){ target=_blank } を使用することで、 ESLint の設定をブラウザー上で可視化できます。
      ルールを変更・追加する際には、想定通りの変更が行われているか確認してください。
      設定ファイルが存在するフォルダーの直下で下記のコマンドを実行することで、 ESLint Config Inspector を起動できます。

      ```shell linenums="0"
      npx @eslint/config-inspector@latest
      ```

#### ESLint の実行 {#run-eslint}

ワークスペースの直下にいることを確認し、再度下記のコマンドを実行します。
ESLint が更新後の設定で正常に実行できることを確認してください。

```shell linenums="0"
npm run lint
```

## Stylelint {#stylelint}

CSS ファイルおよび、 Vue ファイルの`<template>`ブロック、`<style>`ブロックに記述する CSS に対して静的解析をするため、 StyleLint を導入します。
[ブランクプロジェクトの作成](./create-vuejs-blank-project.md) 時には追加されないため、手動でインストールする必要があります。

### Stylelint のインストール {#install-stylelint}

ワークスペースの直下で下記のコマンドを実行してください。

```shell linenums="0"
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
export default {
  extends: ['stylelint-config-standard', 'stylelint-config-recommended-vue'],
  ignoreFiles: ['dist/**/*'],
  overrides: [
    {
      files: ['**/*.vue'],
      /** Vue ファイルの <style> ブロック内を Lint するための設定です。*/
      customSyntax: 'postcss-html',
    },
  ],
}
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

`ignoreFiles`

:   分析の対象外とするファイルまたはフォルダーを設定します。

`overrides`

:   特定のファイルにのみ別のルールを設定したい場合に使用します。

具体的な設定方法や設定値については [公式ドキュメント :material-open-in-new:](https://stylelint.io/user-guide/configure){ target=_blank } を参照してください。

??? warning "デフォルトのアプリケーションに対する Stylelint の警告"

      create-vue で作成されるデフォルトのアプリケーションに対して Stylelint を実行すると、下記の警告が出力されて、動作確認が進められない場合があります。

      ```text linenums="0"
      src/assets/base.css
        25:1  ✖  Unexpected duplicate selector ":root", first used at line 2  no-duplicate-selectors
      ```

      その場合は、 VS Code の拡張機能のサジェストに従って `/* stylelint-disable-next-line no-duplicate-selectors */` を追加し、
      対象箇所のルール違反を無視するようにして動作確認を続行してください。
      ただし、実際のプロジェクトで使用するコードに対しては、ルール違反を無視する設定の利用はできるだけ避けて、警告に従ってコードを修正するようにしてください。

## 静的コード分析とフォーマットの実行 {#static-code-analysis-and-format}

各ワークスペースの package.json には ESLint および Prettier を実行するための scripts がデフォルトで定義されています。
CI での実行と個別の実行を可能にするため、下記のように変更します。

ESLint および Stylelint のオプション引数に `--fix` を、 Prettier のオプション引数に `--write` を設定しているため、自動で修正可能なものについては修正が実行されます。
自動で修正できないルール違反については、ターミナル上に結果が表示されます。
一方で、 `:ci` を付与したタスクではこれらのオプションを使用していないため、自動的に修正可能なルール違反であっても修正は実行されません。

```json title="サンプルアプリケーションの package.json"
https://github.com/AlesInfiny/maia/blob/main/samples/web-csr/dressca-frontend/consumer/package.json#L21-L28
```

ルートワークスペースの直下にいることを確認し、[ワークスペースの設定 - スクリプトの定義](./setting-workspaces.md#register-npm-scripts) で定義した `lint:ci` を実行します。

```shell linenums="0"
npm run lint:ci:workspace-name
```

ESLint 、 Stylelint 、 Prettier が実行されることを確認してください。
