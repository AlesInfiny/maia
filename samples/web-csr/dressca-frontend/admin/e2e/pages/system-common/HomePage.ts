import type { Locator, Page } from '@playwright/test'
import { BasePage } from '../base/BasePage'

/**
 * ホーム画面のページオブジェクトです。
 */
export class HomePage extends BasePage {
  /** ホーム画面の見出しです。 */
  private readonly heading: Locator

  /** カタログアイテム管理画面へのリンクです。 */
  private readonly catalogManagementLink: Locator

  /**
   * {@link HomePage} クラスの新しいインスタンスを初期化します。
   * @param page 操作対象のページ。
   */
  constructor(page: Page) {
    super(page)
    // 同名のリンクがグローバルナビゲーションにも存在するため、メインコンテンツに限定します。
    const main = page.getByRole('main')
    this.heading = main.getByText('Dressca 管理 トップ')
    this.catalogManagementLink = main.getByRole('link', { name: 'カタログアイテム管理' })
  }

  /**
   * ホーム画面を表示します。
   */
  async open(): Promise<void> {
    await this.navigateTo('/')
  }

  /**
   * ホーム画面の見出しを表す Locator を返します。
   * @returns ホーム画面の見出し。
   */
  getHeading(): Locator {
    return this.heading
  }

  /**
   * カタログアイテム管理画面へ遷移します。
   */
  async goToCatalogManagement(): Promise<void> {
    await this.catalogManagementLink.click()
  }
}
