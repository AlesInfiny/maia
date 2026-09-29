import type { Locator, Page } from '@playwright/test'
import { BasePage } from '../base/BasePage'

/**
 * ログイン画面のページオブジェクトです。
 */
export class LoginPage extends BasePage {
  /** メールアドレスの入力欄です。 */
  private readonly emailInput: Locator

  /** パスワードの入力欄です。 */
  private readonly passwordInput: Locator

  /** ログインボタンです。 */
  private readonly loginButton: Locator

  /**
   * {@link LoginPage} クラスの新しいインスタンスを初期化します。
   * @param page 操作対象のページ。
   */
  constructor(page: Page) {
    super(page)
    this.emailInput = page.locator('#email')
    this.passwordInput = page.locator('#password')
    this.loginButton = page.getByRole('button', { name: 'ログイン' })
  }

  /**
   * ログイン画面を表示します。
   */
  async open(): Promise<void> {
    await this.navigateTo('/authentication/login')
  }

  /**
   * メールアドレスとパスワードを入力してログインします。
   * @param email メールアドレス。
   * @param password パスワード。
   */
  async login(email: string, password: string): Promise<void> {
    await this.emailInput.fill(email)
    await this.passwordInput.fill(password)
    await this.loginButton.click()
  }

  /**
   * ログインボタンを表す Locator を返します。
   * @returns ログインボタン。
   */
  getLoginButton(): Locator {
    return this.loginButton
  }
}
