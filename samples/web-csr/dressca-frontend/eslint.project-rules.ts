import type { Linter } from 'eslint'

/**
 * コーディング規約に沿わせるため、全ワークスペースに適用するルールです。
 * 必要に応じて対象のファイルやルールを設定します。
 */
export const codingConventionRules: Linter.Config = {
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
}

/**
 * プロジェクトのフォルダー間の参照方向を強制するルールを生成します。
 *
 *   アプリケーションシェル（App.vue / main.ts）
 *         ↓
 *   コンテキスト
 *         ↓
 *   business-common
 *         ↓
 *   system-common
 *
 * 逆方向の参照を禁止します。
 * コンテキストの下にはドメインのフォルダーを置き、その下にレイヤーを並べます。
 * 同じ階層にあるコンテキスト同士の参照は禁止しません。
 * アプリケーションシェルは全経路を許可する例外です。
 * ルーティング定義（system-common/router/index.ts, route-names.ts）は
 * 全ドメインを集約する役割を持つため、同じく例外として扱います。
 * この例外が層全体へ広がらないよう、system-common の他のコードからは
 * 集約モジュール（route-names.ts）を参照できないようにします。
 * なお本ルールは `@/` エイリアスによる参照を対象とします。
 * レイヤーをまたぐ参照はエイリアスで記述してください。
 * @param project ルールを適用するワークスペースのフォルダー名。
 * @param contextPatterns 業務コードを持つ最上位フォルダー（コンテキスト）を表す
 *   `@/` エイリアスのパターン。
 * @returns 参照方向を強制する ESLint の設定の配列。
 */
function createLayerDependencyRules(project: string, contextPatterns: string[]): Linter.Config[] {
  return [
    {
      name: `${project}/layer-dependency/system-common`,
      files: [`**/${project}/src/system-common/**/*.{vue,ts,mts,tsx}`],
      ignores: [
        `**/${project}/src/system-common/router/index.ts`,
        `**/${project}/src/system-common/router/route-names.ts`,
      ],
      rules: {
        'no-restricted-imports': [
          'error',
          {
            patterns: [
              {
                group: ['@/business-common/**', ...contextPatterns],
                message:
                  'system-common は業務知識を持たない層です。business-common やドメインを参照できません。',
              },
              {
                group: ['@/system-common/router/route-names'],
                message:
                  'ルート名の集約モジュールは全ドメインを参照します。ルーティング定義以外の system-common のコードからは参照できません。',
              },
            ],
          },
        ],
      },
    },
    {
      name: `${project}/layer-dependency/business-common`,
      files: [`**/${project}/src/business-common/**/*.{vue,ts,mts,tsx}`],
      rules: {
        'no-restricted-imports': [
          'error',
          {
            patterns: [
              {
                group: contextPatterns,
                message: 'business-common はドメインを参照できません。',
              },
            ],
          },
        ],
      },
    },
  ]
}

/**
 * consumer プロジェクトのフォルダー間の参照方向を強制するルールです。
 * コンテキストは `shopping`（display-item / basket / ordering）と
 * `authentication`（login）です。
 */
export const consumerLayerDependencyRules: Linter.Config[] = createLayerDependencyRules(
  'consumer',
  ['@/shopping/**', '@/authentication/**'],
)

/**
 * admin プロジェクトのフォルダー間の参照方向を強制するルールです。
 *
 * 最上位のコンテキストは、バックエンドのアプリケーションモジュール
 * （Spring Modulith の `@ApplicationModule`）の定義に合わせています。
 *
 * - `catalog-management` … カタログ管理コンテキスト
 * - `assets-management` … アセット管理コンテキスト
 * - `authorization` … 認可コンテキスト
 * - `authentication` … ログイン・ログアウトの手続き。対応するバックエンドの
 *   コンテキストはなく、認可コンテキストの利用側にあたります。
 *
 * consumer と同じく、同じ階層にあるコンテキスト同士の参照は本ルールでは禁止しません。
 * バックエンドでも、カタログ管理コンテキストから認可コンテキストへの依存が
 * `allowedDependencies` で許可されています。
 */
export const adminLayerDependencyRules: Linter.Config[] = createLayerDependencyRules('admin', [
  '@/assets-management/**',
  '@/authentication/**',
  '@/authorization/**',
  '@/catalog-management/**',
])
