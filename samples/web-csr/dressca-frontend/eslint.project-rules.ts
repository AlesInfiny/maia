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
 * ワークスペースのフォルダー間の参照方向を強制するルールを生成します。
 *
 *   pages（src/pages。 App.vue / main.ts と同じ最上位層）
 *         ↓
 *   コンテキスト
 *         ↓
 *   業務共通（business-common）
 *         ↓
 *   システム共通（system-common）
 *
 * コンテキストの下にはドメインのフォルダーを置き、その下にレイヤーを並べます。
 * App.vue 、 main.ts 、ルーティング定義は全経路を許可する例外です。
 * ルーティング定義の例外が層全体へ広がらないよう、system-common の他のコードからは
 * 集約モジュール（route-names.ts）を参照できないようにします。
 * pages はどの層からも参照されない最上位層のため、例外なく全レイヤーからの参照を禁止します。
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

  return [
    {
      name: `${workspace}/layer-dependency/system-common`,
      files: [`**/${workspace}/src/system-common/**/*.{vue,ts,mts,tsx}`],
      ignores: [
        `**/${workspace}/src/system-common/router/index.ts`,
        `**/${workspace}/src/system-common/router/route-names.ts`,
      ],
      rules: {
        'no-restricted-imports': [
          'error',
          {
            patterns: [
              {
                group: ['@/business-common/**', '@/pages/**', ...contextPatterns],
                message:
                  'system-common は業務知識を持たない層です。business-common や pages 、コンテキストを参照できません。',
              },
              {
                group: ['@/system-common/router/route-names'],
                message:
                  'ルート名の集約モジュールは全コンテキストを参照します。ルーティング定義以外の system-common のコードからは参照できません。',
              },
            ],
          },
        ],
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
                group: ['@/pages/**', ...contextPatterns],
                message: 'business-common は pages やコンテキストを参照できません。',
              },
            ],
          },
        ],
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
                group: ['@/pages/**'],
                message: 'コンテキストは pages を参照できません。pages はコンテキストより上位の層です。',
              },
            ],
          },
        ],
      },
    },
    {
      name: `${workspace}/layer-dependency/pages`,
      files: [`**/${workspace}/src/pages/**/*.{vue,ts,mts,tsx}`],
      rules: {
        'no-restricted-imports': [
          'error',
          {
            patterns: [
              {
                group: contextPatternsExceptPublicApi,
                message:
                  'pages はコンテキストの public-api.ts 経由でのみ参照できます。コンテキストの内部フォルダーを直接参照することはできません。',
              },
            ],
          },
        ],
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
  '@/assets-management/**',
  '@/catalog-management/**',
  '@/security/**',
])
