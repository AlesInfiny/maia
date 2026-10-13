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
 * root 層以外での動的インポート（ `import()` ）を禁止するルールです。
 * 画面を動的インポートするのは root 層のルート表だけです。
 * 動的インポートは no-restricted-imports の検査の対象外のため、参照方向のルールを迂回できないようにします。
 */
const dynamicImportRestriction: Linter.RulesRecord = {
  'no-restricted-syntax': [
    'error',
    {
      selector: 'ImportExpression',
      message:
        '動的インポートは root 層だけで使用できます。画面の遅延読み込みはルート表で定義します。',
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
 * 画面を遷移させられるのは pages 層と root 層だけです。
 */
const vueRouterValueRestriction = {
  name: 'vue-router',
  message:
    '画面を遷移させられるのは pages 層と root 層だけです。 vue-router は型だけ参照できます。遷移先が必要な場合は、遷移先を引数で受け取ります。',
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
 *   root 層（main.ts 、 App.vue 、 src/root）
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
 * root 層は最上位の層のため、参照の制限を設けません。
 * pages から root 層への参照は、ルート名の定数（ `@/root/router/route-names` ）だけを例外として許可します。
 * pages は API クライアントと API の型を参照できません。 API はコンテキストのユースケースコンポーザブルが扱います。
 * pages のテストは、画面を遷移させるためにルーター（ `@/root/router` ）を、
 * API の応答を組み立てるために API の型を参照できます。
 * コンテキストから他のコンテキストを参照できるのは、 public-api.ts だけです。
 * コンテキスト、 business-common 、 system-common は、 vue-router を型だけ参照できます。
 * @param workspace ルールを適用するワークスペースのフォルダー名。
 * @param contextPatterns 業務コードを持つ最上位フォルダー（コンテキスト）を表す
 *   `@/` エイリアスのパターン。
 * @returns 参照方向を強制する ESLint の設定の配列。
 */
function createLayerDependencyRules(workspace: string, contextPatterns: string[]): Linter.Config[] {
  // `@/root/**` は、ルーター（ `@/root/router` ）を含む root 層のすべてのモジュールに一致します。
  const rootPatterns = ['@/root/**']
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
  // root 層のルーティングのうち、ルート名の定数だけを除外します。
  // ルーター（ @/root/router ）は、このパターンに一致しないため、 pages では paths で別に禁止します。
  const rootRouterPatternsExceptRouteNames = ['@/root/router/**', '!@/root/router/route-names']
  const pagesRootMessage =
    'pages から参照できる root 層のモジュールは、ルート名の定数（ @/root/router/route-names ）だけです。'

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
                group: [...rootPatterns, '@/pages/**'],
                message:
                  'コンテキストは root 層と pages を参照できません。 root 層と pages はコンテキストより上位の層です。',
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
                group: [...rootPatterns, '@/business-common/**', '@/pages/**', ...contextPatterns],
                message:
                  'system-common は業務知識を持たない層です。 root 層、 business-common 、 pages 、コンテキストを参照できません。',
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
        [restrictedImportsRule]: [
          'error',
          {
            paths: [vueRouterValueRestriction],
            patterns: [
              {
                group: [...rootPatterns, '@/pages/**', ...contextPatterns],
                message: 'business-common は root 層、 pages 、コンテキストを参照できません。',
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
            paths: [{ name: '@/root/router', message: pagesRootMessage }],
            patterns: [
              pagesContextRestriction,
              { group: rootRouterPatternsExceptRouteNames, message: pagesRootMessage },
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
                group: rootRouterPatternsExceptRouteNames,
                message:
                  'pages のテストから参照できる root 層のモジュールは、ルーター（ @/root/router ）とルート名の定数（ @/root/router/route-names ）だけです。',
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
