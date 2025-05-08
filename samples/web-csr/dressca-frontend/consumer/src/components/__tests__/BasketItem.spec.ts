import { describe, it, expect } from 'vitest';

import { mount } from '@vue/test-utils';
import type { BasketItemResponse } from '@/generated/api-client/models/basket-item-response';
import BasketItem from '../basket/BasketItem.vue';

describe('HelloWorld', () => {
  it('小計が日本円形式で表示できる', () => {
    const basketItemResponse: BasketItemResponse = {
      catalogItemId: 10,
      quantity: 2,
      subTotal: 100000,
      unitPrice: 50000,
    };
    const available: boolean = true;
    const wrapper = mount(BasketItem, {
      props: { item: basketItemResponse, available },
    });
    expect(wrapper.text()).toContain('￥100,000');
  });

  it('単価が日本円形式で表示できる', () => {
    const basketItemResponse: BasketItemResponse = {
      catalogItemId: 11,
      quantity: 3,
      subTotal: 150000,
      unitPrice: 50000,
    };
    const available: boolean = true;
    const wrapper = mount(BasketItem, {
      props: { item: basketItemResponse, available },
    });
    expect(wrapper.text()).toContain('￥50,000');
  });
});
