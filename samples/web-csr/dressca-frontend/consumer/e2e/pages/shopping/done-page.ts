import type { Locator, Page } from '@playwright/test'
import { BasePage } from '../base/base-page'

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
    this.totalPriceCell = this.getCellNextToHeading('合計')
  }

  /**
   * 注文完了画面を表示します。
   * @param orderId 表示対象の注文 ID。
   */
  async open(orderId: string): Promise<void> {
    await this.navigateTo(`/ordering/done/${orderId}`)
  }

  /**
   * 注文が完了した旨のメッセージを表す Locator を返します。
   * @returns 注文完了メッセージ。
   */
  getOrderingCompletedMessage(): Locator {
    return this.orderingCompletedMessage
  }

  /**
   * 合計金額のセルを表す Locator を返します。
   * 注文の読み込みが終わるまでは、合計金額に `-` が表示されます。
   * @returns 合計金額のセル。
   */
  getTotalPriceCell(): Locator {
    return this.totalPriceCell
  }
}
