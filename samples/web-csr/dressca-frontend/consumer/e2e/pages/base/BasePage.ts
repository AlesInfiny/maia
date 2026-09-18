import type { Page } from '@playwright/test'

/**
 * ページオブジェクトの基底クラスです。
 * 各画面のページオブジェクトはこのクラスを継承します。
 */
export abstract class BasePage {
  /**
   * {@link BasePage} クラスの新しいインスタンスを初期化します。
   * @param page 操作対象のページ。
   */
  protected constructor(protected readonly page: Page) {}

  /**
   * 指定したパスへ遷移します。
   * @param path baseURL からの相対パス。
   */
  protected async navigateTo(path: string): Promise<void> {
    await this.page.goto(path)
  }
}
