import { expect, test } from '../fixture/catalogManagementTestFixture'

/*
 * カタログ管理の業務シナリオを検証します。
 * test.step() は業務フローの単位で区切り、要件定義書・設計書と粒度を揃えます。
 *
 * カタログアイテムの追加は実行するたびにデータが増えるため、
 * 実行順序と実行回数に依存しないよう、アイテム名と商品コードは実行ごとに一意な値を生成します。
 *
 * 追加したアイテムが一覧に表示されることは検証しません。
 * カタログアイテム一覧はページサイズ 20 の 1 ページ目のみを表示し、名称による検索もできないため、
 * 登録件数が 20 件を超えると追加したアイテムが一覧に現れず、実行回数に依存したテストになるためです。
 * 追加の成否は、追加に成功した場合のみ表示される通知モーダルで検証します。
 */
test.describe('カタログ管理', () => {
  test('カタログアイテムを追加する', async ({ loginPage, homePage, itemsPage, itemsAddPage }) => {
    const uniqueSuffix = Date.now().toString()
    const itemName = `E2E テスト用アイテム ${uniqueSuffix}`
    const productCode = `E2E${uniqueSuffix}`

    await test.step('ログインする', async () => {
      // ホーム画面は認証が必要なため、ログイン画面にリダイレクトされます。
      await homePage.open()
      await expect(loginPage.getLoginButton()).toBeVisible()
      await loginPage.login('test@example.com', 'password')
      await expect(homePage.getHeading()).toBeVisible()
    })

    await test.step('カタログアイテム一覧を表示する', async () => {
      await homePage.goToCatalogManagement()
      await expect(itemsPage.getHeading()).toBeVisible()
    })

    await test.step('カタログアイテム追加画面を表示する', async () => {
      await itemsPage.goToAddItem()
      await expect(itemsAddPage.getHeading()).toBeVisible()
    })

    await test.step('カタログアイテムを追加する', async () => {
      await itemsAddPage.inputCatalogItem(
        itemName,
        'E2E テストで追加したカタログアイテムです。',
        '1980',
        productCode,
      )
      await itemsAddPage.addCatalogItem()
      await expect(itemsAddPage.getAddNoticeModal()).toContainText(
        'カタログアイテムを追加しました。',
      )
    })

    await test.step('カタログアイテム一覧に戻る', async () => {
      await itemsAddPage.closeAddNotice()
      await expect(itemsPage.getHeading()).toBeVisible()
    })
  })
})
