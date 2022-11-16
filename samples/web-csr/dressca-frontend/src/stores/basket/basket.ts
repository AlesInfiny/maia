import { defineStore } from 'pinia';
import axios from 'axios';
import type { BasketDto } from '@/api-client/models/basket-dto';
import type { PutBasketItemRequest } from '@/api-client/models/put-basket-item-request';
import type { PostBasketItemsRequest } from '@/api-client/models/post-basket-items-request';

export const useBasketStore = defineStore({
  id: 'basket',
  state: () => ({
    basket: {} as BasketDto,
  }),
  actions: {
    async add(catalogItemId: number) {
      const params: PostBasketItemsRequest = {
        catalogItemId: catalogItemId,
        addedQuantity: 1,
      };
      await axios.post('basket-items', params);
    },
    async update(catalogItemId: number, newQuantity: number) {
      const params: PutBasketItemRequest[] = [
        {
          catalogItemId: catalogItemId,
          quantity: newQuantity,
        },
      ];
      await axios.put('basket-items', params);
    },
    async remove(catalogItemId: number) {
      await axios.delete(`/basket-items/${catalogItemId}`);
    },
    async fetch() {
      const response = await axios.get('basket-items');
      this.basket = response.data;
    },
  },
  getters: {
    getBasket(state): BasketDto {
      return state.basket;
    },
  },
});
