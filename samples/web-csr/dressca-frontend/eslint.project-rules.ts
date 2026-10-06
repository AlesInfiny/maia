import type { Linter } from 'eslint'

/**
 * 全ワークスペースに適用するルールです。
 * 必要に応じて対象のファイルやルールを設定します。
 */
export const codingConventionRules: Linter.Config[] = [
  {
    name: 'dressca-frontend/coding-convention-rules',
    files: ['**/*.{vue,ts,mts,tsx}'],
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
]

/**
 * app 層以外での動的インポート（ `import()` ）を禁止するルールです。
 * 画面を動的インポートするのは app 層のルート表だけです。
 * 動的インポートは no-restricted-imports の検査の対象外のため、参照方向のルールを迂回できないようにします。
 */
const dynamicImportRestriction: Linter.RulesRecord = {
  'no-restricted-syntax': [
    'error',
    {
      selector: 'ImportExpression',
      message:
        '動的インポートは app 層だけで使用できます。画面の遅延読み込みはルート表で定義します。',
    },
  ],
}

/**
 * ワークスペースのフォルダー間の参照方向を強制するルールを生成します。
 *
 *   app 層（main.ts 、 App.vue 、 src/app）
 *         ↓
 *   pages（src/pages）
 *         ↓
 *   コンテキスト
 *         ↓
 *   業務共通（business-common）
 *         ↓
 *   システム共通（system-common）
 *
 * コンテキストの下にはドメインのフォルダーを置き、その下にレイヤーを並べます。
 * app 層は最上位の層のため、参照の制限を設けません。
 * pages から app 層への参照は、ルート名の定数（ `@/app/router/route-names` ）だけを例外として許可します。
 * pages のテストは、画面を遷移させるためにルーター（ `@/app/router` ）も参照できます。
 * @param workspace ルールを適用するワークスペースのフォルダー名。
 * @param contextPatterns 業務コードを持つ最上位フォルダー（コンテキスト）を表す
 *   `@/` エイリアスのパターン。
 * @returns 参照方向を強制する ESLint の設定の配列。
 */
function createLayerDependencyRules(workspace: string, contextPatterns: string[]): Linter.Config[] {
  const contextFileGlobs = contextPatterns.map((pattern) => {
    const contextFolder = pattern.replace(/^@\//, '').replace(/\/\*\*$/, '')
    return `**/${workspace}/src/${contextFolder}/**/*.{vue,ts,mts,tsx}`
  })
  // 各コンテキストパターンについて「コンテキスト全体を禁止しつつ public-api.ts だけ許可する」
  // 否定パターンのペアを作ります（gitignore 形式の除外記法）。
  const contextPatternsExceptPublicApi = contextPatterns.flatMap((pattern) => {
    const contextFolder = pattern.replace(/^@\//, '').replace(/\/\*\*$/, '')
    return [pattern, `!@/${contextFolder}/public-api`]
  })
  const pagesFileGlob = `**/${workspace}/src/pages/**/*.{vue,ts,mts,tsx}`
  // `__tests__/**/*.{vue,ts}` の形は、 pages 直下のフォルダーのテスト（ pages/basket/__tests__ など）に一致しないため、
  // `__tests__/**` の形で指定します。
  // `pages/**/__tests__/**` は pages 直下のテスト（ pages/__tests__ ）にも一致しないため、別に指定します。
  const pagesTestFileGlobs = [
    `**/${workspace}/src/pages/__tests__/**`,
    `**/${workspace}/src/pages/**/__tests__/**`,
  ]
  const pagesContextRestriction = {
    group: contextPatternsExceptPublicApi,
    message:
      'pages はコンテキストの public-api.ts 経由でのみ参照できます。コンテキストの内部フォルダーを直接参照することはできません。',
  }
  // app 層のうちルーター（ @/app/router ）とルート名の定数だけを除外します。
  // gitignore 形式では親のフォルダーを除外しないと配下のファイルを除外できないため、 @/app/router も除外します。
  const appPatternsExceptRouter = ['@/app/**', '!@/app/router', '!@/app/router/route-names']
  const pagesAppMessage =
    'pages から参照できる app 層のモジュールは、ルート名の定数（ @/app/router/route-names ）だけです。'

  return [
    {
      name: `${workspace}/layer-dependency/system-common`,
      files: [`**/${workspace}/src/system-common/**/*.{vue,ts,mts,tsx}`],
      rules: {
        'no-restricted-imports': [
          'error',
          {
            patterns: [
              {
                group: ['@/app/**', '@/business-common/**', '@/pages/**', ...contextPatterns],
                message:
                  'system-common は業務知識を持たない層です。 app 層、 business-common 、 pages 、コンテキストを参照できません。',
              },
            ],
          },
        ],
        ...dynamicImportRestriction,
      },
    },
    {
      name: `${workspace}/layer-dependency/business-common`,
      files: [`**/${workspace}/src/business-common/**/*.{vue,ts,mts,tsx}`],
      rules: {
        'no-restricted-imports': [
          'error',
          {
            patterns: [
              {
                group: ['@/app/**', '@/pages/**', ...contextPatterns],
                message: 'business-common は app 層、 pages 、コンテキストを参照できません。',
              },
            ],
          },
        ],
        ...dynamicImportRestriction,
      },
    },
    {
      name: `${workspace}/layer-dependency/context`,
      files: contextFileGlobs,
      rules: {
        'no-restricted-imports': [
          'error',
          {
            patterns: [
              {
                group: ['@/app/**', '@/pages/**'],
                message:
                  'コンテキストは app 層と pages を参照できません。 app 層と pages はコンテキストより上位の層です。',
              },
            ],
          },
        ],
        ...dynamicImportRestriction,
      },
    },
    {
      name: `${workspace}/layer-dependency/pages`,
      files: [pagesFileGlob],
      ignores: pagesTestFileGlobs,
      rules: {
        'no-restricted-imports': [
          'error',
          {
            paths: [{ name: '@/app/router', message: pagesAppMessage }],
            patterns: [
              pagesContextRestriction,
              { group: appPatternsExceptRouter, message: pagesAppMessage },
            ],
          },
        ],
        ...dynamicImportRestriction,
      },
    },
    {
      name: `${workspace}/layer-dependency/pages-test`,
      files: pagesTestFileGlobs,
      rules: {
        'no-restricted-imports': [
          'error',
          {
            patterns: [
              pagesContextRestriction,
              {
                group: appPatternsExceptRouter,
                message:
                  'pages のテストから参照できる app 層のモジュールは、ルーター（ @/app/router ）とルート名の定数（ @/app/router/route-names ）だけです。',
              },
            ],
          },
        ],
        ...dynamicImportRestriction,
      },
    },
  ]
}

/**
 * consumer ワークスペースのフォルダー間の参照方向を強制するルールです。
 */
export const consumerLayerDependencyRules: Linter.Config[] = createLayerDependencyRules(
  'consumer',
  ['@/shopping/**', '@/security/**'],
)

/**
 * admin のフォルダー間の参照方向を強制するルールです。
 */
export const adminLayerDependencyRules: Linter.Config[] = createLayerDependencyRules('admin', [
  '@/catalog-management/**',
  '@/security/**',
])
