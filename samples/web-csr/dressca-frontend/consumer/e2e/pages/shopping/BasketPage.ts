import type { Locator, Page } from '@playwright/test'
import { BasePage } from '../base/BasePage'

/**
 * 買い物かご画面のページオブジェクトです。
 */
export class BasketPage extends BasePage {
  /** 陳列品を買い物かごに追加した旨のメッセージです。 */
  private readonly addedItemMessage: Locator

  /** 買い物かごの中身の見出しです。 */
  private readonly basketContentsHeading: Locator

  /** レジに進むボタンです。 */
  private readonly orderButton: Locator

  /**
   * {@link BasketPage} クラスの新しいインスタンスを初期化します。
   * @param page 操作対象のページ。
   */
  constructor(page: Page) {
    super(page)
    this.addedItemMessage = page.getByText('以下の商品が追加されました。')
    this.basketContentsHeading = page.getByText('現在のカートの中身')
    this.orderButton = page.getByTestId('orderButton')
  }

  /**
   * 陳列品を買い物かごに追加した旨のメッセージを表す Locator を返します。
   * @returns 追加完了メッセージ。
   */
  getAddedItemMessage(): Locator {
    return this.addedItemMessage
  }

  /**
   * 買い物かごの中身の見出しを表す Locator を返します。
   * @returns 買い物かごの中身の見出し。
   */
  getBasketContentsHeading(): Locator {
    return this.basketContentsHeading
  }

  /**
   * レジに進みます。
   */
  async proceedToCheckout(): Promise<void> {
    await this.orderButton.click()
  }
}
