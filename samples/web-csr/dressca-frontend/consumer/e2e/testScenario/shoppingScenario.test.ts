import { expect, test } from '../fixture/shoppingTestFixture'

/*
 * 買い物の業務シナリオを検証します。
 * test.step() は業務フローの単位で区切り、要件定義書・設計書と粒度を揃えます。
 */
test.describe('買い物', () => {
  test('1 つの陳列品の購入を完了できる', async ({
    page,
    loginPage,
    displayItemPage,
    basketPage,
    checkoutPage,
    donePage,
  }) => {
    let totalPriceOnCheckout = ''

    await test.step('陳列品一覧を表示する', async () => {
      await displayItemPage.open()
      await expect(displayItemPage.getFirstAddToBasketButton()).toBeVisible()
    })

    await test.step('陳列品を買い物かごに入れる', async () => {
      await displayItemPage.addFirstDisplayItemToBasket()
      await expect(basketPage.getAddedItemMessage()).toBeVisible()
      await expect(basketPage.getBasketContentsHeading()).toBeVisible()
    })

    await test.step('レジに進む', async () => {
      await basketPage.proceedToCheckout()
      // 注文内容確認画面は認証が必要なため、ログイン画面にリダイレクトされます。
      await expect(loginPage.getLoginButton()).toBeVisible()
    })

    await test.step('ログインする', async () => {
      await loginPage.login('test@example.com', 'password')
      await expect(checkoutPage.getCheckAndCompleteMessage()).toBeVisible()
    })

    await test.step('注文を確定する', async () => {
      totalPriceOnCheckout = await checkoutPage.getTotalPrice()
      await checkoutPage.confirmOrder()
    })

    await test.step('注文完了を確認する', async () => {
      await expect(donePage.getOrderingCompletedMessage()).toBeVisible()
      await expect(page).toHaveURL(/\/ordering\/done\/.+/)
      expect(await donePage.getTotalPrice()).toBe(totalPriceOnCheckout)
    })
  })
})
