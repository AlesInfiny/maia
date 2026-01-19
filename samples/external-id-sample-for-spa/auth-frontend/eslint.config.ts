import { globalIgnores } from 'eslint/config'
import tseslint from 'typescript-eslint'
import { defineConfigWithVueTs, vueTsConfigs } from '@vue/eslint-config-typescript'
import pluginVue from 'eslint-plugin-vue'
import pluginVitest from '@vitest/eslint-plugin'
// eslint-disable-next-line @typescript-eslint/ban-ts-comment
// @ts-ignore
import skipFormatting from '@vue/eslint-config-prettier/skip-formatting'
import { configureVueProject } from '@vue/eslint-config-typescript'
import jsdoc from 'eslint-plugin-jsdoc'

configureVueProject({
  // mono-repo 用に、 .vue ファイルを探すルートディレクトリをデフォルト値 `process.cwd()` から変更します。
  rootDir: import.meta.dirname,
})

export default defineConfigWithVueTs(
  // Lint 対象外とするファイルパスを列挙します。
  globalIgnores([
    '**/dist/**',
    '**/dist-ssr/**',
    '**/coverage/**',
    '**/src/generated/**',
    '**/mockServiceWorker.js',
  ]),

  // Vue.js 向けの推奨ルールを適用します。
  // .vue ファイルを Lint の対象とします。
  pluginVue.configs['flat/recommended'],

  // TypeScript + Vue.js 向けの型情報を使用した推奨ルールを適用します。
  // .vue .ts .mts .ts .vue ファイルを Lint の対象とします。
  vueTsConfigs.recommendedTypeChecked,

  // 型情報を使用した Lint を実行するために、 tsconfig ファイルを探すための設定をします。
  {
    languageOptions: {
      parserOptions: {
        projectService: true,
        tsconfigRootDir: import.meta.dirname,
      },
    },
  },

  // JavaScript ファイルに対しては、 型情報を使用した Lint は無効化します。
  {
    files: ['**/*.js'],
    extends: [tseslint.configs.disableTypeChecked],
  },

  // プロジェクトやワークスペースに固有のルールを適用します。
  // 必要に応じて対象のファイルやルールを設定します。
  {
    name: 'auth-frontend/additional-rules',
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

  // Vitest 用のテストスイートに対して、 Vitest 推奨の Lint ルールを適用します。
  {
    ...pluginVitest.configs.recommended,
    files: ['**/src/**/__tests__/**/*'],
  },

  // TypeScript ファイルに対して JSDoc 形式のドキュメンテーションを強制します。
  {
    ...jsdoc.configs['flat/recommended-typescript-error'],
    files: ['**/*.ts'],
  },

  // コードのフォーマットは Prettier で実行するので、 ESLint のフォーマットルールは無効化します。
  skipFormatting,
)
