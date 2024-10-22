---
title: Vue.js 開発手順
description: Vue.js を用いた クライアントサイドアプリケーションの 開発手順を説明します。
---

<!-- cSpell:ignore parens rushstack stylelintrc -->

# 静的コード分析とフォーマット {#top}

静的コード分析とフォーマットには .editorconfig 、 ESLint 、 Stylelint 、および Prettier を使用します。
これらの設定はアプリケーション間で共通することが多いため、ルートプロジェクトに配置して共通化します。
下記の手順を実行後の設定ファイルの配置例を示します。

```terminal linenums="0"
<root-project-name> ------ ルートプロジェクト
├ .editorconfig
├ .eslintrc.cjs
├ .stylelintrc.js
├ .prettierrc.json
└ <workspace-name> -- ワークスペース/プロジェクト
  ├ .eslintrc.cjs
  └ .stylelintrc.js
```

## .editorconfigの追加 {#add-editorconfig}

ルートプロジェクトの直下に [.editorconfig :material-open-in-new:](https://editorconfig.org/){ target=_blank } を追加することで、 IDE 上で追加されるファイルにコーディングルールを課すことが可能になります。

VSCode の推奨プラグインである [EditorConfig for Visual Studio Code :material-open-in-new:](https://github.com/editorconfig/editorconfig-vscode){ target=_blank } を使用すると、以下のような設定が可能です。

- エンコード
- 改行コード
- 文末に空白行を追加
- インデントのサイズ
- インデントの形式
- 行末の空白を削除

開発時に統一する必要がある項目を .editorconfig に定義します。特に開発者によって差が出やすいエンコード、改行コードやインデントのサイズなどを定めておくと良いでしょう。

.editorconfig の設定には、自動的に適用されるものと、違反すると IDE のエディター上に警告として表示されるものがあります。詳細は [公式ドキュメント :material-open-in-new:](https://github.com/editorconfig/editorconfig-vscode){ target=_blank } を参照してください。

## Prettier {#prettier}

Prettier は Vue.js のブランクプロジェクト作成時にオプションとしてインストールしているため、追加でインストールする必要はありません。
ただし、ワークスペースの直下に作成されているため、ルートプロジェクトの直下に移動します。

### Prettier の設定 {#settings-prettier}

設定ファイル `./.prettierrc.json` で行います。このファイルはインストール時に自動的に追加されます。
既定の設定を上書きする場合、設定値を記述します。以下は設定例です。

```json title=".prettierrc.json"
{
  "semi": true,
  "arrowParens": "always",
  "singleQuote": true,
  "trailingComma": "all",
  "endOfLine": "auto"
}
```

一部の設定値は、既定で .editorconfig に記述している値が適用されます。したがって、`./.prettierrc.json` では、 .editorconfig では設定できないもののみ設定すると良いでしょう。

全ての設定可能な値は [Options - Prettier :material-open-in-new:](https://prettier.io/docs/en/options.html){ target=_blank } を参照してください。また、設定方法は [Configuration File - Prettier :material-open-in-new:](https://prettier.io/docs/en/configuration.html){ target=_blank } を参照してください。

## ESLint {#eslint}

ESLint は Vue.js のブランクプロジェクト作成時にオプションとしてインストールしているため、追加でインストールする必要はありません。

### ESLint の設定 {#settings-eslint}

設定ファイル `./.eslintrc.cjs` で行います。このファイルはインストール時にワークスペースの直下に自動的に追加されているので、ルートプロジェクトの直下にコピーします。

既定の状態でも静的コード分析は可能ですが、 postCSS の設定ファイルなど、分析する必要のないファイルまで分析対象となってしまうため、以下のように ignorePatterns を追加します。

```javascript title=".eslintrc.cjs" hl_lines="13 13"
/* eslint-env node */
require('@rushstack/eslint-patch/modern-module-resolution')

module.exports = {
  root: true,
  'extends': [
    'plugin:vue/vue3-essential',
    'eslint:recommended',
    '@vue/eslint-config-typescript',
    '@vue/eslint-config-prettier/skip-formatting',
  ],
  /* 中略 */
  ignorePatterns: ['postcss.config.js', 'tailwind.config.js'],
}
```

ワークスペース直下の設定ファイルは、ルートプロジェクト直下の設定ファイルを継承するように変更します。

```javascript title=".eslintrc.cjs"
/* eslint-env node */
require('@rushstack/eslint-patch/modern-module-resolution');

module.exports = {
  root: true,
  extends: '../.eslintrc.cjs',
};
```

その他の設定方法については [公式ドキュメント :material-open-in-new:](https://eslint.org/docs/latest/user-guide/configuring/){ target=_blank } を参照してください。

### ESLint と Prettier の連携 {#eslint-and-prettier}

Vue.js のブランクプロジェクト作成時に ESLint と Prettier をそれぞれオプションとしてインストールした場合、 ESLint と Prettier を連携させるプラグインが自動的にインストール・設定されます。
したがって、 ESLint と Prettier を連携させるための追加の設定は必要ありません。

## Stylelint {#stylelint}

### Stylelint のインストール {#install-stylelint}

Stylelint および、標準の設定や vue ファイルで使用する設定等をインストールします。サンプルアプリケーションでは以下をインストールしています。

| パッケージ名                     | 使用目的                               |
| -------------------------------- | -------------------------------------- |
| stylelint                        | cssファイルの構文検証                  |
| stylelint-config-standard        | Stylelint の標準設定                   |
| stylelint-config-recommended-vue | Stylelint の .vue ファイル向け推奨設定 |
| stylelint-prettier               | Stylelint と Prettier の連携プラグイン |

```terminal
npm install -D stylelint \
  stylelint-config-standard \
  stylelint-config-recommended-vue \
  stylelint-prettier
```

### Stylelint の設定 {#settings-stylelint}

ルートプロジェクトの直下に設定ファイル `./.stylelintrc.js` を作成し、コードを記述します。

```javascript title=".stylelintrc.js"
export default {
  plugins: ['stylelint-prettier'],
  extends: [
    'stylelint-config-standard',
    'stylelint-config-recommended-vue',
    'stylelint-prettier/recommended',
  ],
  rules: {
    'prettier/prettier': true,
    'at-rule-no-unknown': [
      true,
      { ignoreAtRules: ['tailwind', 'define-mixin'] },
    ],
  },
  ignoreFiles: ['dist/**/*'],
  overrides: [
    {
      files: ['**/*.vue'],
      customSyntax: 'postcss-html',
    },
  ],
};
```

各ワークスペースでは、ルートプロジェクトの設定ファイルを継承し、必要に応じて設定を追加します。

```javascript title=".stylelintrc.js"
import stylelintConfigBase from '../.stylelintrc.js'

export default {
  extends: stylelintConfigBase
};
```

`plugins`

:   使用する外部のプラグインを宣言します。

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

`./package.json` に ESLint を実行するための script がデフォルトで追加されています。ここに Stylelint も同時に実行するようにコマンドを追加します。追加後の scripts は以下のようになります（関係のないコマンドは省略しています）。

```json title="package.json"
"scripts": {
  "lint": "eslint . --ext .vue,.js,.jsx,.cjs,.mjs,.ts,.tsx,.cts,.mts --fix --ignore-path .gitignore && stylelint **/*.{vue,css} --fix",
}
```

Stylelint を vue ファイルと css ファイルに対して実行するように設定しています。

ターミナルを開き、コマンドを実行します。

```terminal
npm run lint
```

ESLint および Stylelint のオプション引数に `--fix` を設定しているため、フォーマットが自動的に実行されます。フォーマットできない違反については、ターミナル上で結果が表示されます。
