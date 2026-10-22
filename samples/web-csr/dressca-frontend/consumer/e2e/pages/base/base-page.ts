import type { Locator, Page } from '@playwright/test'

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

  /**
   * 指定した見出しのセルを含む表の行の、 2 列目のセルを表す Locator を返します。
   * @param heading 見出しのセルのテキスト。完全一致で検索します。
   * @returns 見出しの隣のセル。
   */
  protected getCellNextToHeading(heading: string): Locator {
    return this.page
      .getByRole('row')
      .filter({ has: this.page.getByRole('cell', { name: heading, exact: true }) })
      .getByRole('cell')
      .nth(1)
  }
}
