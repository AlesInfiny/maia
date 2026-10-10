import { describe, it, expect, beforeEach, vi } from 'vitest'
import { defineComponent } from 'vue'
import { mount } from '@vue/test-utils'
import { createPinia, setActivePinia, type Pinia } from 'pinia'
import { i18n } from '@/system-common/locales/i18n'
import { useAuthenticationStore } from '../../stores/authentication'
import { useSignIn, type SignIn } from '../use-sign-in'

let pinia: Pinia

beforeEach(() => {
  sessionStorage.clear()
  pinia = createPinia()
  setActivePinia(pinia)
  i18n.global.locale.value = 'ja'
})

/**
 * コンポーネントの中でユースケースコンポーザブルを呼び出します。
 * vee-validate の useForm はコンポーネントの setup の中で呼び出す必要があるため、テスト用のコンポーネントを使います。
 * @returns ログインのユースケース。
 */
function setupSignIn(): SignIn {
  let signIn: SignIn | undefined
  mount(
    defineComponent({
      setup() {
        signIn = useSignIn()
        return () => null
      },
    }),
    { global: { plugins: [pinia] } },
  )
  if (!signIn) {
    throw new Error('ユースケースコンポーザブルを生成できませんでした。')
  }
  return signIn
}

// 検証エラーのメッセージは入力欄の操作で表示されるため、画面のテストで検証します。
describe('入力フォーム', () => {
  it('メールアドレスとパスワードを入力する_検証に合格する', async () => {
    // Arrange
    const { form } = setupSignIn()
    // Act
    form.email = 'user@example.com'
    form.password = 'password'
    // Assert
    await vi.waitFor(() => expect(form.isValid).toBe(true))
  })

  it('メールアドレスの形式が正しくない_検証に合格しない', async () => {
    // Arrange
    const { form } = setupSignIn()
    form.email = 'user@example.com'
    form.password = 'password'
    await vi.waitFor(() => expect(form.isValid).toBe(true))
    // Act
    form.email = 'invalid'
    // Assert
    await vi.waitFor(() => expect(form.isValid).toBe(false))
  })
})

describe('ログイン', () => {
  it('ログインする_signedInを返し認証済みになる', async () => {
    // Arrange
    const { signIn } = setupSignIn()
    // Act
    const outcome = await signIn()
    // Assert
    expect(outcome).toEqual({ kind: 'signedIn' })
    expect(useAuthenticationStore(pinia).isAuthenticated).toBe(true)
  })
})
