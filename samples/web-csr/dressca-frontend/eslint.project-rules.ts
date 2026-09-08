import type { Linter } from 'eslint'

/**
 * プロジェクトやワークスペースに固有のルールです。
 * 必要に応じて対象のファイルやルールを設定します。
 */
export const additionalRules: Linter.Config = {
  name: 'dressca-frontend/additional-rules',
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
 * consumer プロジェクトのフォルダー間の参照方向を強制するルールです。
 *
 *   アプリケーションシェル（App.vue / main.ts）
 *         ↓
 *   ドメイン（shopping/*, authentication）
 *         ↓
 *   business-common
 *         ↓
 *   system-common
 *
 * 逆方向の参照を禁止します。
 * 同じ階層にあるドメイン同士の参照は禁止しません。
 * アプリケーションシェルは全経路を許可する例外です。
 * ルーティング定義（system-common/router/index.ts, route-names.ts）は
 * 全ドメインを集約する役割を持つため、同じく例外として扱います。
 * なお本ルールは `@/` エイリアスによる参照を対象とします。
 * レイヤーをまたぐ参照はエイリアスで記述してください。
 */
export const layerDependencyRules: Linter.Config[] = [
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
              group: ['@/business-common/**', '@/shopping/**', '@/authentication/**'],
              message:
                'system-common は業務知識を持たない層です。business-common やドメインを参照できません。',
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
              group: ['@/shopping/**', '@/authentication/**'],
              message: 'business-common はドメインを参照できません。',
            },
          ],
        },
      ],
    },
  },
]
