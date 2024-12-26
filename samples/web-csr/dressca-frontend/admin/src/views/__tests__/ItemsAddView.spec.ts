import { describe, it, expect, vi, beforeAll } from 'vitest';
import { flushPromises, mount, VueWrapper } from '@vue/test-utils';
import { router } from '@/router';
import { createPinia, setActivePinia } from 'pinia';
import { createCustomErrorHandler } from '@/shared/error-handler/custom-error-handler';
import ItemsAddView from '@/views/catalog/ItemsAddView.vue';

async function getWrapper() {
  const pinia = createPinia();
  setActivePinia(pinia);
  const customErrorHandler = createCustomErrorHandler();
  return mount(ItemsAddView, {
    global: { plugins: [pinia, router, customErrorHandler] },
  });
}

describe('アイテムを追加できる', () => {
  let wrapper: VueWrapper;

  beforeAll(async () => {
    wrapper = await getWrapper();
  });

  it('追加画面に遷移できる', async () => {
    // Arrange
    // Act
    await flushPromises();
    // Assert
    expect(wrapper.html()).toContain('カタログアイテム追加');
  });

  it('追加ボタンを押下_追加成功_通知モーダルが開く', async () => {
    // Arrange
    // Act
    wrapper.find('button').trigger('click');
    await flushPromises();
    await vi.waitUntil(() =>
      wrapper.findAllComponents({ name: 'NotificationModal' })[0].isVisible(),
    );
    // Assert
    expect(wrapper.html()).toContain('カタログアイテムを追加しました。');
  });
});
