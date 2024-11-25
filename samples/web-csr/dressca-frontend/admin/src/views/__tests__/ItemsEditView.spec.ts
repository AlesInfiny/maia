import { describe, it, expect } from 'vitest';
import { flushPromises, mount } from '@vue/test-utils';
import { createPinia, setActivePinia } from 'pinia';
import { createCustomErrorHandler } from '@/shared/error-handler/custom-error-handler';
import ItemsEditView from '@/views/catalog/ItemsEditView.vue';
import { router } from '@/router';

describe('アイテム編集画面_アイテム削除機能のテスト', () => {
  it('アイテムを削除できる', async () => {
    const pinia = createPinia();
    setActivePinia(pinia);
    const customErrorHandler = createCustomErrorHandler();
    router.push({ name: 'catalog/items/edit', params: { itemId: 1 } });
    await router.isReady();
    const wrapper = mount(ItemsEditView, {
      global: { plugins: [pinia, router, customErrorHandler] },
    });
    await flushPromises();
    expect(wrapper.html()).toContain('カタログアイテム編集');
    await wrapper.findAll('button')[0].trigger('click');
    expect(wrapper.html()).toContain('カタログアイテムを削除します。');
    await wrapper
      .findAllComponents({ name: 'ConfirmationModal' })[0]
      .findAll('button')[0]
      .trigger('click');
    await flushPromises();
    expect(
      wrapper.findAllComponents({ name: 'ConfirmationModal' })[0].isVisible(),
    ).toBeFalsy();
    expect(wrapper.html()).toContain('カタログアイテムを削除しました。');
    await wrapper
      .findAllComponents({ name: 'NotificationModal' })[0]
      .findAll('button')[0]
      .trigger('click');
    expect(
      wrapper.findAllComponents({ name: 'NotificationModal' })[0].isVisible(),
    ).toBeFalsy();
  });
});

describe('アイテム編集画面_アイテム更新機能のテスト', () => {
  it('アイテムを更新できる', async () => {
    const pinia = createPinia();
    setActivePinia(pinia);
    const customErrorHandler = createCustomErrorHandler();
    router.push({ name: 'catalog/items/edit', params: { itemId: 1 } });
    await router.isReady();
    const wrapper = mount(ItemsEditView, {
      global: { plugins: [pinia, router, customErrorHandler] },
    });
    await flushPromises();
    expect(wrapper.html()).toContain('カタログアイテム編集');
    const editButton = wrapper.findAll('button')[1];
    await editButton.trigger('click');
    expect(wrapper.html()).toContain('カタログアイテムを更新します。');
    await wrapper
      .findAllComponents({ name: 'ConfirmationModal' })[1]
      .findAll('button')[0]
      .trigger('click');
    await flushPromises();
    expect(
      wrapper.findAllComponents({ name: 'ConfirmationModal' })[1].isVisible(),
    ).toBeFalsy();
    expect(wrapper.html()).toContain('カタログアイテムを更新しました。');
    await wrapper
      .findAllComponents({ name: 'NotificationModal' })[1]
      .findAll('button')[0]
      .trigger('click');
    expect(
      wrapper.findAllComponents({ name: 'NotificationModal' })[1].isVisible(),
    ).toBeFalsy();
  });
});
