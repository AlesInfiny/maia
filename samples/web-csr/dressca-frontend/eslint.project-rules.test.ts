/**
 * eslint.project-rules.ts の参照方向のルールの回帰テストです。
 * 次の 2 つを分けて確かめます。
 *
 * - 設定の割り当て: どのファイルに、どの名前の設定が適用されるか。
 *   ファイルの深さやフォルダー名の違いによる glob の一致は、ここで確かめます。
 * - 参照の判定: 層ごとに代表のファイルを 1 つ選び、参照先ごとにエラーになるか通るかを確かめます。
 *   エラーになる参照は、どの制限で拒否されたかをメッセージで確かめます。
 *
 * 禁止する参照は正規表現の文字列で書いているため、エスケープの誤りなどをこのテストで検出します。
 * たとえば `\\.\\.` を `..` と書くと任意の 2 文字に一致し、 2 文字のフォルダー（ `@/ab/**` ）への参照を見逃します。
 *
 * 実行方法: npm run test:root
 */
import { describe, test } from 'node:test'
import assert from 'node:assert/strict'
import { ESLint, type Linter } from 'eslint'
import tseslint from 'typescript-eslint'
import { adminLayerDependencyRules, consumerLayerDependencyRules } from './eslint.project-rules.ts'

const restrictedImportsRule = '@typescript-eslint/no-restricted-imports'
const restrictedSyntaxRule = 'no-restricted-syntax'

const projectRules = [...consumerLayerDependencyRules, ...adminLayerDependencyRules]

const eslint = new ESLint({
  cwd: import.meta.dirname,
  // eslint.config.ts の型情報を使用するルールは、存在しないファイルを検査できないため読み込みません。
  // 参照方向のルールの評価に必要なパーサーとプラグインだけを設定します。
  overrideConfigFile: true,
  overrideConfig: [
    // .vue ファイルの設定を計算できるよう（ eslint.config.ts のテストで使用します）、プラグインはファイルを限定せずに登録します。
    { plugins: { '@typescript-eslint': tseslint.plugin } },
    { files: ['**/*.ts'], languageOptions: { parser: tseslint.parser } },
    ...projectRules,
  ] satisfies Linter.Config[],
})

/**
 * 参照を拒否した制限を識別する、メッセージの一部です。
 */
const denied = {
  context: 'コンテキストは core 層、 pages 、他のコンテキストを参照できません。',
  businessCommon: 'business-common は core 層、 pages 、コンテキストを参照できません。',
  systemCommon: 'system-common は業務知識を持たない層です。',
  pagesContext: 'pages はコンテキストの public-api.ts 経由でのみ参照できます。',
  pagesCore: 'pages から参照できる core 層のモジュールは、',
  pagesApi: 'pages は API クライアントと API の型を参照できません。',
  pagesTestCore: 'pages のテストから参照できる core 層のモジュールは、',
  vueRouterValue: 'vue-router は型だけ参照できます。',
  services: 'services には Vue に依存しない関数だけを置きます。',
  dynamicImport: '動的インポートは core 層だけで使用できます。',
} as const

/**
 * 参照を検査したときに期待する結果です。
 * `'ok'` はエラーにならないこと、それ以外は拒否した制限のメッセージの一部です。
 */
type Expected = 'ok' | (typeof denied)[keyof typeof denied]

/**
 * コードを検査し、期待する結果になることを確かめます。
 * @param filePath 検査するファイルの、 dressca-frontend からの相対パス。ファイルが存在する必要はありません。
 * @param code 検査するコード。
 * @param expected 期待する結果。
 */
async function assertLint(filePath: string, code: string, expected: Expected): Promise<void> {
  const [result] = await eslint.lintText(code, { filePath, warnIgnored: true })
  assert.ok(result, `${filePath} の検査結果がありません。`)
  const messages = result.messages.map((m) => `${m.ruleId}: ${m.message}`)
  if (expected === 'ok') {
    assert.deepEqual(messages, [], `${filePath} の \`${code}\` はエラーにならないはずです。`)
    return
  }
  const ruleId = expected === denied.dynamicImport ? restrictedSyntaxRule : restrictedImportsRule
  assert.ok(
    result.messages.some(
      (m) => m.ruleId === ruleId && m.severity === 2 && m.message.includes(expected),
    ),
    `${filePath} の \`${code}\` は「${expected}」で拒否されるはずです。実際: ${JSON.stringify(messages)}`,
  )
}

type ImportCase = [specifier: string, expected: Expected]

/**
 * 参照元のファイルから、参照先ごとに import 文を検査するテストを定義します。
 * @param filePath 参照元のファイル。
 * @param cases 参照先のモジュールと期待する結果の組。
 */
