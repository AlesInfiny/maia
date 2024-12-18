import { describe, it, expect } from 'vitest';
import { flushPromises, mount } from '@vue/test-utils';
import { router } from '@/router';
import { createPinia, setActivePinia } from 'pinia';
import { createCustomErrorHandler } from '@/shared/error-handler/custom-error-handler';
import ItemsView from '@/views/catalog/ItemsView.vue';

describe('アイテム一覧画面のテスト', () => {
  it('アイテム一覧が表示できる', async () => {
    const pinia = createPinia();
    setActivePinia(pinia);
    const customErrorHandler = createCustomErrorHandler();
    const wrapper = mount(ItemsView, {
      global: { plugins: [pinia, router, customErrorHandler] },
    });
    await flushPromises();
    expect(wrapper.html()).toContain('カタログアイテム一覧');
  });
});
