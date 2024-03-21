<script setup lang="ts">
import { onMounted, onUnmounted, reactive, toRefs } from 'vue';
import { useBasketStore } from '@/stores/basket/basket';
import type { BasketResponse } from '@/generated/api-client/models/basket-response';
import type { BasketItemResponse } from '@/generated/api-client/models/basket-item-response';
import { useRouter } from 'vue-router';
import BasketItem from '@/components/basket/BasketItem.vue';
import Loading from '@/components/common/LoadingSpinner.vue';
import currencyHelper from '@/shared/helpers/currencyHelper';
import assetHelper from '@/shared/helpers/assetHelper';

const state = reactive({
  basket: {} as BasketResponse,
  added: null as BasketItemResponse | null,
  showLoading: true,
});

const { basket, added } = toRefs(state);
const router = useRouter();
const { toCurrencyJPY } = currencyHelper();
const { getFirstAssetUrl } = assetHelper();

const isEmpty = () => {
  return (
    typeof state.basket.basketItems === 'undefined' ||
    state.basket.basketItems.length === 0
  );
};

const goCatalog = () => {
  router.push({ name: 'catalog' });
};

const update = async (catalogItemId: number, newQuantity: number) => {
  state.added = null;
  await basketStore.update(catalogItemId, newQuantity);
  await basketStore.fetch();
  state.basket = basketStore.getBasket;
};

const remove = async (catalogItemId: number) => {
  state.added = null;
  await basketStore.remove(catalogItemId);
  await basketStore.fetch();
  state.basket = basketStore.getBasket;
};

const order = () => {
  router.push({ name: 'ordering/checkout' });
};

const basketStore = useBasketStore();

onMounted(async () => {
  state.showLoading = true;
  await basketStore.fetch();
  state.basket = JSON.parse(JSON.stringify(basketStore.getBasket));

  if (
    typeof basketStore.getAddedItemId !== 'undefined' &&
    typeof basketStore.getBasket.basketItems !== 'undefined'
  ) {
    // eslint-disable-next-line @typescript-eslint/no-non-null-assertion
    state.added = basketStore.getBasket.basketItems.find(
      (item) => item.catalogItemId === basketStore.getAddedItemId,
    )!;
  }
  state.showLoading = false;
});

onUnmounted(async () => {
  basketStore.deleteAddedItemId();
});
</script>

<template>
  <div class="container mx-auto my-4 max-w-4xl">
    <Loading :show="state.showLoading"></Loading>
    <div v-if="!state.showLoading">
      <div v-if="basketStore.getAddedItemId && !!added" class="mx-2">
        <span class="text-lg font-medium text-green-500"
          >以下の商品が追加されました。</span
        >
        <div class="grid grid-cols-1 lg:grid-cols-3 mt-4 flex items-center">
          <img
            :src="getFirstAssetUrl(added.catalogItem?.assetCodes)"
            class="h-[150px] m-auto pointer-events-none"
          />
          <span class="text-center lg:text-left">{{
            added.catalogItem?.name
          }}</span>
          <span class="text-center lg:text-left">{{
            toCurrencyJPY(added.unitPrice)
          }}</span>
        </div>
      </div>

      <div v-if="isEmpty()" class="mt-4 mx-2">
        <span class="text-2xl font-medium">買い物かごに商品がありません。</span>
      </div>
      <div v-if="!isEmpty()" class="mt-8 mx-2">
        <span class="text-2xl font-medium">現在のカートの中身</span>
        <div
          class="hidden lg:grid grid-cols-1 lg:grid-cols-5 mt-4 flex items-center"
        >
          <div class="text-lg font-medium text-center lg:col-span-3">商品</div>
          <div class="text-lg font-medium text-right lg:col-span-1">数量</div>
        </div>
        <div
          v-for="item in basket.basketItems"
          :key="item.catalogItemId"
          class="grid grid-cols-5 lg:grid-cols-8 mt-4 flex items-center"
        >
          <BasketItem
            :item="item"
            @update="update"
            @remove="remove"
          ></BasketItem>
        </div>
        <hr class="mt-4" />
        <div class="mt-4 mr-2 text-right">
          <table class="inline-block border-separate">
            <tr>
              <th>税抜き合計</th>
              <td>{{ toCurrencyJPY(basket.account?.totalItemsPrice) }}</td>
            </tr>
            <tr>
              <th>送料</th>
              <td>{{ toCurrencyJPY(basket.account?.deliveryCharge) }}</td>
            </tr>
            <tr>
              <th>消費税</th>
              <td>{{ toCurrencyJPY(basket.account?.consumptionTax) }}</td>
            </tr>
            <tr>
              <th>合計</th>
              <td class="">{{ toCurrencyJPY(basket.account?.totalPrice) }}</td>
            </tr>
          </table>
        </div>
      </div>
      <div class="flex justify-between">
        <button
          class="w-36 mt-4 ml-4 bg-teal-500 hover:bg-teal-700 text-white font-bold py-2 px-4 rounded"
          @click="goCatalog()"
        >
          買い物を続ける
        </button>
        <span v-if="!isEmpty()">
          <button
            class="w-36 mt-4 mr-4 bg-orange-500 hover:bg-amber-700 text-white font-bold py-2 px-4 rounded"
            @click="order()"
          >
            レジに進む
          </button>
        </span>
      </div>
    </div>
  </div>
</template>
