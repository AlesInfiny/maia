<script setup lang="ts">
import { onMounted, onUnmounted, reactive } from 'vue';
import {
  fetchBasket,
  removeItemFromBasket,
  updateItemInBasket,
} from '@/services/basket/basket-service';
import { useBasketStore } from '@/stores/basket/basket';
import { useRouter } from 'vue-router';
import BasketItem from '@/components/basket/BasketItem.vue';
import Loading from '@/components/common/LoadingSpinner.vue';
import currencyHelper from '@/shared/helpers/currencyHelper';
import assetHelper from '@/shared/helpers/assetHelper';
import { storeToRefs } from 'pinia';

const state = reactive({
  showLoading: true,
});

const basketStore = useBasketStore();
const { getBasket, getAddedItem, getAddedItemId } = storeToRefs(basketStore);

const router = useRouter();
const { toCurrencyJPY } = currencyHelper();
const { getFirstAssetUrl } = assetHelper();

const isEmpty = () => {
  return getBasket.value.basketItems?.length === 0;
};

const goCatalog = () => {
  router.push({ name: 'catalog' });
};

const update = async (catalogItemId: number, newQuantity: number) => {
  await updateItemInBasket(catalogItemId, newQuantity);
};

const remove = async (catalogItemId: number) => {
  await removeItemFromBasket(catalogItemId);
};

const order = () => {
  router.push({ name: 'ordering/checkout' });
};

onMounted(async () => {
  state.showLoading = true;
  try {
    await fetchBasket();
  } catch (error) {
    console.error(error);
  } finally {
    state.showLoading = false;
  }
});

onUnmounted(async () => {
  basketStore.deleteAddedItemId();
});
</script>

<template>
  <div class="container mx-auto my-4 max-w-4xl">
    <Loading :show="state.showLoading"></Loading>
    <div v-if="!state.showLoading">
      <div v-if="getAddedItemId && !!getAddedItem" class="mx-2">
        <span class="text-lg font-medium text-green-500"
          >以下の商品が追加されました。</span
        >
        <div class="grid grid-cols-1 lg:grid-cols-3 mt-4 flex items-center">
          <img
            :src="getFirstAssetUrl(getAddedItem.catalogItem?.assetCodes)"
            class="h-[150px] m-auto pointer-events-none"
          />
          <span class="text-center lg:text-left">{{
            getAddedItem.catalogItem?.name
          }}</span>
          <span class="text-center lg:text-left">{{
            toCurrencyJPY(getAddedItem.unitPrice)
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
          v-for="item in getBasket.basketItems"
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
              <td>{{ toCurrencyJPY(getBasket.account?.totalItemsPrice) }}</td>
            </tr>
            <tr>
              <th>送料</th>
              <td>{{ toCurrencyJPY(getBasket.account?.deliveryCharge) }}</td>
            </tr>
            <tr>
              <th>消費税</th>
              <td>{{ toCurrencyJPY(getBasket.account?.consumptionTax) }}</td>
            </tr>
            <tr>
              <th>合計</th>
              <td class="">
                {{ toCurrencyJPY(getBasket.account?.totalPrice) }}
              </td>
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
