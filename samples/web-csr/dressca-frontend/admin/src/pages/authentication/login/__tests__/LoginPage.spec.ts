import { beforeEach, describe, expect, it, vi } from 'vitest'
import { routeNames } from '@/business-common/router/route-names'
import { flushPromises, mount, VueWrapper } from '@vue/test-utils'
import { router } from '@/business-common/router'
import LoginPage from '@/pages/authentication/login/LoginPage.vue'
import { FormContextKey, type FormContext } from 'vee-validate'
import type { ComponentInternalInstance } from 'vue'
import { createTestingPinia, type TestingPinia } from '@pinia/testing'
import { http, HttpResponse } from 'msw'
import { HttpStatusCode } from 'axios'
import { useNotificationStore } from '@/business-common/stores/notification'
import { server } from '@/../mock/node'

/**
 * ログイン画面のラッパーを生成します。
 * @returns マウント済みのラッパー。
 */
async function getWrapper() {
  router.push({ name: routeNames.login })
  await router.isReady()
  return mount(LoginPage, {
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

describe('LoginPage', () => {
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

describe('ログインの結果の通知と遷移', () => {
  /**
   * 戻り先を指定してログイン画面を表示し、有効な値を入力します。
   * @param pinia テストに使用する TestingPinia インスタンス。
   * @returns マウント済みのラッパー。
   */
  async function getFilledWrapper(pinia: TestingPinia) {
    await router.push({ name: routeNames.login, query: { redirect: '/catalog/items' } })
    const filled = mount(LoginPage, { global: { plugins: [pinia, router] } })
    await setValuesAndValidate(filled, 'user@example.com', 'password')
    return filled
  }

  it('ログインできる_戻り先の画面へ遷移する', async () => {
    // Arrange
    const pinia = createTestingPinia({ createSpy: vi.fn, stubActions: false })
    const filled = await getFilledWrapper(pinia)
    // Act
    await filled.get('button').trigger('click')
    await vi.waitUntil(() => router.currentRoute.value.name === routeNames.catalogItems)
    // Assert
    expect(router.currentRoute.value.fullPath).toBe('/catalog/items')
  })

  it('ログインできない_トーストを表示しログイン画面にとどまる', async () => {
    // Arrange
    server.use(
      http.get(
        '/api/users',
        () => new HttpResponse(null, { status: HttpStatusCode.InternalServerError }),
      ),
    )
    const pinia = createTestingPinia({ createSpy: vi.fn, stubActions: false })
    const filled = await getFilledWrapper(pinia)
    // Act
    await filled.get('button').trigger('click')
    await flushPromises()
    // Assert
    expect(useNotificationStore(pinia).message).toBe('ログインに失敗しました。')
    expect(router.currentRoute.value.name).toBe(routeNames.login)
  })
})
