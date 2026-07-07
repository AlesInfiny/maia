import { beforeEach, describe, expect, it } from 'vitest'
import { flushPromises, mount, VueWrapper } from '@vue/test-utils'
import { router } from '@/router'
import LoginView from '@/views/authentication/LoginView.vue'
import { FormContextKey, type FormContext } from 'vee-validate'
import type { ComponentInternalInstance } from 'vue'

/**
 * ログイン画面のラッパーを生成します。
 * @returns マウント済みのラッパー。
 */
async function getWrapper() {
  router.push({ name: 'authentication/login' })
  await router.isReady()
  return mount(LoginView, {
    global: {
      plugins: [router],
    },
  })
}

type ComponentInternalInstanceWithProvides = ComponentInternalInstance & {
  provides: Record<symbol, unknown>
}

/**
 * FormContext を取得するユーティリティ関数
 * @param wrapper VueWrapper インスタンス
 * @returns FormContext
 */
function getFormContext(wrapper: VueWrapper): FormContext {
  const instance = wrapper.getCurrentComponent() as ComponentInternalInstanceWithProvides
  return instance.provides[FormContextKey] as FormContext
}

/**
 *入力値を設定してバリデーションを実行します。
 * @param wrapper VueWrapper インスタンス
 * @param userName ユーザー名
 * @param password パスワード
 */
async function setValuesAndValidate(wrapper: VueWrapper, userName?: string, password?: string) {
  const formCtx = getFormContext(wrapper)
  const userNameInput = wrapper.get('input#userName')
  const passwordInput = wrapper.get('input#password')

  if (userName === undefined) {
    formCtx.setFieldValue('userName', undefined)
  } else {
    await userNameInput.setValue(userName)
    await userNameInput.trigger('blur')
  }

  if (password === undefined) {
    formCtx.setFieldValue('password', undefined)
  } else {
    await passwordInput.setValue(password)
    await passwordInput.trigger('blur')
  }

  // 明示的にvalidateを呼び出す
  await formCtx.validate()
  await flushPromises()
}

describe('LoginView', () => {
  let wrapper: VueWrapper

  beforeEach(async () => {
    wrapper = await getWrapper()
    await flushPromises()
  })

  it('ログイン画面を表示できる', () => {
    expect(wrapper.text()).toContain('ログイン')
  })

  it('ユーザー名が空文字のときエラーメッセージが表示される', async () => {
    await setValuesAndValidate(wrapper, '', 'password123')
    const emailErr = wrapper.find('#username-error').text()
    expect(emailErr).toBe('ユーザー名は必須です。')
  })

  it('ユーザー名が空白のときエラーメッセージが表示される', async () => {
    await setValuesAndValidate(wrapper, ' ', 'password123')
    const emailErr = wrapper.find('#username-error').text()
    expect(emailErr).toBe('ユーザー名は必須です。')
  })

  it('ユーザー名の形式が正しくないときエラーメッセージが表示される', async () => {
    await setValuesAndValidate(wrapper, 'invalid-email', 'password123')
    const emailErr = wrapper.find('#username-error').text()
    expect(emailErr).toBe('メールアドレスの形式で入力してください。')
  })

  it('パスワードが空文字のときエラーメッセージが表示される', async () => {
    await setValuesAndValidate(wrapper, 'aaa@example.com', '')
    const passwordErr = wrapper.find('#password-error').text()
    expect(passwordErr).toBe('パスワードは必須です。')
  })

  it('パスワードが空白のときエラーメッセージが表示される', async () => {
    await setValuesAndValidate(wrapper, 'aaa@example.com', ' ')
    const passwordErr = wrapper.find('#password-error').text()
    expect(passwordErr).toBe('パスワードは必須です。')
  })
})
