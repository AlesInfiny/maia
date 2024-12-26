import { describe, it, expect, vi, beforeAll } from 'vitest';
import { flushPromises, mount, VueWrapper } from '@vue/test-utils';
import { createPinia, setActivePinia } from 'pinia';
import { createCustomErrorHandler } from '@/shared/error-handler/custom-error-handler';
import ItemsEditView from '@/views/catalog/ItemsEditView.vue';
import { router } from '@/router';

async function getWrapper() {
  const pinia = createPinia();
  setActivePinia(pinia);
  const customErrorHandler = createCustomErrorHandler();
  router.push({ name: 'catalog/items/edit', params: { itemId: 1 } });
  await router.isReady();
  return mount(ItemsEditView, {
    global: { plugins: [pinia, router, customErrorHandler] },
  });
}

describe('アイテムが削除できる', () => {
  let wrapper: VueWrapper;

  beforeAll(async () => {
    wrapper = await getWrapper();
  });

  it('編集画面に遷移できる', async () => {
    // Arrange
    // Act
    await flushPromises();
    // Assert
    expect(wrapper.html()).toContain('カタログアイテム編集');
  });

  it('削除ボタンを押下_確認モーダルが開く', async () => {
    // Arrange
    // Act
    await wrapper.findAll('button')[0].trigger('click');
    // Assert
    expect(wrapper.html()).toContain('カタログアイテムを削除します。');
  });

  it('OKボタンを押下_確認モーダルが閉じる', async () => {
    // Arrange
    // Act
    await wrapper
      .findAllComponents({ name: 'ConfirmationModal' })[0]
      .findAll('button')[0]
      .trigger('click');
    await flushPromises();
    // Assert
    expect(
      wrapper.findAllComponents({ name: 'ConfirmationModal' })[0].isVisible(),
    ).toBeFalsy();
  });

  it('削除成功_通知モーダルが開く', async () => {
    // Arrange
    // Act
    await vi.waitUntil(() =>
      wrapper.findAllComponents({ name: 'NotificationModal' })[0].isVisible(),
    );
    // Assert
    expect(wrapper.html()).toContain('カタログアイテムを削除しました。');
  });

  it('OKボタンを押下__通知モーダルが閉じる', async () => {
    // Act
    await wrapper
      .findAllComponents({ name: 'NotificationModal' })[0]
      .findAll('button')[0]
      .trigger('click');
    // Assert
    expect(
      wrapper.findAllComponents({ name: 'NotificationModal' })[0].isVisible(),
    ).toBeFalsy();
  });
});

describe('アイテムが更新できる', () => {
  let wrapper: VueWrapper;

  beforeAll(async () => {
    wrapper = await getWrapper();
  });

  it('編集画面に遷移できる', async () => {
    // Arrange
    // Act
    await flushPromises();
    // Assert
    expect(wrapper.html()).toContain('カタログアイテム編集');
  });

  it('更新ボタンを押下__確認モーダルが開く', async () => {
    // Arrange
    const editButton = wrapper.findAll('button')[1];
    // Act
    await editButton.trigger('click');
    // Assert
    expect(wrapper.html()).toContain('カタログアイテムを更新します。');
  });

  it('更新成功__通知モーダルが開く', async () => {
    // Arrange
    // Act
    await wrapper
      .findAllComponents({ name: 'ConfirmationModal' })[1]
      .findAll('button')[0]
      .trigger('click');
    await flushPromises();
    await vi.waitUntil(() =>
      wrapper.findAllComponents({ name: 'NotificationModal' })[1].isVisible(),
    );
    // Assert
    expect(
      wrapper.findAllComponents({ name: 'ConfirmationModal' })[1].isVisible(),
    ).toBeFalsy();
    expect(wrapper.html()).toContain('カタログアイテムを更新しました。');
  });

  it('OKボタンを押下__通知モーダルが閉じる', async () => {
    // Act
    await wrapper
      .findAllComponents({ name: 'NotificationModal' })[1]
      .findAll('button')[0]
      .trigger('click');
    // Assert
    expect(
      wrapper.findAllComponents({ name: 'NotificationModal' })[1].isVisible(),
    ).toBeFalsy();
  });
});
