/**
 * eslint.project-rules.ts の参照方向のルールの回帰テストです。
 * 違反する参照と許可される参照を書いたコード片を ESLint の Node API で検査し、
 * 期待どおりにエラーになる・ならないことを確かめます。
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

const eslint = new ESLint({
  cwd: import.meta.dirname,
  // eslint.config.ts の型情報を使用するルールは、存在しないファイルを検査できないため読み込みません。
  // 参照方向のルールの評価に必要なパーサーとプラグインだけを設定します。
  overrideConfigFile: true,
  overrideConfig: [
    {
      files: ['**/*.ts'],
      languageOptions: { parser: tseslint.parser },
      plugins: { '@typescript-eslint': tseslint.plugin },
    },
    ...consumerLayerDependencyRules,
    ...adminLayerDependencyRules,
  ] satisfies Linter.Config[],
})

/**
 * コード片を、指定したファイルのコードとして検査します。
 * @param filePath dressca-frontend からの相対パス。ファイルが存在する必要はありません。
 * @param code 検査するコード。
 * @returns 検出されたメッセージ。
 */
async function lint(filePath: string, code: string): Promise<Linter.LintMessage[]> {
  const [result] = await eslint.lintText(code, { filePath, warnIgnored: true })
  assert.ok(result, `${filePath} の検査結果がありません。`)
  return result.messages
}

/**
 * 参照を検査し、エラーになるかどうかを確かめます。
 * @param filePath 参照元のファイル。
 * @param code 参照を書いたコード。
 * @param expected エラーになるべきなら `'error'` 、通るべきなら `'ok'` 。
 * @param ruleId エラーになるべき場合に、エラーを報告するルール。
 */
async function assertLint(
  filePath: string,
  code: string,
  expected: 'error' | 'ok',
  ruleId = restrictedImportsRule,
): Promise<void> {
  const messages = await lint(filePath, code)
  if (expected === 'ok') {
    assert.deepEqual(
      messages.map((m) => `${m.ruleId}: ${m.message}`),
      [],
      `${filePath} の \`${code}\` はエラーにならないはずです。`,
    )
  } else {
    assert.ok(
      messages.some((m) => m.ruleId === ruleId && m.severity === 2),
      `${filePath} の \`${code}\` は ${ruleId} のエラーになるはずです。実際: ${JSON.stringify(messages)}`,
    )
  }
}

type Case = [specifier: string, expected: 'error' | 'ok']

/**
 * 参照元のファイルごとに、 import 文の参照先と期待する結果を並べたテストを定義します。
 * @param filePath 参照元のファイル。
 * @param cases 参照先のモジュールと期待する結果の組。
 */
function defineImportCases(filePath: string, cases: Case[]): void {
  describe(filePath, () => {
    for (const [specifier, expected] of cases) {
      test(`${specifier} は${expected === 'error' ? 'エラーになる' : '通る'}`, async () => {
        await assertLint(filePath, `import { x } from '${specifier}'\nexport { x }\n`, expected)
      })
    }
  })
}

/** コンテキストから参照したときに、コンテキストによらず結果が同じ参照です。 */
const contextCommonCases: Case[] = [
  // 他のコンテキスト（ 2 文字のフォルダー名を含む）
  ['@/security/public-api', 'error'],
  ['@/security/authentication/application/use-login', 'error'],
  ['@/ab/public-api', 'error'],
  ['@/ab/domain/x', 'error'],
  ['@/cd/public-api', 'error'],
  ['@/brand-new-context/public-api', 'error'],
  // 層の名前で始まる、層ではないフォルダー
  ['@/system-commons/x', 'error'],
  ['@/business-common-extra/x', 'error'],
  // モックのデータの例外（ `@/../` ）が、ドットで始まる他のフォルダーに広がらないこと
  ['@/.a/x', 'error'],
  ['@/./x', 'error'],
  // core 層と pages
  ['@/core/router', 'error'],
  ['@/core/router/route-names', 'error'],
  ['@/core/stores/x', 'error'],
  ['@/pages/basket/BasketPage.vue', 'error'],
  // vue-router の値
  ['vue-router', 'error'],
  // 相対パス、業務共通、システム共通、モックのデータ
  ['./x', 'ok'],
  ['../domain/x', 'ok'],
  ['@/business-common/x', 'ok'],
  ['@/system-common/x', 'ok'],
  ['@/system-common/api-client', 'ok'],
  ['@/system-common/generated/api-client', 'ok'],
  ['@/../mock/x', 'ok'],
  ['vue', 'ok'],
]

