import { test as base } from '@playwright/test'
import { ItemsAddPage } from '../pages/catalog-management/ItemsAddPage'
import { ItemsPage } from '../pages/catalog-management/ItemsPage'
import { LoginPage } from '../pages/security/LoginPage'
import { HomePage } from '../pages/system-common/HomePage'

/**
 * カタログ管理のテストシナリオで使用するページオブジェクトです。
 */
type CatalogManagementPages = {
  /** ログイン画面。 */
  loginPage: LoginPage
  /** ホーム画面。 */
  homePage: HomePage
  /** カタログアイテム一覧画面。 */
  itemsPage: ItemsPage
  /** カタログアイテム追加画面。 */
  itemsAddPage: ItemsAddPage
}

/**
 * カタログ管理のテストシナリオで使用する test です。
 * 各画面のページオブジェクトをフィクスチャーとして提供します。
 */
export const test = base.extend<CatalogManagementPages>({
  loginPage: async ({ page }, use) => {
    await use(new LoginPage(page))
  },
  homePage: async ({ page }, use) => {
    await use(new HomePage(page))
  },
  itemsPage: async ({ page }, use) => {
    await use(new ItemsPage(page))
  },
  itemsAddPage: async ({ page }, use) => {
    await use(new ItemsAddPage(page))
  },
})

export { expect } from '@playwright/test'
