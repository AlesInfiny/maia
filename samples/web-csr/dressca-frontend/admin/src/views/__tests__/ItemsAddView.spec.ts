import { describe, it, expect } from 'vitest';
import { flushPromises, mount } from '@vue/test-utils';
import { router } from '@/router';
import { createPinia, setActivePinia } from 'pinia';
import { createCustomErrorHandler } from '@/shared/error-handler/custom-error-handler';
import ItemsAddView from '@/views/catalog/ItemsAddView.vue';

describe('アイテム追加画面のテスト', () => {
  it('アイテムを追加できる', async () => {
    const pinia = createPinia();
    setActivePinia(pinia);
    const customErrorHandler = createCustomErrorHandler();
    const wrapper = mount(ItemsAddView, {
      global: { plugins: [pinia, router, customErrorHandler] },
    });
    await flushPromises();
    expect(wrapper.html()).toContain('カタログアイテム追加');
    wrapper.find('button').trigger('click');
    await flushPromises();
    expect(wrapper.html()).toContain('カタログアイテムを追加しました。');
  });
});
