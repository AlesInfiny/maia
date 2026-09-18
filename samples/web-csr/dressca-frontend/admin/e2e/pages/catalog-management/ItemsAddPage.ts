import type { Locator, Page } from '@playwright/test'
import { BasePage } from '../base/BasePage'

/**
 * カタログアイテム追加画面のページオブジェクトです。
 */
export class ItemsAddPage extends BasePage {
  /** カタログアイテム追加画面の見出しです。 */
  private readonly heading: Locator

  /** アイテム名の入力欄です。 */
  private readonly itemNameInput: Locator

  /** 説明の入力欄です。 */
  private readonly itemDescriptionInput: Locator

  /** 単価の入力欄です。 */
  private readonly priceInput: Locator

  /** 商品コードの入力欄です。 */
  private readonly productCodeInput: Locator

  /** 追加ボタンです。 */
  private readonly addButton: Locator

  /** 追加成功を通知するモーダルです。 */
  private readonly addNoticeModal: Locator

  /** 追加成功を通知するモーダルの確認ボタンです。 */
  private readonly addNoticeCloseButton: Locator

  /**
   * {@link ItemsAddPage} クラスの新しいインスタンスを初期化します。
   * @param page 操作対象のページ。
   */
  constructor(page: Page) {
    super(page)
    this.heading = page.getByText('カタログアイテム追加')
    this.itemNameInput = page.locator('#item-name')
    this.itemDescriptionInput = page.locator('#item-description')
    this.priceInput = page.locator('#unit-price')
    this.productCodeInput = page.locator('#product-code')
    this.addButton = page.getByRole('button', { name: '追加', exact: true })
    this.addNoticeModal = page.getByRole('dialog')
    this.addNoticeCloseButton = this.addNoticeModal.getByRole('button', { name: 'はい' })
  }

  /**
   * カタログアイテム追加画面の見出しを表す Locator を返します。
   * @returns カタログアイテム追加画面の見出し。
   */
  getHeading(): Locator {
    return this.heading
  }

  /**
   * 追加するカタログアイテムの情報を入力します。
   * @param itemName アイテム名。
   * @param itemDescription 説明。
   * @param price 単価。
   * @param productCode 商品コード。
   */
  async inputCatalogItem(
    itemName: string,
    itemDescription: string,
    price: string,
    productCode: string,
  ): Promise<void> {
    await this.itemNameInput.fill(itemName)
    await this.itemDescriptionInput.fill(itemDescription)
    await this.priceInput.fill(price)
    await this.productCodeInput.fill(productCode)
  }

  /**
   * 入力した内容でカタログアイテムを追加します。
   */
  async addCatalogItem(): Promise<void> {
    await this.addButton.click()
  }

  /**
   * 追加成功を通知するモーダルを表す Locator を返します。
   * @returns 追加成功を通知するモーダル。
   */
  getAddNoticeModal(): Locator {
    return this.addNoticeModal
  }

  /**
   * 追加成功を通知するモーダルを閉じます。
   */
  async closeAddNotice(): Promise<void> {
    await this.addNoticeCloseButton.click()
  }
}
