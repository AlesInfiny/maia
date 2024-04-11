import { defineStore } from 'pinia';
import type {
  BasketResponse,
  PutBasketItemsRequest,
  PostBasketItemsRequest,
} from '@/generated/api-client';
import { basketItemsApi } from '@/api-client';

export const useBasketStore = defineStore({
  id: 'basket',
  state: () => ({
    basket: {} as BasketResponse,
    addedItemId: undefined as number | undefined,
  }),
  actions: {
    async add(catalogItemId: number) {
      const params: PostBasketItemsRequest = {
        catalogItemId: catalogItemId,
        addedQuantity: 1,
      };
      await basketItemsApi.postBasketItem(params);
      this.addedItemId = catalogItemId;
    },
    async update(catalogItemId: number, newQuantity: number) {
      const params: PutBasketItemsRequest[] = [
        {
          catalogItemId: catalogItemId,
          quantity: newQuantity,
        },
      ];
      await basketItemsApi.putBasketItem(params);
    },
    async remove(catalogItemId: number) {
      await basketItemsApi.deleteBasketItem(catalogItemId);
    },
    async fetch() {
      const response = await basketItemsApi.getBasketItems();
      this.basket = response.data;
    },
    async deleteAddedItemId() {
      this.addedItemId = undefined;
    },
  },
  getters: {
    getBasket(state): BasketResponse {
      return state.basket;
    },
    getAddedItemId(state): number | undefined {
      return state.addedItemId;
    },
  },
});
