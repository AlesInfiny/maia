import { describe, expect, it, vi } from 'vitest'
import { i18n } from '@/locales/i18n'
import LoginView from '@/views/authentication/LoginView.vue'
import { flushPromises, mount, VueWrapper } from '@vue/test-utils'
import { router } from '@/router'
import { createTestingPinia } from '@pinia/testing'
import { FormContextKey, type FormContext } from 'vee-validate'
import type { ComponentInternalInstance } from 'vue'

/**
 * 日本語ロケールでのテスト用のラッパーを生成します。
 * @returns マウント済みのラッパー。
 */
function getWrapperJa() {
  const pinia = createTestingPinia({
    createSpy: vi.fn,
  })
  i18n.global.locale.value = 'ja'
  return mount(LoginView, {
    global: { plugins: [pinia, router, i18n] },
  })
}

/**
 * 英語ロケールでのテスト用のラッパーを生成します。
 * @returns マウント済みのラッパー。
 */
function getWrapperEn() {
  const pinia = createTestingPinia({
    createSpy: vi.fn,
  })
  i18n.global.locale.value = 'en'
  return mount(LoginView, {
    global: { plugins: [pinia, router, i18n] },
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
 * 入力値を設定してバリデーションを実行します。
 * @param wrapper VueWrapper インスタンス
 * @param email メールアドレス
 * @param password パスワード
 */
async function setValuesAndValidate(wrapper: VueWrapper, email?: string, password?: string) {
  const formCtx = getFormContext(wrapper)
  const emailInput = wrapper.get('input#email')
  const passwordInput = wrapper.get('input#password')

  if (email === undefined) {
    formCtx.setFieldValue('email', undefined)
  } else {
    await emailInput.setValue(email)
    await emailInput.trigger('blur')
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

describe('Authentication validation', () => {
  it('入力値が空の場合、日本語で必須メッセージを返す', async () => {
    const wrapper = getWrapperJa()
    await setValuesAndValidate(wrapper, '', '')

    const emailErr = wrapper.find('#email-error').text()
    expect(emailErr).toBe('値を入力してください')
    const passwordErr = wrapper.find('#password-error').text()
    expect(passwordErr).toBe('値を入力してください')
  })

  it('入力値が空白の場合、日本語で必須メッセージを返す', async () => {
    const wrapper = getWrapperJa()
    await setValuesAndValidate(wrapper, ' ', ' ')
    const emailErr = wrapper.find('#email-error').text()
    expect(emailErr).toBe('値を入力してください')
    const passwordErr = wrapper.find('#password-error').text()
    expect(passwordErr).toBe('値を入力してください')
  })

  it('日本語でメール形式メッセージを返す', async () => {
    const wrapper = getWrapperJa()
    await setValuesAndValidate(wrapper, 'invalid-email', 'aaa')
    const emailErr = wrapper.find('#email-error').text()
    expect(emailErr).toBe('メールアドレスの形式で入力してください')
  })

  it('入力値が空の場合、英語で必須メッセージを返す', async () => {
    const wrapper = getWrapperEn()
    await setValuesAndValidate(wrapper, '', '')

    const emailErr = wrapper.find('#email-error').text()
    expect(emailErr).toBe('this field is required')
    const passwordErr = wrapper.find('#password-error').text()
    expect(passwordErr).toBe('this field is required')
  })

  it('入力値が空白の場合、英語で必須メッセージを返す', async () => {
    const wrapper = getWrapperEn()
    await setValuesAndValidate(wrapper, ' ', ' ')

    const emailErr = wrapper.find('#email-error').text()
    expect(emailErr).toBe('this field is required')
    const passwordErr = wrapper.find('#password-error').text()
    expect(passwordErr).toBe('this field is required')
  })

  it('英語でメール形式メッセージを返す', async () => {
    const wrapper = getWrapperEn()
    await setValuesAndValidate(wrapper, 'invalid-email', 'aaa')

    const emailErr = wrapper.find('#email-error').text()
    expect(emailErr).toBe('invalid email format')
  })
})
