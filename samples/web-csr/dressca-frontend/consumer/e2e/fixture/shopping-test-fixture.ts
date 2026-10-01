import { test as base } from '@playwright/test'
import { LoginPage } from '../pages/security/login-page'
import { BasketPage } from '../pages/shopping/basket-page'
import { CheckoutPage } from '../pages/shopping/checkout-page'
import { DisplayItemPage } from '../pages/shopping/display-item-page'
import { DonePage } from '../pages/shopping/done-page'

/**
 * 買い物のテストシナリオで使用するページオブジェクトです。
 */
type ShoppingPages = {
  /** ログイン画面。 */
  loginPage: LoginPage
  /** 陳列品一覧画面。 */
  displayItemPage: DisplayItemPage
  /** 買い物かご画面。 */
  basketPage: BasketPage
  /** 注文内容確認画面。 */
  checkoutPage: CheckoutPage
  /** 注文完了画面。 */
  donePage: DonePage
}

/**
 * 買い物のテストシナリオで使用する test です。
 * 各画面のページオブジェクトをフィクスチャーとして提供します。
 */
export const test = base.extend<ShoppingPages>({
  loginPage: async ({ page }, use) => {
    await use(new LoginPage(page))
  },
  displayItemPage: async ({ page }, use) => {
    await use(new DisplayItemPage(page))
  },
  basketPage: async ({ page }, use) => {
    await use(new BasketPage(page))
  },
  checkoutPage: async ({ page }, use) => {
    await use(new CheckoutPage(page))
  },
  donePage: async ({ page }, use) => {
    await use(new DonePage(page))
  },
})

export { expect } from '@playwright/test'
