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
 * vue-router の型だけを許可するため（ `allowTypeImports` ）、 typescript-eslint のルールを使用します。
 */
const restrictedImportsRule = '@typescript-eslint/no-restricted-imports'

const vueRouterValueRestriction = {
  name: 'vue-router',
  message:
    '画面を遷移させられるのは pages 層と core 層だけです。 vue-router は型だけ参照できます。遷移先が必要な場合は、遷移先を引数で受け取ります。',
  allowTypeImports: true,
}

const servicesVueMessage =
  'services には Vue に依存しない関数だけを置きます。 Vue 、ストア、ルーターなどに依存する処理は composables に置きます。'
const servicesVueRestrictions = {
  paths: ['vue', 'pinia', 'vue-router'].map((name) => ({ name, message: servicesVueMessage })),
  patterns: [
    { group: ['@vueuse/*', 'vee-validate', '@vee-validate/*'], message: servicesVueMessage },
    { regex: '(?:^|/)(?:stores|composables)(?:/|$)', message: servicesVueMessage },
  ],
}

/**
 * 参照方向のルールの設定に、 services フォルダーだけに適用する設定を加えます。
 * 同じルールを後の設定で指定すると、オプションは上書きされるため、
 * 層の参照方向の制限に、 Vue に依存するモジュールの参照の禁止を加えた設定を作ります。
 * @param config 層の参照方向のルールの設定。
 * @param layerGlob 層のフォルダーに一致する glob 。
 * @param sourceFileGlob ソースファイルの名前に一致する glob 。
 * @returns 層の設定と、その層の services フォルダーの設定。
 */
function withServicesRestriction(
  config: Linter.Config,
  layerGlob: string,
  sourceFileGlob: string,
): Linter.Config[] {
  const [severity, options] = config.rules?.[restrictedImportsRule] as [
    Linter.RuleSeverity,
    { paths: { name: string }[]; patterns: object[] },
  ]
  return [
    config,
    {
      name: `${config.name}/services`,
      // `<層>/**/services/**/<ファイル>` の 1 つの glob では、 minimatch が途中の `**` が 0 階層のパス
      // （ <層>/services/x.ts など）に一致しないため、 `**` が 0 階層の場合を別の glob で指定します。
      files: [
        `${layerGlob}/services/**/${sourceFileGlob}`,
        `${layerGlob}/**/services/${sourceFileGlob}`,
        `${layerGlob}/**/services/**/${sourceFileGlob}`,
      ],
      ...(config.ignores ? { ignores: config.ignores } : {}),
      rules: {
        ...config.rules,
        [restrictedImportsRule]: [
          severity,
          {
            paths: [
              ...options.paths.filter((path) => path.name !== 'vue-router'),
              ...servicesVueRestrictions.paths,
            ],
            patterns: [...options.patterns, ...servicesVueRestrictions.patterns],
          },
        ],
      },
    },
  ]
}

const layerFolders = ['core', 'pages', 'business-common', 'system-common', 'assets']

const mockDataParentFolderPattern = '\\.\\.'

/**
 * `@/` エイリアスで書いた参照のうち、指定したフォルダー以外への参照に一致する正規表現を作ります。
 * gitignore 形式のパターン（ `group` ）では `..` を含むパスを除外できないため、正規表現を使います。
 * @param allowedFolders 参照を許可する src 直下のフォルダー。
 * @returns 正規表現の文字列。
 */
function importsExcept(allowedFolders: string[]): string {
  return `^@/(?!(?:${[...allowedFolders, mockDataParentFolderPattern].join('|')})/)`
}

/**
 * ワークスペースのフォルダー間の参照方向を強制するルールを生成します。
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
    regex: `${importsExcept(layerFolders)}[^/]+/(?!public-api$)`,
    message:
      'pages はコンテキストの public-api.ts 経由でのみ参照できます。コンテキストの内部フォルダーを直接参照することはできません。',
  }
  // ルーター（ @/core/router ）は、このパターンに一致しないため、 pages では paths で別に禁止します。
  const coreRouterPatternsExceptRouteNames = ['@/core/router/**', '!@/core/router/route-names']
  const pagesCoreMessage =
    'pages から参照できる core 層のモジュールは、ルート名の定数（ @/core/router/route-names ）だけです。'

  return [
    ...withServicesRestriction(
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
      `${srcGlob}/system-common`,
      sourceFileGlob,
    ),
    ...withServicesRestriction(
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
      `${srcGlob}/business-common`,
      sourceFileGlob,
    ),
    ...withServicesRestriction(
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
      `${srcGlob}/*`,
      sourceFileGlob,
    ),
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
