import { describe, it, expect, beforeEach } from 'vitest';
import { mount } from '@vue/test-utils';
import type { BasketItemResponse } from '@/generated/api-client/models/basket-item-response';
import { i18n } from '@/locales/i18n';
import BasketItem from '../basket/BasketItem.vue';

function createBasketItemResponse(): BasketItemResponse {
  return {
    catalogItemId: 10,
    quantity: 2,
    subTotal: 100000,
    unitPrice: 50000,
  };
}

describe('BasketItem', () => {
  beforeEach(() => {
    i18n.global.locale.value = 'ja';
  });

  it('小計が日本円形式で表示できる', () => {
    // Arrange
    const basketItemResponse = createBasketItemResponse();
    const available = true;

    // Act
    const wrapper = mount(BasketItem, {
      props: { item: basketItemResponse, available },
      global: { plugins: [i18n] },
    });

    // Assert
    expect(wrapper.text()).toContain('￥100,000');
  });

  it('単価が日本円形式で表示できる', () => {
    // Arrange
    const basketItemResponse = createBasketItemResponse();
    const available = true;

    // Act
    const wrapper = mount(BasketItem, {
      props: { item: basketItemResponse, available },
      global: { plugins: [i18n] },
    });

    // Assert
    expect(wrapper.text()).toContain('￥50,000');
  });

  it('販売中止中のメッセージが表示できる', () => {
    // Arrange
    const basketItemResponse = createBasketItemResponse();
    const available = false;

    // Act
    const wrapper = mount(BasketItem, {
      props: { item: basketItemResponse, available },
      global: { plugins: [i18n] },
    });

    // Assert
    expect(wrapper.text()).toContain(
      'こちらの商品は現在販売しておりません。買い物かごから削除してください。',
    );
  });
});
