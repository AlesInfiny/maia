import { expect, test } from '../fixture/shopping-test-fixture'

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
      const totalPriceCell = checkoutPage.getTotalPriceCell()
      // 買い物かごの読み込みが終わるまでは `-` が表示されるため、金額が表示されるまで待ちます。
      // 読み取る値と、注文完了画面で比較する値を揃えるため、どちらも innerText を使用します。
      await expect(totalPriceCell).toHaveText(/^￥[\d,]+$/, { useInnerText: true })
      totalPriceOnCheckout = await totalPriceCell.innerText()
      await checkoutPage.confirmOrder()
    })

    await test.step('注文完了を確認する', async () => {
      await expect(donePage.getOrderingCompletedMessage()).toBeVisible()
      await expect(page).toHaveURL(/\/ordering\/done\/.+/)
      await expect(donePage.getTotalPriceCell()).toHaveText(totalPriceOnCheckout, {
        useInnerText: true,
      })
    })
  })
})
