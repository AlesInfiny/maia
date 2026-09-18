import type { Locator, Page } from '@playwright/test'
import { BasePage } from '../base/BasePage'

/**
 * 注文内容確認画面のページオブジェクトです。
 */
export class CheckoutPage extends BasePage {
  /** 注文内容の確認を促すメッセージです。 */
  private readonly checkAndCompleteMessage: Locator

  /** 注文を確定するボタンです。 */
  private readonly confirmOrderButton: Locator

  /** 合計金額のセルです。 */
  private readonly totalPriceCell: Locator

  /**
   * {@link CheckoutPage} クラスの新しいインスタンスを初期化します。
   * @param page 操作対象のページ。
   */
  constructor(page: Page) {
    super(page)
    this.checkAndCompleteMessage = page.getByText(
      '注文内容を確認して「注文を確定する」ボタンを押してください。',
    )
    this.confirmOrderButton = page.getByRole('button', { name: '注文を確定する' })
    this.totalPriceCell = page
      .getByRole('row')
      .filter({ has: page.getByRole('cell', { name: '合計', exact: true }) })
      .getByRole('cell')
      .nth(1)
  }

  /**
   * 注文内容の確認を促すメッセージを表す Locator を返します。
   * @returns 注文内容の確認を促すメッセージ。
   */
  getCheckAndCompleteMessage(): Locator {
    return this.checkAndCompleteMessage
  }

  /**
   * 表示されている合計金額を取得します。
   * @returns 合計金額の表示文字列。
   */
  async getTotalPrice(): Promise<string> {
    return (await this.totalPriceCell.innerText()).trim()
  }

  /**
   * 注文を確定します。
   */
  async confirmOrder(): Promise<void> {
    await this.confirmOrderButton.click()
  }
}
