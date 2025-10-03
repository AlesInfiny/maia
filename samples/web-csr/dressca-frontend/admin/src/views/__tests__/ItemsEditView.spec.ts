import { describe, it, expect, vi, beforeAll } from 'vitest'
import { flushPromises, mount, VueWrapper } from '@vue/test-utils'
import { createTestingPinia, type TestingPinia } from '@pinia/testing'
import ItemsEditView from '@/views/catalog/ItemsEditView.vue'
import { router } from '@/router'
import { Roles } from '@/shared/constants/roles'

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
async function getWrapper(pinia: TestingPinia) {
  router.push({ name: 'catalog/items/edit', params: { itemId: 1 } })
  await router.isReady()
  return mount(ItemsEditView, {
    global: { plugins: [pinia, router] },
  })
}

describe('管理者ロール_アイテムが削除できる', () => {
  let loginState: TestingPinia
  let wrapper: VueWrapper

  beforeAll(async () => {
    loginState = CreateLoginState([Roles.ADMIN])
    wrapper = await getWrapper(loginState)
  })

  it('編集画面に遷移できる', async () => {
    // Arrange
    // Act
    await flushPromises()
    // Assert
    expect(wrapper.html()).toContain('カタログアイテム編集')
  })

  it('削除ボタンを押下_確認モーダルが開く', async () => {
    // Arrange
    // Act
    await wrapper.findAll('button')[0].trigger('click')
    // Assert
    expect(wrapper.html()).toContain('カタログアイテムを削除します。')
  })

  it('OKボタンを押下_確認モーダルが閉じる', async () => {
    // Arrange
    // Act
    await wrapper
      .findAllComponents({ name: 'ConfirmationModal' })[0]
      .findAll('button')[0]
      .trigger('click')
    await flushPromises()
    // Assert
    expect(wrapper.findAllComponents({ name: 'ConfirmationModal' })[0].isVisible()).toBeFalsy()
  })

  it('削除成功_通知モーダルが開く', async () => {
    // Arrange
    // Act
    await vi.waitUntil(() =>
      wrapper.findAllComponents({ name: 'NotificationModal' })[0].isVisible(),
    )
    // Assert
    expect(wrapper.html()).toContain('カタログアイテムを削除しました。')
  })

  it('OKボタンを押下__通知モーダルが閉じる', async () => {
    // Act
    await wrapper
      .findAllComponents({ name: 'NotificationModal' })[0]
      .findAll('button')[0]
      .trigger('click')
    // Assert
    expect(wrapper.findAllComponents({ name: 'NotificationModal' })[0].isVisible()).toBeFalsy()
  })
})

describe('ゲストロール_アイテム削除ボタンが非活性', () => {
  let loginState: TestingPinia
  let wrapper: VueWrapper

  beforeAll(async () => {
    loginState = CreateLoginState(['ROLE_GUEST'])
    wrapper = await getWrapper(loginState)
  })

  it('編集画面に遷移できる', async () => {
    // Arrange
    // Act
    await flushPromises()
    // Assert
    expect(wrapper.html()).toContain('カタログアイテム編集')
  })

  it('削除ボタンが非活性', () => {
    // Arrange
    // Act
    const deleteButton = wrapper.findAll('button')[0]
    // Assert
    expect(deleteButton.attributes('disabled')).toBeDefined()
  })
})

describe('管理者ロール_アイテムが更新できる', () => {
  let loginState: TestingPinia
  let wrapper: VueWrapper

  beforeAll(async () => {
    loginState = CreateLoginState([Roles.ADMIN])
    wrapper = await getWrapper(loginState)
  })

  it('編集画面に遷移できる', async () => {
    // Arrange
    // Act
    await flushPromises()
    // Assert
    expect(wrapper.html()).toContain('カタログアイテム編集')
  })

  it('更新ボタンを押下__確認モーダルが開く', async () => {
    // Arrange
    const editButton = wrapper.findAll('button')[1]
    // Act
    await editButton.trigger('click')
    // Assert
    expect(wrapper.html()).toContain('カタログアイテムを更新します。')
  })

  it('更新成功__通知モーダルが開く', async () => {
    // Arrange
    // Act
    await wrapper
      .findAllComponents({ name: 'ConfirmationModal' })[1]
      .findAll('button')[0]
      .trigger('click')
    await flushPromises()
    await vi.waitUntil(() =>
      wrapper.findAllComponents({ name: 'NotificationModal' })[1].isVisible(),
    )
    // Assert
    expect(wrapper.findAllComponents({ name: 'ConfirmationModal' })[1].isVisible()).toBeFalsy()
    expect(wrapper.html()).toContain('カタログアイテムを更新しました。')
  })

  it('OKボタンを押下__通知モーダルが閉じる', async () => {
    // Act
    await wrapper
      .findAllComponents({ name: 'NotificationModal' })[1]
      .findAll('button')[0]
      .trigger('click')
    // Assert
    expect(wrapper.findAllComponents({ name: 'NotificationModal' })[1].isVisible()).toBeFalsy()
  })
})

describe('ゲストロール_アイテム更新ボタンが非活性', () => {
  let loginState: TestingPinia
  let wrapper: VueWrapper

  beforeAll(async () => {
    loginState = CreateLoginState(['ROLE_GUEST'])
    wrapper = await getWrapper(loginState)
  })

  it('編集画面に遷移できる', async () => {
    // Arrange
    // Act
    await flushPromises()
    // Assert
    expect(wrapper.html()).toContain('カタログアイテム編集')
  })

  it('更新ボタンが非活性', () => {
    // Arrange
    // Act
    const editButton = wrapper.findAll('button')[1]
    // Assert
    expect(editButton.attributes('disabled')).toBeDefined()
  })
})
