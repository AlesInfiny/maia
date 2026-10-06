import { describe, it, expect, beforeEach } from 'vitest'
import { mount, RouterLinkStub } from '@vue/test-utils'
import { createPinia, setActivePinia, type Pinia } from 'pinia'
import { useAuthenticationStore } from '../../stores/authentication'
import AuthenticationMenu from '../AuthenticationMenu.vue'

let pinia: Pinia

beforeEach(() => {
  sessionStorage.clear()
  pinia = createPinia()
  setActivePinia(pinia)
})

/**
 * メニューをマウントします。
 * コンテキストはルーターを参照できないため、リンクはスタブに置き換えます。
 * @returns マウント済みのラッパー。
 */
function mountMenu() {
  return mount(AuthenticationMenu, {
    props: { loginLocation: '/authentication/login' },
    global: { plugins: [pinia], stubs: { RouterLink: RouterLinkStub } },
  })
}

describe('AuthenticationMenu', () => {
  it('未認証_ログインのリンクを指定した遷移先で表示する', () => {
    // Arrange
    // Act
    const wrapper = mountMenu()
    // Assert
    const link = wrapper.getComponent(RouterLinkStub)
    expect(link.text()).toBe('ログイン')
    expect(link.props('to')).toBe('/authentication/login')
  })

  it('認証済みでログアウトする_未認証になりloggedOutを発行する', async () => {
    // Arrange
    useAuthenticationStore(pinia).signIn()
    const wrapper = mountMenu()
    // Act
    await wrapper.get('button').trigger('click')
    // Assert
    expect(useAuthenticationStore(pinia).isAuthenticated).toBe(false)
    expect(wrapper.emitted('loggedOut')).toHaveLength(1)
  })
})
