import type { Locator, Page } from '@playwright/test'
import { BasePage } from '../base/BasePage'

/**
 * 陳列品一覧画面のページオブジェクトです。
 */
export class DisplayItemPage extends BasePage {
  /** 陳列品を買い物かごに入れるボタンです。画面には陳列品の数だけ存在します。 */
  private readonly addToBasketButtons: Locator

  /**
   * {@link DisplayItemPage} クラスの新しいインスタンスを初期化します。
   * @param page 操作対象のページ。
   */
  constructor(page: Page) {
    super(page)
    this.addToBasketButtons = page.getByRole('button', { name: '買い物かごに入れる' })
  }

  /**
   * 陳列品一覧画面を表示します。
   */
  async open(): Promise<void> {
    await this.navigateTo('/')
  }

  /**
   * 最初に表示されている陳列品を買い物かごに入れます。
   */
  async addFirstDisplayItemToBasket(): Promise<void> {
    await this.addToBasketButtons.first().click()
  }

  /**
   * 陳列品が一覧に表示されていることを表す Locator を返します。
   * @returns 最初の陳列品の買い物かごに入れるボタン。
   */
  getFirstAddToBasketButton(): Locator {
    return this.addToBasketButtons.first()
  }
}
