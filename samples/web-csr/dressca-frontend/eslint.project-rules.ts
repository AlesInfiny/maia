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
 * 参照を禁止するルールの名前です。
 * vue-router の型だけを許可するため（ `allowTypeImports` ）、 typescript-eslint のルールを使用します。
 */
const restrictedImportsRule = '@typescript-eslint/no-restricted-imports'

/**
 * vue-router の値の参照を禁止し、型の参照だけを許可する設定です。
 * 画面を遷移させられるのは pages 層と app 層だけです。
 */
const vueRouterValueRestriction = {
  name: 'vue-router',
  message:
    '画面を遷移させられるのは pages 層と app 層だけです。 vue-router は型だけ参照できます。遷移先が必要な場合は、遷移先を引数で受け取ります。',
  allowTypeImports: true,
}

/**
 * コンテキストを表す `@/` エイリアスのパターンからフォルダー名を取り出します。
 * @param contextPattern コンテキストを表すパターン（例: `@/security/**` ）。
 * @returns フォルダー名（例: `security` ）。
 */
function toContextFolder(contextPattern: string): string {
  return contextPattern.replace(/^@\//, '').replace(/\/\*\*$/, '')
}

/**
 * コンテキスト全体の参照を禁止しつつ、 public-api.ts だけを許可するパターンを作ります。
 * gitignore 形式の除外記法で、コンテキストごとに否定パターンのペアを作ります。
 * @param contextPatterns コンテキストを表すパターン。
 * @returns 参照を禁止するパターン。
 */
function exceptPublicApi(contextPatterns: string[]): string[] {
  return contextPatterns.flatMap((pattern) => [
    pattern,
    `!@/${toContextFolder(pattern)}/public-api`,
  ])
}

/**
 * ワークスペースのフォルダー間の参照方向を強制するルールを生成します。
 *
 *   app 層（main.ts 、 App.vue 、 src/business-common/router）
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
 * app 層のルーティング（ src/business-common/router ）は、業務共通のチームが保守するため business-common のフォルダーに置きます。
 * 参照方向はフォルダーではなく層で決まるため、 business-common/router には business-common の制限を適用しません。
 * app 層は最上位の層のため、参照の制限を設けません。
 * business-common のほかのフォルダーは、 app 層のルーティングを参照できません。
 * pages から app 層への参照は、ルート名の定数（ `@/business-common/router/route-names` ）だけを例外として許可します。
 * pages は API クライアントと API の型を参照できません。 API はコンテキストのユースケースコンポーザブルが扱います。
 * pages のテストは、画面を遷移させるためにルーター（ `@/business-common/router` ）を、
 * API の応答を組み立てるために API の型を参照できます。
 * コンテキストから他のコンテキストを参照できるのは、 public-api.ts だけです。
 * コンテキスト、 business-common 、 system-common は、 vue-router を型だけ参照できます。
 * @param workspace ルールを適用するワークスペースのフォルダー名。
 * @param contextPatterns 業務コードを持つ最上位フォルダー（コンテキスト）を表す
 *   `@/` エイリアスのパターン。
 * @returns 参照方向を強制する ESLint の設定の配列。
 */
function createLayerDependencyRules(workspace: string, contextPatterns: string[]): Linter.Config[] {
  const appRouterFileGlob = `**/${workspace}/src/business-common/router/**`
  // `@/business-common/router/**` はフォルダー直下のモジュール（ index.ts ）を指す `@/business-common/router` に一致しないため、別に指定します。
  const appRouterPatterns = ['@/business-common/router', '@/business-common/router/**']
  const pagesFileGlob = `**/${workspace}/src/pages/**/*.{vue,ts,mts,tsx}`
  // `__tests__/**/*.{vue,ts}` の形は、 pages 直下のフォルダーのテスト（ pages/basket/__tests__ など）に一致しないため、
  // `__tests__/**` の形で指定します。
  // `pages/**/__tests__/**` は pages 直下のテスト（ pages/__tests__ ）にも一致しないため、別に指定します。
  const pagesTestFileGlobs = [
    `**/${workspace}/src/pages/__tests__/**`,
    `**/${workspace}/src/pages/**/__tests__/**`,
  ]
  const pagesContextRestriction = {
    group: exceptPublicApi(contextPatterns),
    message:
      'pages はコンテキストの public-api.ts 経由でのみ参照できます。コンテキストの内部フォルダーを直接参照することはできません。',
  }
  // app 層のルーティングのうち、ルート名の定数だけを除外します。
  // ルーター（ @/business-common/router ）は、このパターンに一致しないため、 pages では paths で別に禁止します。
  const appRouterPatternsExceptRouteNames = [
    '@/business-common/router/**',
    '!@/business-common/router/route-names',
  ]
  const pagesAppMessage =
    'pages から参照できる app 層のモジュールは、ルート名の定数（ @/business-common/router/route-names ）だけです。'

  /**
   * コンテキストごとに、他のコンテキストの内部の参照を禁止する設定を作ります。
   * 同じコンテキストの中の参照は制限しません。
   * @param contextPattern 設定を作るコンテキストを表すパターン。
   * @returns ESLint の設定。
   */
  const createContextRule = (contextPattern: string): Linter.Config => {
    const contextFolder = toContextFolder(contextPattern)
    const otherContextPatterns = contextPatterns.filter((pattern) => pattern !== contextPattern)
    return {
      name: `${workspace}/layer-dependency/context/${contextFolder}`,
      files: [`**/${workspace}/src/${contextFolder}/**/*.{vue,ts,mts,tsx}`],
      rules: {
        [restrictedImportsRule]: [
          'error',
          {
            paths: [vueRouterValueRestriction],
            patterns: [
              {
                group: [...appRouterPatterns, '@/pages/**'],
                message:
                  'コンテキストは app 層（ business-common/router ）と pages を参照できません。 app 層と pages はコンテキストより上位の層です。',
              },
              ...(otherContextPatterns.length > 0
                ? [
                    {
                      group: exceptPublicApi(otherContextPatterns),
                      message:
                        '他のコンテキストは public-api.ts 経由でのみ参照できます。コンテキストの内部フォルダーを直接参照することはできません。',
                    },
                  ]
                : []),
            ],
          },
        ],
        ...dynamicImportRestriction,
      },
    }
  }

  return [
    {
      name: `${workspace}/layer-dependency/system-common`,
      files: [`**/${workspace}/src/system-common/**/*.{vue,ts,mts,tsx}`],
      rules: {
        [restrictedImportsRule]: [
          'error',
          {
            paths: [vueRouterValueRestriction],
            patterns: [
              {
                group: ['@/business-common/**', '@/pages/**', ...contextPatterns],
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
      // business-common/router は app 層のため、 business-common の制限を適用しません。
      ignores: [appRouterFileGlob],
      rules: {
        [restrictedImportsRule]: [
          'error',
          {
            paths: [vueRouterValueRestriction],
            patterns: [
              {
                group: [...appRouterPatterns, '@/pages/**', ...contextPatterns],
                message:
                  'business-common は app 層（ business-common/router ）、 pages 、コンテキストを参照できません。',
              },
            ],
          },
        ],
        ...dynamicImportRestriction,
      },
    },
    ...contextPatterns.map(createContextRule),
    {
      name: `${workspace}/layer-dependency/pages`,
      files: [pagesFileGlob],
      ignores: pagesTestFileGlobs,
      rules: {
        [restrictedImportsRule]: [
          'error',
          {
            paths: [{ name: '@/business-common/router', message: pagesAppMessage }],
            patterns: [
              pagesContextRestriction,
              { group: appRouterPatternsExceptRouteNames, message: pagesAppMessage },
              {
                group: [
                  '@/system-common/api-client',
                  '@/system-common/api-client/**',
                  '@/system-common/generated/**',
                ],
                message:
                  'pages は API クライアントと API の型を参照できません。コンテキストのユースケースコンポーザブルを使用します。',
              },
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
        [restrictedImportsRule]: [
          'error',
          {
            patterns: [
              pagesContextRestriction,
              {
                group: appRouterPatternsExceptRouteNames,
                message:
                  'pages のテストから参照できる app 層のモジュールは、ルーター（ @/business-common/router ）とルート名の定数（ @/business-common/router/route-names ）だけです。',
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
