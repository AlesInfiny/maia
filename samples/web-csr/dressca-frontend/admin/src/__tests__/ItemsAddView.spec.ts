import { describe, it, expect } from 'vitest';
import { flushPromises, mount } from '@vue/test-utils';
import { router } from '@/router';
import { createPinia, setActivePinia } from 'pinia';
import ItemsAddView from '../views/catalog/ItemsAddView.vue';

describe('アイテム追加画面', () => {
  it('アイテムを追加できる', async () => {
    const pinia = createPinia();
    setActivePinia(pinia);
    const wrapper = mount(ItemsAddView, {
      global: { plugins: [pinia, router] },
    });
    await flushPromises();
    expect(wrapper.html()).toContain('カタログアイテム追加');
    await wrapper.find('button').trigger('click');
    await flushPromises();
    expect(wrapper.html()).toContain('カタログアイテムを追加しました。');
  });
});
