import { describe, it, expect, beforeAll } from 'vitest';
import { flushPromises, mount, VueWrapper } from '@vue/test-utils';
import { router } from '@/router';
import { createPinia, setActivePinia } from 'pinia';
import { createCustomErrorHandler } from '@/shared/error-handler/custom-error-handler';
import ItemsView from '@/views/catalog/ItemsView.vue';

async function getWrapper() {
  const pinia = createPinia();
  setActivePinia(pinia);
  const customErrorHandler = createCustomErrorHandler();
  return mount(ItemsView, {
    global: { plugins: [pinia, router, customErrorHandler] },
  });
}
describe('アイテム一覧が表示できる', () => {
  let wrapper: VueWrapper;

  beforeAll(async () => {
    wrapper = await getWrapper();
  });

  it('アイテム一覧画面に遷移できる', async () => {
    // Arrange
    // Act
    await flushPromises();
    // Assert
    expect(wrapper.html()).toContain('カタログアイテム一覧');
  });

  it('アイテムが取得した個数分表示される', async () => {
    // Arrange
    const expectedItemCount = 11;
    // Act
    const tableRows = wrapper.find('tbody').findAll('tr');
    // Assert
    expect(tableRows.length).toBe(expectedItemCount);
  });
});