function defineImportCases(filePath: string, cases: ImportCase[]): void {
  describe(filePath, () => {
    for (const [specifier, expected] of cases) {
      test(`${specifier} は${expected === 'ok' ? '通る' : '拒否される'}`, async () => {
        await assertLint(filePath, `import { x } from '${specifier}'\nexport { x }\n`, expected)
      })
    }
  })
}

describe('設定の割り当て', () => {
  /**
   * 名前で設定を探し、参照方向のルールの設定を返します。
   * @param name 設定の名前。
   * @returns ルールの名前と設定の組。
   */
  function rulesOf(name: string): NonNullable<Linter.Config['rules']> {
    const config = projectRules.find((c) => c.name === name)
    assert.ok(config?.rules, `${name} という名前の設定がありません。`)
    return config.rules
  }

  // 参照元のファイルと、適用されるべき設定の名前の組です。
  // null は、参照方向のルールが適用されないことを表します。
  const assignments: [filePath: string, configName: string | null][] = [
    // コンテキスト。層として登録していないフォルダー（ 2 文字の名前を含む）はコンテキストとして扱います。
    [
      'consumer/src/shopping/catalog/application/use-catalog.ts',
      'consumer/layer-dependency/context',
    ],
    ['consumer/src/ab/domain/x.ts', 'consumer/layer-dependency/context'],
    ['consumer/src/brand-new-context/x.ts', 'consumer/layer-dependency/context'],
    ['consumer/src/ab/ui/X.vue', 'consumer/layer-dependency/context'],
    ['admin/src/ab/domain/x.ts', 'admin/layer-dependency/context'],
    // pages と pages のテスト
    ['consumer/src/pages/use-page.ts', 'consumer/layer-dependency/pages'],
    ['consumer/src/pages/basket/BasketPage.vue', 'consumer/layer-dependency/pages'],
    ['consumer/src/pages/__tests__/HomePage.spec.ts', 'consumer/layer-dependency/pages-test'],
    [
      'consumer/src/pages/basket/__tests__/BasketPage.spec.ts',
      'consumer/layer-dependency/pages-test',
    ],
    ['admin/src/pages/catalog/use-catalog-page.ts', 'admin/layer-dependency/pages'],
    // 共通の層
    ['consumer/src/business-common/x.ts', 'consumer/layer-dependency/business-common'],
    ['consumer/src/system-common/x.ts', 'consumer/layer-dependency/system-common'],
    // services フォルダー。フォルダーの深さによらず services の設定が適用されます。
    ['consumer/src/ab/services/x.ts', 'consumer/layer-dependency/context/services'],
    ['consumer/src/ab/domain/services/x.ts', 'consumer/layer-dependency/context/services'],
    ['consumer/src/ab/services/sub/x.ts', 'consumer/layer-dependency/context/services'],
    ['consumer/src/ab/domain/services/sub/x.ts', 'consumer/layer-dependency/context/services'],
    [
      'consumer/src/business-common/services/x.ts',
      'consumer/layer-dependency/business-common/services',
    ],
    [
      'consumer/src/business-common/a/services/b/x.ts',
      'consumer/layer-dependency/business-common/services',
    ],
    [
      'consumer/src/system-common/services/sub/x.ts',
      'consumer/layer-dependency/system-common/services',
    ],
    [
      'consumer/src/system-common/api/services/x.ts',
      'consumer/layer-dependency/system-common/services',
    ],
    [
      'admin/src/catalog-management/catalog/services/catalog-service.ts',
      'admin/layer-dependency/context/services',
    ],
    // コンテキストの services の設定は、層のフォルダー（ pages 、 core ）の services に適用しません。
    ['consumer/src/pages/basket/services/x.ts', 'consumer/layer-dependency/pages'],
    ['consumer/src/core/services/x.ts', null],
    // core 層には参照の制限を設けません。
    ['consumer/src/core/router/index.ts', null],
    ['consumer/src/main.ts', null],
  ]

  for (const [filePath, configName] of assignments) {
    test(`${filePath} には ${configName ?? '参照方向のルールが適用されない'}`, async () => {
      const actual = ((await eslint.calculateConfigForFile(filePath)) as Linter.Config).rules ?? {}
      if (configName === null) {
        assert.equal(actual[restrictedImportsRule], undefined)
        assert.equal(actual[restrictedSyntaxRule], undefined)
        return
      }
      const expected = rulesOf(configName)
      for (const ruleId of [restrictedImportsRule, restrictedSyntaxRule]) {
        // 計算した設定では重大度が数値になるため、オプションだけを比べます。
        const [severity, ...options] = actual[ruleId] as [number, ...unknown[]]
        const [, ...expectedOptions] = expected[ruleId] as [string, ...unknown[]]
        assert.equal(severity, 2, `${filePath} の ${ruleId} がエラーになっていません。`)
        assert.deepEqual(
          options,
          expectedOptions,
          `${filePath} の ${ruleId} の設定が一致しません。`,
        )
      }
    })
  }
})

