import { describe, expect, it } from 'vitest'
import { mount } from '@vue/test-utils'
import { router } from '@/router'
import LoginView from '@/views/authentication/LoginView.vue'

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

describe('LoginView', () => {
  it('ログイン画面を表示できる', async () => {
    const wrapper = await getWrapper()
    expect(wrapper.text()).toContain('ログイン')
  })
})
