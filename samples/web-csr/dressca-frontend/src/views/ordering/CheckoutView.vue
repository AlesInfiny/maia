<script setup lang="ts">
import { onMounted, reactive, toRefs } from 'vue';
import { useBasketStore } from '@/stores/basket/basket';
import { useAccountStore } from '@/stores/account/account';
import type { BasketDto } from '@/api-client/models/basket-dto';

import { useOrderingStore } from '@/stores/ordering/ordering';
import { useRouter } from 'vue-router';
import currencyHelper from '@/shared/helpers/currencyHelper';
import assetHelper from '@/shared/helpers/assetHelper';

const accountStore = useAccountStore();

const state = reactive({
  basket: {} as BasketDto,
  address: accountStore.getAddress,
});

const { basket, address } = toRefs(state);
const router = useRouter();
const { toCurrencyJPY } = currencyHelper();
const { getFirstAssetUrl } = assetHelper();

const orderingStore = useOrderingStore();

const checkout = async () => {
  await orderingStore.order(
    address.value.fullName,
    address.value.postalCode,
    address.value.todofuken,
    address.value.shikuchoson,
    address.value.azanaAndOthers,
  );
  router.push({ name: 'ordering/done' });
};

const basketStore = useBasketStore();

onMounted(async () => {
  await basketStore.fetch();
  const basket = basketStore.getBasket;
  if (basket.basketItems?.length === 0) {
    router.push('/');
    return;
  }
  state.basket = basket;
});
</script>

<template>
  <div class="container mx-auto my-4 max-w-4xl">
    <span class="text-lg font-medium text-green-500">
      注文内容を確認して「注文を確定する」ボタンを押してください。
    </span>
  </div>
  <div class="container mx-auto my-4 max-w-4xl">
    <div
      class="mx-2 grid grid-cols-2 lg:grid-cols-3 lg:gap-x-12 flex items-center"
    >
      <table
        class="lg:row-start-1 lg:col-span-1 table-fixed mt-2 lg:mt-0 border-t border-b lg:border"
      >
        <tbody>
          <tr>
            <td>税抜き合計</td>
            <td class="text-right">
              {{ toCurrencyJPY(basket.account?.totalItemsPrice) }}
            </td>
          </tr>
          <tr>
            <td>送料</td>
            <td class="text-right">
              {{ toCurrencyJPY(basket.account?.deliveryCharge) }}
            </td>
          </tr>
          <tr>
            <td>消費税</td>
            <td class="text-right">
              {{ toCurrencyJPY(basket.account?.consumptionTax) }}
            </td>
          </tr>
          <tr>
            <td>合計</td>
            <td class="text-right text-xl font-bold text-red-500">
              {{ toCurrencyJPY(basket.account?.totalPrice) }}
            </td>
          </tr>
        </tbody>
      </table>
      <button
        class="lg:col-end-3 mx-auto w-36 bg-orange-500 hover:bg-amber-700 text-white font-bold py-2 px-4 rounded"
        @click="checkout()"
      >
        注文を確定する
      </button>
      <table
        class="lg:col-span-3 table-fixed mt-2 lg:mt-4 border-t border-b lg:border"
      >
        <tbody>
          <tr>
            <td rowspan="5" class="w-24 pl-2 border-r">お届け先</td>
            <td class="pl-2">{{ address.fullName }}</td>
          </tr>
          <tr>
            <td class="pl-2">{{ `〒${address.postalCode}` }}</td>
          </tr>
          <tr>
            <td class="pl-2">{{ address.todofuken }}</td>
          </tr>
          <tr>
            <td class="pl-2">{{ address.shikuchoson }}</td>
          </tr>
          <tr>
            <td class="pl-2">{{ address.azanaAndOthers }}</td>
          </tr>
        </tbody>
      </table>
    </div>
    <div class="mt-8 mx-2">
      <div
        v-for="item in basket.basketItems"
        :key="item.catalogItemId"
        class="grid grid-cols-5 lg:grid-cols-8 mt-4 flex items-center"
      >
        <div class="col-span-4 lg:col-span-5">
          <div class="grid grid-cols-2">
            <img
              :src="getFirstAssetUrl(item.catalogItem?.assetCodes)"
              class="h-[150px] pointer-events-none"
            />
            <div class="ml-2">
              <p>{{ item.catalogItem?.name }}</p>
              <p class="mt-4">
                {{ `価格: ${toCurrencyJPY(item.unitPrice)}` }}
              </p>
              <p class="mt-4">{{ `数量: ${item.quantity}` }}</p>
              <p class="mt-4">
                {{ toCurrencyJPY(item.subTotal) }}
              </p>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>
