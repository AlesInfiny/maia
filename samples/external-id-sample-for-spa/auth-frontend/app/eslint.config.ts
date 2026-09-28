import type { Linter } from 'eslint'
import { defineConfig } from 'eslint/config'
import rootConfig from '../eslint.config'

export default defineConfig(
  // ルートプロジェクトの設定を継承します。
  // ルートの設定に含まれるファイルパスのパターンを、ルートプロジェクトを基準に評価するよう basePath を指定します。
  {
    basePath: '..',
    // defineConfigWithVueTs() の戻り値は typescript-eslint の型のため、 ESLint の型に変換します。
    extends: [rootConfig as Linter.Config[]],
  },

  // app プロジェクトに固有のルールを適用します。
  // 必要に応じて対象のファイルやルールを設定します。
  {
    name: 'auth-frontend/app',
    files: ['**/*.{vue,ts,mts,tsx}'],
    rules: {},
  },
)
