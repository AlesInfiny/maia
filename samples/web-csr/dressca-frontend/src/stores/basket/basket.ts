import { defineStore } from 'pinia';
import type {
  BasketResponse,
  PutBasketItemRequest,
  PostBasketItemsRequest,
} from '@/generated/api-client';
import { basketItemApi } from '@/api-client';

export const useBasketStore = defineStore({
  id: 'basket',
  state: () => ({
    basket: {} as BasketResponse,
  }),
  actions: {
    async add(catalogItemId: number) {
      const params: PostBasketItemsRequest = {
        catalogItemId: catalogItemId,
        addedQuantity: 1,
      };
      await basketItemApi.postBasketItem(params);
    },
    async update(catalogItemId: number, newQuantity: number) {
      const params: PutBasketItemRequest[] = [
        {
          catalogItemId: catalogItemId,
          quantity: newQuantity,
        },
      ];
      await basketItemApi.putBasketItem(params);
    },
    async remove(catalogItemId: number) {
      await basketItemApi.deleteBasketItemAsync(catalogItemId);
    },
    async fetch() {
      const response = await basketItemApi.getBasketItems();
      this.basket = response.data;
    },
  },
  getters: {
    getBasket(state): BasketResponse {
      return state.basket;
    },
  },
});
