import { describe, it, expect } from 'vitest';
import { mount } from '@vue/test-utils';
import { router } from '@/router';
import App from '../App.vue';

describe('App.vue', () => {
  it('トップページに遷移する', async () => {
    const wrapper = mount(App, { global: { plugins: [router] } });
    await router.isReady();
    expect(wrapper.html()).toContain('Dressca 管理 トップ');
  });
});
