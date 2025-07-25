import { describe, it, expect, vi, beforeAll } from 'vitest'
import { flushPromises, mount, VueWrapper } from '@vue/test-utils'
import { router } from '@/router'
import { createTestingPinia, type TestingPinia } from '@pinia/testing'
import ItemsAddView from '@/views/catalog/ItemsAddView.vue'
import { Roles } from '@/shared/constants/roles'

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

function getWrapper(pinia: TestingPinia) {
  return mount(ItemsAddView, {
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
      wrapper.findAllComponents({ name: 'NotificationModal' })[0].isVisible(),
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
