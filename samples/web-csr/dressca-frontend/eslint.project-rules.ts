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
 * consumer プロジェクトのフォルダー間の参照方向を強制するルールです。
 *
 *   アプリケーション（App.vue / main.ts）
 *         ↓
 *   コンテキスト（shopping/*, security/*）
 *         ↓
 *   業務共通（business-common）
 *         ↓
 *   システム共通（system-common）
 *
 * コンテキストの下にはドメインのフォルダーを置き、その下にレイヤーを並べます。
 * App.vue 、 main.ts 、ルーティング定義は全経路を許可する例外です。
 * ルーティング定義の例外が層全体へ広がらないよう、system-common の他のコードからは
 * 集約モジュール（route-names.ts）を参照できないようにします。
 */
export const consumerLayerDependencyRules: Linter.Config[] = [
  {
    name: 'consumer/layer-dependency/system-common',
    files: ['**/consumer/src/system-common/**/*.{vue,ts,mts,tsx}'],
    ignores: [
      '**/consumer/src/system-common/router/index.ts',
      '**/consumer/src/system-common/router/route-names.ts',
    ],
    rules: {
      'no-restricted-imports': [
        'error',
        {
          patterns: [
            {
              group: ['@/business-common/**', '@/shopping/**', '@/security/**'],
              message:
                'system-common は業務知識を持たない層です。business-common やコンテキストを参照できません。',
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
    name: 'consumer/layer-dependency/business-common',
    files: ['**/consumer/src/business-common/**/*.{vue,ts,mts,tsx}'],
    rules: {
      'no-restricted-imports': [
        'error',
        {
          patterns: [
            {
              group: ['@/shopping/**', '@/security/**'],
              message: 'business-common はコンテキストを参照できません。',
            },
          ],
        },
      ],
    },
  },
]
