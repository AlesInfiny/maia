import { globalIgnores } from 'eslint/config'
import { defineConfigWithVueTs, vueTsConfigs } from '@vue/eslint-config-typescript'
import pluginVue from 'eslint-plugin-vue'
import pluginPlaywright from 'eslint-plugin-playwright'
import pluginVitest from '@vitest/eslint-plugin'
import skipFormatting from 'eslint-config-prettier/flat'
import tseslint from 'typescript-eslint'
import { configureVueProject } from '@vue/eslint-config-typescript'
import jsdoc from 'eslint-plugin-jsdoc'
import {
  adminLayerDependencyRules,
  codingConventionRules,
  consumerLayerDependencyRules,
} from './eslint.project-rules'

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
    '**/src/system-common/generated/**',
    '**/mockServiceWorker.js',
    '**/playwright-report/**',
  ]),

  // Vue.js 向けの推奨ルールを適用します。
  // .vue ファイルを Lint の対象とします。
  ...pluginVue.configs['flat/recommended'],

  // TypeScript + Vue.js 向けの型情報を使用した推奨ルールを適用します。
  // .vue .ts .mts .tsx ファイルを Lint の対象とします。
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

  // コーディング規約に沿わせるためのルールを適用します。
  ...codingConventionRules,

  // consumer / admin プロジェクトのフォルダー間の参照方向を強制します。
  ...consumerLayerDependencyRules,
  ...adminLayerDependencyRules,

  // Playwright 用のテストスイートに対して、 Playwright 推奨の Lint ルールを適用します。
  {
    ...pluginPlaywright.configs['flat/recommended'],
    files: ['**/e2e/**/*.{spec,test}.{js,ts,jsx,tsx}'],
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
