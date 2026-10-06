import { describe, it, expect, beforeEach, vi } from 'vitest'
import { defineComponent } from 'vue'
import { mount } from '@vue/test-utils'
import { createPinia, setActivePinia, type Pinia } from 'pinia'
import { http, HttpResponse } from 'msw'
import { HttpStatusCode } from 'axios'
import { server } from '@/../mock/node'
import { useAuthenticationStore } from '@/security/authorization/stores/authentication'
import { useLogin, type Login } from '../use-login'

let pinia: Pinia

beforeEach(() => {
  sessionStorage.clear()
  pinia = createPinia()
  setActivePinia(pinia)
})

/**
 * コンポーネントの中でユースケースコンポーザブルを呼び出します。
 * vee-validate の useForm はコンポーネントの setup の中で呼び出す必要があるため、テスト用のコンポーネントを使います。
 * @returns ログインのユースケース。
 */
function setupLogin(): Login {
  let login: Login | undefined
  mount(
    defineComponent({
      setup() {
        login = useLogin()
        return () => null
      },
    }),
    { global: { plugins: [pinia] } },
  )
  if (!login) {
    throw new Error('ユースケースコンポーザブルを生成できませんでした。')
  }
  return login
}

// 検証エラーのメッセージは入力欄の操作で表示されるため、画面のテストで検証します。
describe('入力フォーム', () => {
  it('ユーザー名とパスワードを入力する_検証に合格する', async () => {
    // Arrange
    const { form } = setupLogin()
    // Act
    form.userName = 'user@example.com'
    form.password = 'password'
    // Assert
    await vi.waitFor(() => expect(form.isValid).toBe(true))
  })

  it('ユーザー名の形式が正しくない_検証に合格しない', async () => {
    // Arrange
    const { form } = setupLogin()
    form.userName = 'user@example.com'
    form.password = 'password'
    await vi.waitFor(() => expect(form.isValid).toBe(true))
    // Act
    form.userName = 'invalid'
    // Assert
    await vi.waitFor(() => expect(form.isValid).toBe(false))
  })
})

describe('ログイン', () => {
  it('ログインできる_loggedInを返し認証済みになる', async () => {
    // Arrange
    const { login } = setupLogin()
    // Act
    const outcome = await login()
    // Assert
    expect(outcome).toEqual({ kind: 'loggedIn' })
    expect(useAuthenticationStore(pinia).isAuthenticated).toBe(true)
  })

  it('サーバーエラー_failedを返し認証済みにならない', async () => {
    // Arrange
    server.use(
      http.get(
        '/api/users',
        () => new HttpResponse(null, { status: HttpStatusCode.InternalServerError }),
      ),
    )
    const { login } = setupLogin()
    // Act
    const outcome = await login()
    // Assert
    expect(outcome).toEqual({ kind: 'failed' })
    expect(useAuthenticationStore(pinia).isAuthenticated).toBe(false)
  })
})
