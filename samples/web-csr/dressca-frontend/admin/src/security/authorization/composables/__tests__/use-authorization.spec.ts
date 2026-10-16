import { describe, it, expect, beforeEach } from 'vitest'
import { defineComponent, h, nextTick } from 'vue'
import { mount } from '@vue/test-utils'
import { createPinia, setActivePinia } from 'pinia'
import { useAuthenticationStore } from '../../stores/authentication'
import { Roles } from '../../constants/roles'
import { useAuthorization } from '../use-authorization'

beforeEach(() => {
  sessionStorage.clear()
  setActivePinia(createPinia())
})

describe('useAuthorization', () => {
  it('管理者ロールを持つ_管理者と判定する', () => {
    // Arrange
    useAuthenticationStore().userRoles = [Roles.ADMIN]
    // Act
    const { isAdmin } = useAuthorization()
    // Assert
    expect(isAdmin.value).toBe(true)
  })

  it('管理者ロールを持たない_管理者ではないと判定する', () => {
    // Arrange
    useAuthenticationStore().userRoles = ['ROLE_GUEST']
    // Act
    const { isAdmin } = useAuthorization()
    // Assert
    expect(isAdmin.value).toBe(false)
  })

  it('判定後にロールが変わる_変更後のロールで判定する', () => {
    // Arrange
    const store = useAuthenticationStore()
    store.userRoles = ['ROLE_GUEST']
    const { isAdmin } = useAuthorization()
    expect(isAdmin.value).toBe(false)
    // Act
    store.userRoles = [Roles.ADMIN]
    // Assert
    expect(isAdmin.value).toBe(true)
  })

  it('描画中に判定する_ロールの変化に追随して再描画される', async () => {
    // Arrange
    const store = useAuthenticationStore()
    store.userRoles = ['ROLE_GUEST']
    const wrapper = mount(
      defineComponent({
        setup() {
          const { isAdmin } = useAuthorization()
          return () => h('span', isAdmin.value ? 'admin' : 'guest')
        },
      }),
    )
    expect(wrapper.text()).toBe('guest')
    // Act
    store.userRoles = [Roles.ADMIN]
    await nextTick()
    // Assert
    expect(wrapper.text()).toBe('admin')
  })
})