describe('参照の判定', () => {
  describe('コンテキスト', () => {
    defineImportCases('consumer/src/shopping/catalog/application/use-catalog.ts', [
      // 他のコンテキスト（ 2 文字のフォルダー名を含む）と、自分のコンテキストへの `@/` の参照
      ['@/security/public-api', denied.context],
      ['@/security/authentication/application/use-login', denied.context],
      ['@/ab/public-api', denied.context],
      ['@/ab/domain/x', denied.context],
      ['@/brand-new-context/public-api', denied.context],
      ['@/shopping/public-api', denied.context],
      ['@/shopping/catalog/domain/x', denied.context],
      // 層の名前で始まる、層ではないフォルダー
      ['@/system-commons/x', denied.context],
      ['@/business-common-extra/x', denied.context],
      // モックのデータの例外（ `@/../` ）が、ドットで始まる他のフォルダーに広がらないこと
      ['@/.a/x', denied.context],
      ['@/./x', denied.context],
      // core 層と pages
      ['@/core/router', denied.context],
      ['@/core/router/route-names', denied.context],
      ['@/pages/basket/BasketPage.vue', denied.context],
      // vue-router の値
      ['vue-router', denied.vueRouterValue],
      // 相対パス、業務共通、システム共通、モックのデータ
      ['./x', 'ok'],
      ['../domain/x', 'ok'],
      ['@/business-common/x', 'ok'],
      ['@/system-common/x', 'ok'],
      ['@/system-common/generated/api-client', 'ok'],
      ['@/../mock/x', 'ok'],
      ['vue', 'ok'],
    ])

    test('vue-router の型は参照できる', async () => {
      await assertLint(
        'consumer/src/shopping/catalog/application/use-catalog.ts',
        "import type { Router } from 'vue-router'\nexport type { Router }\n",
        'ok',
      )
    })
  })

  describe('pages', () => {
    defineImportCases('consumer/src/pages/basket/use-basket-page.ts', [
      // コンテキストは public-api だけを参照できる
      ['@/shopping/public-api', 'ok'],
      ['@/ab/public-api', 'ok'],
      ['@/shopping/catalog/application/use-catalog', denied.pagesContext],
      ['@/shopping/public-api/x', denied.pagesContext],
      ['@/ab/public-api-internal', denied.pagesContext],
      // core 層はルート名の定数だけを参照できる
      ['@/core/router', denied.pagesCore],
      ['@/core/router/index', denied.pagesCore],
      ['@/core/router/route-names', 'ok'],
      // API クライアントと API の型
      ['@/system-common/api-client', denied.pagesApi],
      ['@/system-common/api-client/x', denied.pagesApi],
      ['@/system-common/generated/api-client', denied.pagesApi],
      ['@/system-common/x', 'ok'],
      ['@/business-common/x', 'ok'],
      ['@/pages/catalog/x', 'ok'],
      ['vue-router', 'ok'],
    ])
  })

  describe('pages のテスト', () => {
    defineImportCases('consumer/src/pages/basket/__tests__/BasketPage.spec.ts', [
      ['@/core/router', 'ok'],
      ['@/core/router/route-names', 'ok'],
      ['@/system-common/generated/api-client', 'ok'],
      ['@/shopping/public-api', 'ok'],
      ['@/core/router/index', denied.pagesTestCore],
      ['@/shopping/catalog/application/use-catalog', denied.pagesContext],
    ])
  })

  describe('business-common', () => {
    defineImportCases('consumer/src/business-common/x.ts', [
      ['@/core/router/route-names', denied.businessCommon],
      ['@/pages/basket/BasketPage.vue', denied.businessCommon],
      ['@/shopping/public-api', denied.businessCommon],
      ['@/ab/public-api', denied.businessCommon],
      ['vue-router', denied.vueRouterValue],
      ['@/business-common/y', 'ok'],
      ['@/system-common/x', 'ok'],
      ['@/../mock/x', 'ok'],
    ])
  })

  describe('system-common', () => {
    defineImportCases('consumer/src/system-common/x.ts', [
      ['@/core/router', denied.systemCommon],
      ['@/pages/basket/BasketPage.vue', denied.systemCommon],
      ['@/ab/public-api', denied.systemCommon],
      ['@/business-common/x', denied.systemCommon],
      ['vue-router', denied.vueRouterValue],
      ['@/system-common/y', 'ok'],
      ['@/../mock/x', 'ok'],
    ])
  })

  describe('core 層', () => {
    defineImportCases('consumer/src/core/router/index.ts', [
      ['@/pages/basket/BasketPage.vue', 'ok'],
      ['@/ab/domain/x', 'ok'],
      ['@/system-common/api-client', 'ok'],
      ['vue-router', 'ok'],
    ])
  })

  describe('services', () => {
    defineImportCases('consumer/src/shopping/basket/services/basket-service.ts', [
      // Vue に依存するモジュール
      ['vue', denied.services],
      ['pinia', denied.services],
      ['vue-router', denied.services],
      ['@vueuse/core', denied.services],
      ['vee-validate', denied.services],
      ['@vee-validate/zod', denied.services],
      ['../stores/basket', denied.services],
      ['@/business-common/stores/notification', denied.services],
      ['../composables/use-basket', denied.services],
      ['@/business-common/composables/use-notification', denied.services],
      // 層の参照方向のルールも適用される
      ['@/core/router', denied.context],
      ['@/ab/public-api', denied.context],
      // API の呼び出しとデータの変換に使うモジュール
      ['@/system-common/api-client', 'ok'],
      ['@/system-common/generated/api-client', 'ok'],
      ['../models/basket-line', 'ok'],
      ['axios', 'ok'],
      // 名前に stores や composables を含むだけのモジュール
      ['../stores-like/x', 'ok'],
      ['./my-composables', 'ok'],
    ])

    test('vue-router の型も参照できない', async () => {
      await assertLint(
        'consumer/src/shopping/basket/services/basket-service.ts',
        "import type { Router } from 'vue-router'\nexport type { Router }\n",
        denied.services,
      )
    })

    // 共通の層の services にも、 Vue に依存するモジュールの禁止と、その層の参照方向のルールが適用される
    defineImportCases('consumer/src/business-common/services/x.ts', [
      ['vue', denied.services],
      ['@/shopping/public-api', denied.businessCommon],
    ])
    defineImportCases('consumer/src/system-common/services/x.ts', [
      ['vue', denied.services],
      ['@/business-common/x', denied.systemCommon],
    ])

    // services の外と、層のフォルダー（ pages 、 core ）の services では、 Vue に依存するモジュールを参照できる
    defineImportCases('consumer/src/shopping/basket/composables/use-basket.ts', [
      ['vue', 'ok'],
      ['../stores/basket', 'ok'],
      ['../services/basket-service', 'ok'],
    ])
    defineImportCases('consumer/src/pages/basket/services/x.ts', [['vue', 'ok']])
    defineImportCases('consumer/src/core/services/x.ts', [['pinia', 'ok']])
  })

  describe('動的インポート', () => {
    const dynamicImport = "export const load = () => import('@/pages/basket/BasketPage.vue')\n"
    test('core 層以外ではエラーになる', async () => {
      await assertLint('consumer/src/ab/domain/x.ts', dynamicImport, denied.dynamicImport)
    })
    test('core 層では通る', async () => {
      await assertLint('consumer/src/core/router/index.ts', dynamicImport, 'ok')
    })
  })
})

