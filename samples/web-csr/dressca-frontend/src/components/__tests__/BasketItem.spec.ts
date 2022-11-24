import { describe, it, expect } from 'vitest';

import { mount } from '@vue/test-utils';
import BasketItem from '@/components/basket/BasketItem.vue';
import type { BasketItemDto } from '@/api-client/models/basket-item-dto';

describe('HelloWorld', () => {
  it('小計が日本円形式で表示できる', () => {
    const basketItemDto: BasketItemDto = {
      catalogItemId: 10,
      quantity: 2,
      subTotal: 100000,
      unitPrice: 50000,
    };
    const wrapper = mount(BasketItem, { props: { item: basketItemDto } });
    expect(wrapper.text()).toContain('￥100,000');
  });

  it('単価が日本円形式で表示できる', () => {
    const basketItemDto: BasketItemDto = {
      catalogItemId: 11,
      quantity: 3,
      subTotal: 150000,
      unitPrice: 50000,
    };
    const wrapper = mount(BasketItem, { props: { item: basketItemDto } });
    expect(wrapper.text()).toContain('￥50,000');
  });
});
