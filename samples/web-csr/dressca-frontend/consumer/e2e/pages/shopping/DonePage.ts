import type { Locator, Page } from '@playwright/test'
import { BasePage } from '../base/BasePage'

/**
 * 注文完了画面のページオブジェクトです。
 */
export class DonePage extends BasePage {
  /** 注文が完了した旨のメッセージです。 */
  private readonly orderingCompletedMessage: Locator

  /** 合計金額のセルです。 */
  private readonly totalPriceCell: Locator

  /**
   * {@link DonePage} クラスの新しいインスタンスを初期化します。
   * @param page 操作対象のページ。
   */
  constructor(page: Page) {
    super(page)
    this.orderingCompletedMessage = page.getByText('注文が完了しました。')
    this.totalPriceCell = page
      .getByRole('row')
      .filter({ has: page.getByRole('cell', { name: '合計', exact: true }) })
      .getByRole('cell')
      .nth(1)
  }

  /**
   * 注文が完了した旨のメッセージを表す Locator を返します。
   * @returns 注文完了メッセージ。
   */
  getOrderingCompletedMessage(): Locator {
    return this.orderingCompletedMessage
  }

  /**
   * 表示されている合計金額を取得します。
   * @returns 合計金額の表示文字列。
   */
  async getTotalPrice(): Promise<string> {
    return (await this.totalPriceCell.innerText()).trim()
  }
}
