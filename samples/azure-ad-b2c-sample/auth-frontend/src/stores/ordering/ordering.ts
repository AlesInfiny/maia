import { defineStore } from 'pinia';
import type { OrderResponse, PostOrderRequest } from '@/generated/api-client';
import { ordersApi } from '@/api-client';

export const useOrderingStore = defineStore({
  id: 'ordering',
  state: () => ({
    lastOrder: undefined as OrderResponse | undefined,
  }),
  actions: {
    async order(
      fullName: string,
      postalCode: string,
      todofuken: string,
      shikuchoson: string,
      azanaAndOthers: string,
    ) {
      const postOrderInput: PostOrderRequest = {
        fullName: fullName,
        postalCode: postalCode,
        todofuken: todofuken,
        shikuchoson: shikuchoson,
        azanaAndOthers: azanaAndOthers,
      };
      const orderResponse = await ordersApi.postOrder(postOrderInput);
      const url = new URL(orderResponse.headers.location);
      const orderId = Number(url.pathname.split('/').pop());
      const orderResultResponse = await ordersApi.getById(orderId);
      this.lastOrder = orderResultResponse.data;
    },
    clearLastOrder() {
      this.lastOrder = undefined;
    },
  },
  getters: {
    getLastOrder(state) {
      return state.lastOrder;
    },
  },
});
