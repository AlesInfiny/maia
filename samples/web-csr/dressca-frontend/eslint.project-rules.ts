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
 * core 層以外での動的インポート（ `import()` ）を禁止するルールです。
 * 画面を動的インポートするのは core 層のルート表だけです。
 * 動的インポートは no-restricted-imports の検査の対象外のため、参照方向のルールを迂回できないようにします。
 */
const dynamicImportRestriction: Linter.RulesRecord = {
  'no-restricted-syntax': [
    'error',
    {
      selector: 'ImportExpression',
      message:
        '動的インポートは core 層だけで使用できます。画面の遅延読み込みはルート表で定義します。',
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
 * 画面を遷移させられるのは pages 層と core 層だけです。
 */
const vueRouterValueRestriction = {
  name: 'vue-router',
  message:
    '画面を遷移させられるのは pages 層と core 層だけです。 vue-router は型だけ参照できます。遷移先が必要な場合は、遷移先を引数で受け取ります。',
  allowTypeImports: true,
}

/**
 * コンテキストではない src 直下のフォルダー（層）です。
 * src 直下のフォルダーのうち、ここに挙げないものは、すべてコンテキストとして扱います。
 * コンテキストを追加するときに、このファイルを変更する必要はありません。
 * 層を追加するときは、ここに追加し、その層の参照方向のルールを定義します。
 */
const layerFolders = ['core', 'pages', 'business-common', 'system-common', 'assets']

/**
 * `@/` エイリアスで書いた参照のうち、指定したフォルダー以外への参照に一致する正規表現を作ります。
 * テストが参照するモックのデータ（ `@/../mock/**` ）は src の外にあるため、一致させません。
 * gitignore 形式のパターン（ `group` ）では `..` を含むパスを除外できないため、正規表現を使います。
 * @param allowedFolders 参照を許可する src 直下のフォルダー。
 * @returns 正規表現の文字列。
 */
function importsExcept(allowedFolders: string[]): string {
  // 文字列の値を `\.\.` にするため、ソースでは `\\.\\.` と書きます。
  return `^@/(?!(?:${[...allowedFolders, '\\.\\.'].join('|')})/)`
}

/**
 * ワークスペースのフォルダー間の参照方向を強制するルールを生成します。
 *
 *   core 層（main.ts 、 App.vue 、 src/core）
 *         ↓
 *   pages（src/pages）
 *         ↓
 *   コンテキスト（src 直下の、層以外のすべてのフォルダー）
 *         ↓
 *   業務共通（business-common）
 *         ↓
 *   システム共通（system-common）
 *
 * 各層には、参照してよいフォルダーだけを定義し、それ以外の `@/` の参照を禁止します。
 * コンテキストを列挙しないため、コンテキストを追加してもルールを変更する必要はありません。
 * 層として登録していないフォルダーはコンテキストとして扱われ、最も厳しいルールが適用されます。
 *
 * コンテキストの下にはドメインのフォルダーを置き、その下にレイヤーを並べます。
 * core 層は最上位の層のため、参照の制限を設けません。
 * pages から core 層への参照は、ルート名の定数（ `@/core/router/route-names` ）だけを例外として許可します。
 * pages は API クライアントと API の型を参照できません。 API はコンテキストのユースケースコンポーザブルが扱います。
 * pages のテストは、画面を遷移させるためにルーター（ `@/core/router` ）を、
 * API の応答を組み立てるために API の型を参照できます。
 * コンテキストは他のコンテキストを参照できません。 public-api.ts も参照できません。コンテキストの組み合わせは pages で行います。
 * コンテキストの中の参照は相対パスで書きます。 `@/` で自分のコンテキストを参照することもできません。
 * コンテキスト、 business-common 、 system-common は、 vue-router を型だけ参照できます。
 * @param workspace ルールを適用するワークスペースのフォルダー名。
 * @returns 参照方向を強制する ESLint の設定の配列。
 */
function createLayerDependencyRules(workspace: string): Linter.Config[] {
  const srcGlob = `**/${workspace}/src`
  const sourceFileGlob = '*.{vue,ts,mts,tsx}'
  // `__tests__/**/*.{vue,ts}` の形は、 pages 直下のフォルダーのテスト（ pages/basket/__tests__ など）に一致しないため、
  // `__tests__/**` の形で指定します。
  // `pages/**/__tests__/**` は pages 直下のテスト（ pages/__tests__ ）にも一致しないため、別に指定します。
  const pagesTestFileGlobs = [`${srcGlob}/pages/__tests__/**`, `${srcGlob}/pages/**/__tests__/**`]
  const pagesContextRestriction = {
    // 層以外のフォルダー（コンテキスト）のうち、 public-api 以外への参照に一致します。
    regex: `${importsExcept(layerFolders)}[^/]+/(?!public-api$)`,
    message:
      'pages はコンテキストの public-api.ts 経由でのみ参照できます。コンテキストの内部フォルダーを直接参照することはできません。',
  }
  // core 層のルーティングのうち、ルート名の定数だけを除外します。
  // ルーター（ @/core/router ）は、このパターンに一致しないため、 pages では paths で別に禁止します。
  const coreRouterPatternsExceptRouteNames = ['@/core/router/**', '!@/core/router/route-names']
  const pagesCoreMessage =
    'pages から参照できる core 層のモジュールは、ルート名の定数（ @/core/router/route-names ）だけです。'

  return [
    {
      name: `${workspace}/layer-dependency/system-common`,
      files: [`${srcGlob}/system-common/**/${sourceFileGlob}`],
      rules: {
        [restrictedImportsRule]: [
          'error',
          {
            paths: [vueRouterValueRestriction],
            patterns: [
              {
                regex: importsExcept(['system-common']),
                message:
                  'system-common は業務知識を持たない層です。 core 層、 business-common 、 pages 、コンテキストを参照できません。',
              },
            ],
          },
        ],
        ...dynamicImportRestriction,
      },
    },
    {
      name: `${workspace}/layer-dependency/business-common`,
      files: [`${srcGlob}/business-common/**/${sourceFileGlob}`],
      rules: {
        [restrictedImportsRule]: [
          'error',
          {
            paths: [vueRouterValueRestriction],
            patterns: [
              {
                regex: importsExcept(['business-common', 'system-common']),
                message: 'business-common は core 層、 pages 、コンテキストを参照できません。',
              },
            ],
          },
        ],
        ...dynamicImportRestriction,
      },
    },
    {
      name: `${workspace}/layer-dependency/context`,
      files: [`${srcGlob}/*/**/${sourceFileGlob}`],
      ignores: layerFolders.map((folder) => `${srcGlob}/${folder}/**`),
      rules: {
        [restrictedImportsRule]: [
          'error',
          {
            paths: [vueRouterValueRestriction],
            patterns: [
              {
                regex: importsExcept(['business-common', 'system-common']),
                message:
                  'コンテキストは core 層、 pages 、他のコンテキストを参照できません。コンテキストの組み合わせは pages で行います。自分のコンテキストの中は相対パスで参照します。',
              },
            ],
          },
        ],
        ...dynamicImportRestriction,
      },
    },
    {
      name: `${workspace}/layer-dependency/pages`,
      files: [`${srcGlob}/pages/**/${sourceFileGlob}`],
      ignores: pagesTestFileGlobs,
      rules: {
        [restrictedImportsRule]: [
          'error',
          {
            paths: [{ name: '@/core/router', message: pagesCoreMessage }],
            patterns: [
              pagesContextRestriction,
              { group: coreRouterPatternsExceptRouteNames, message: pagesCoreMessage },
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
                group: coreRouterPatternsExceptRouteNames,
                message:
                  'pages のテストから参照できる core 層のモジュールは、ルーター（ @/core/router ）とルート名の定数（ @/core/router/route-names ）だけです。',
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
export const consumerLayerDependencyRules: Linter.Config[] = createLayerDependencyRules('consumer')

/**
 * admin のフォルダー間の参照方向を強制するルールです。
 */
export const adminLayerDependencyRules: Linter.Config[] = createLayerDependencyRules('admin')
