import { describe, it, expect, vi, beforeAll } from 'vitest'
import { flushPromises, mount, VueWrapper } from '@vue/test-utils'
import { router } from '@/business-common/router'
import { createTestingPinia, type TestingPinia } from '@pinia/testing'
import { http, HttpResponse } from 'msw'
import { HttpStatusCode } from 'axios'
import ItemsAddPage from '@/pages/catalog/items/add/ItemsAddPage.vue'
import { Roles } from '@/security/public-api'
import { useNotificationStore } from '@/business-common/stores/notification'
import { server } from '@/../mock/node'

/**
 * テスト用の Pinia ストアを生成します。
 * ユーザーのロール情報を初期状態として設定し、結合テスト用の環境を構築します。
 * @param userRoles - 認証状態に設定するユーザーロールの配列
 * @returns 初期化済みの TestingPinia インスタンス
 */
function CreateLoginState(userRoles: string[]) {
  return createTestingPinia({
    initialState: {
      authentication: {
        userRoles,
      },
    },
    createSpy: vi.fn, // 明示的に設定する必要があります。
    stubActions: false, // 結合テストなので、アクションはモック化しないように設定します。
  })
}

/**
 * 指定された Pinia ストアを利用してコンポーネントをマウントします。
 * グローバルプラグインとして `pinia` と `router` を注入します。
 * @param pinia - テストに使用する TestingPinia インスタンス
 * @returns マウント済みの Vue Test Utils のラッパー
 */
function getWrapper(pinia: TestingPinia) {
  return mount(ItemsAddPage, {
    global: { plugins: [pinia, router] },
  })
}

describe('管理者ロール_アイテムを追加できる', () => {
  let loginState: TestingPinia
  let wrapper: VueWrapper

  beforeAll(() => {
    loginState = CreateLoginState([Roles.ADMIN])
    wrapper = getWrapper(loginState)
  })

  it('追加画面に遷移できる', async () => {
    // Arrange
    // Act
    await flushPromises()
    // Assert
    expect(wrapper.html()).toContain('カタログアイテム追加')
  })

  it('追加ボタンを押下_追加成功_通知モーダルが開く', async () => {
    // Arrange
    // Act
    await wrapper.find('button').trigger('click')
    await flushPromises()
    await vi.waitUntil(() =>
      wrapper.findAllComponents({ name: 'NotificationModal' })[0]!.isVisible(),
    )
    // Assert
    expect(wrapper.html()).toContain('カタログアイテムを追加しました。')
  })
})

describe('ゲストロール_アイテム追加ボタンが非活性', () => {
  let loginState: TestingPinia
  let wrapper: VueWrapper

  beforeAll(() => {
    loginState = CreateLoginState(['ROLE_GUEST'])
    wrapper = getWrapper(loginState)
  })

  it('追加画面に遷移できる', async () => {
    // Arrange
    // Act
    await flushPromises()
    // Assert
    expect(wrapper.html()).toContain('カタログアイテム追加')
  })

  it('追加ボタンが非活性', () => {
    // Arrange
    // Act
    const button = wrapper.find('button')
    // Assert
    expect(button.attributes('disabled')).toBeDefined()
  })
})

describe('結果の通知', () => {
  it('カテゴリとブランドを取得できない_トーストを表示し入力フォームを表示しない', async () => {
    // Arrange
    server.use(
      http.get(
        '/api/catalog-categories',
        () => new HttpResponse(null, { status: HttpStatusCode.InternalServerError }),
      ),
    )
    const loginState = CreateLoginState([Roles.ADMIN])
    // Act
    const wrapper = getWrapper(loginState)
    await flushPromises()
    // Assert
    expect(useNotificationStore(loginState).message).toBe(
      'カテゴリとブランド情報の取得に失敗しました。',
    )
    expect(wrapper.find('form').exists()).toBe(false)
  })

  it('追加に失敗した_トーストを表示する', async () => {
    // Arrange
    const loginState = CreateLoginState([Roles.ADMIN])
    const wrapper = getWrapper(loginState)
    await flushPromises()
    server.use(
      http.post(
        '/api/catalog-items',
        () => new HttpResponse(null, { status: HttpStatusCode.InternalServerError }),
      ),
    )
    // Act
    await wrapper.find('button').trigger('click')
    await flushPromises()
    // Assert
    expect(useNotificationStore(loginState).message).toBe('カタログアイテムの追加に失敗しました。')
    expect(wrapper.findAllComponents({ name: 'NotificationModal' })[0]!.isVisible()).toBe(false)
  })
})