describe('コンテキスト', () => {
  defineImportCases('consumer/src/shopping/catalog/application/use-catalog.ts', [
    ...contextCommonCases,
    // 自分のコンテキストへの `@/` の参照
    ['@/shopping/catalog/domain/x', 'error'],
    ['@/shopping/public-api', 'error'],
  ])

  // 2 文字のフォルダー名のコンテキスト
  defineImportCases('consumer/src/ab/domain/x.ts', [
    ...contextCommonCases.filter(([specifier]) => !specifier.startsWith('@/ab/')),
    ['@/ab/domain/y', 'error'],
    ['@/ab/public-api', 'error'],
    ['@/shopping/public-api', 'error'],
  ])

  // layerFolders に登録していない新しいフォルダーもコンテキストとして扱われる
  defineImportCases('consumer/src/brand-new-context/x.ts', [
    ...contextCommonCases.filter(([specifier]) => !specifier.startsWith('@/brand-new-context/')),
    ['@/brand-new-context/y', 'error'],
    ['@/shopping/public-api', 'error'],
  ])

  defineImportCases('admin/src/ab/domain/x.ts', [
    ['@/cd/public-api', 'error'],
    ['@/ab/domain/y', 'error'],
    ['@/core/router', 'error'],
    ['@/business-common/x', 'ok'],
    ['@/../mock/x', 'ok'],
  ])

  test('vue-router の型は参照できる', async () => {
    await assertLint(
      'consumer/src/ab/domain/x.ts',
      "import type { Router } from 'vue-router'\nexport type { Router }\n",
      'ok',
    )
  })
})

describe('pages', () => {
  const pagesCases: Case[] = [
    // コンテキストは public-api だけを参照できる
    ['@/shopping/public-api', 'ok'],
    ['@/ab/public-api', 'ok'],
    ['@/brand-new-context/public-api', 'ok'],
    ['@/shopping/catalog/application/use-catalog', 'error'],
    ['@/shopping/public-api/x', 'error'],
    ['@/ab/domain/x', 'error'],
    ['@/ab/public-api-internal', 'error'],
    // core 層はルート名の定数だけを参照できる
    ['@/core/router', 'error'],
    ['@/core/router/index', 'error'],
    ['@/core/router/route-names', 'ok'],
    // API クライアントと API の型
    ['@/system-common/api-client', 'error'],
    ['@/system-common/api-client/x', 'error'],
    ['@/system-common/generated/api-client', 'error'],
    ['@/system-common/x', 'ok'],
    ['@/business-common/x', 'ok'],
    ['@/pages/catalog/x', 'ok'],
    ['vue-router', 'ok'],
  ]
  defineImportCases('consumer/src/pages/basket/use-basket-page.ts', pagesCases)
  defineImportCases('consumer/src/pages/use-page.ts', pagesCases)
  defineImportCases('admin/src/pages/catalog/use-catalog-page.ts', pagesCases)
})

describe('pages のテスト', () => {
  const pagesTestCases: Case[] = [
    ['@/core/router', 'ok'],
    ['@/core/router/route-names', 'ok'],
    ['@/system-common/generated/api-client', 'ok'],
    ['@/shopping/public-api', 'ok'],
    ['@/ab/public-api', 'ok'],
    ['@/core/router/index', 'error'],
    ['@/shopping/catalog/application/use-catalog', 'error'],
    ['@/ab/domain/x', 'error'],
  ]
  defineImportCases('consumer/src/pages/basket/__tests__/BasketPage.spec.ts', pagesTestCases)
  defineImportCases('consumer/src/pages/__tests__/HomePage.spec.ts', pagesTestCases)
})

describe('business-common', () => {
  defineImportCases('consumer/src/business-common/x.ts', [
    ['@/core/router', 'error'],
    ['@/core/router/route-names', 'error'],
    ['@/pages/basket/BasketPage.vue', 'error'],
    ['@/shopping/public-api', 'error'],
    ['@/ab/public-api', 'error'],
    ['@/brand-new-context/x', 'error'],
    ['vue-router', 'error'],
    ['@/business-common/y', 'ok'],
    ['@/system-common/x', 'ok'],
    ['@/../mock/x', 'ok'],
    ['./y', 'ok'],
  ])
})

describe('system-common', () => {
  defineImportCases('consumer/src/system-common/x.ts', [
    ['@/core/router', 'error'],
    ['@/pages/basket/BasketPage.vue', 'error'],
    ['@/shopping/public-api', 'error'],
    ['@/ab/public-api', 'error'],
    ['@/business-common/x', 'error'],
    ['vue-router', 'error'],
    ['@/system-common/y', 'ok'],
    ['@/../mock/x', 'ok'],
    ['./y', 'ok'],
  ])
})

describe('core 層', () => {
  defineImportCases('consumer/src/core/router/index.ts', [
    ['@/pages/basket/BasketPage.vue', 'ok'],
    ['@/shopping/public-api', 'ok'],
    ['@/ab/domain/x', 'ok'],
    ['@/system-common/api-client', 'ok'],
    ['vue-router', 'ok'],
  ])
  defineImportCases('consumer/src/main.ts', [
    ['@/core/router', 'ok'],
    ['@/ab/domain/x', 'ok'],
  ])
})

describe('動的インポート', () => {
  const dynamicImport = "export const load = () => import('@/pages/basket/BasketPage.vue')\n"
  for (const filePath of [
    'consumer/src/pages/basket/use-basket-page.ts',
    'consumer/src/pages/basket/__tests__/BasketPage.spec.ts',
    'consumer/src/ab/domain/x.ts',
    'consumer/src/business-common/x.ts',
    'consumer/src/system-common/x.ts',
  ]) {
    test(`${filePath} ではエラーになる`, async () => {
      await assertLint(filePath, dynamicImport, 'error', restrictedSyntaxRule)
    })
  }
  test('core 層では通る', async () => {
    await assertLint('consumer/src/core/router/index.ts', dynamicImport, 'ok')
  })
})