describe('eslint.config.ts', () => {
  // 上のテストは参照方向のルールだけを読み込むため、 eslint.config.ts がルールを読み込んでいることは確かめられません。
  // ここでは eslint.config.ts を読み込み、各層のファイルに同じルールが同じ設定で適用されることを確かめます。
  // 後に並べた設定がルールを上書きした場合も検出します。
  const projectEslint = new ESLint({ cwd: import.meta.dirname })
  for (const workspace of ['consumer', 'admin']) {
    for (const filePath of [
      `${workspace}/src/ab/domain/x.ts`,
      `${workspace}/src/ab/ui/X.vue`,
      `${workspace}/src/pages/basket/BasketPage.vue`,
      `${workspace}/src/pages/basket/__tests__/BasketPage.spec.ts`,
      `${workspace}/src/business-common/x.ts`,
      `${workspace}/src/system-common/x.ts`,
      `${workspace}/src/ab/domain/services/x.ts`,
      `${workspace}/src/business-common/services/x.ts`,
      `${workspace}/src/system-common/services/x.ts`,
    ]) {
      test(`${filePath} に参照方向のルールが適用される`, async () => {
        const expected = (await eslint.calculateConfigForFile(filePath)) as Linter.Config
        const actual = (await projectEslint.calculateConfigForFile(filePath)) as Linter.Config
        for (const ruleId of [restrictedImportsRule, restrictedSyntaxRule]) {
          assert.ok(expected.rules?.[ruleId], `${filePath} に ${ruleId} の期待値がありません。`)
          assert.deepEqual(
            actual.rules?.[ruleId],
            expected.rules[ruleId],
            `${filePath} の ${ruleId} の設定が eslint.project-rules.ts と一致しません。`,
          )
        }
      })
    }
  }
})
