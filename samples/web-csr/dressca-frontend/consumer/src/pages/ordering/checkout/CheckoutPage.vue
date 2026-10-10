<script setup lang="ts">
import { watch } from 'vue'
import { routeNames } from '@/business-common/router/route-names'
import { useCheckout } from '@/shopping/public-api'
import { showFailureToast } from '@/business-common/services/notification-service'
import { useRouter } from 'vue-router'
import { currencyHelper } from '@/system-common/helpers/currency-helper'
import { assetHelper } from '@/business-common/helpers/asset-helper'
import { i18n } from '@/system-common/locales/i18n'

const router = useRouter()
const { toCurrencyJPY } = currencyHelper()
const { getFirstAssetUrl } = assetHelper()
const { t } = i18n.global
const { status, lines, account, hasUnavailableItems, address, placeOrder } = useCheckout()

/**
 * 買い物かごが空の場合は、陳列品画面へ遷移します。
 * 読み込みに失敗した場合は、利用者に通知してエラー画面へ遷移します。
 */
watch(status, (newStatus) => {
  if (newStatus.kind === 'empty') {
    router.push({ name: routeNames.displayItem })
  } else if (newStatus.kind === 'failed') {
    showFailureToast(newStatus.problem, t('failedToGetCarts'))
    router.push({ name: routeNames.error })
  }
})

const goBasket = () => {
  router.push({ name: routeNames.basket })
}

/**
 * 注文を確定し、注文完了画面へ遷移します。
 * 失敗した場合は、利用者に通知してエラー画面へ遷移します。
 */
const placeOrderAsync = async () => {
  const outcome = await placeOrder()
  switch (outcome.kind) {
    case 'ordered':
      router.push({ name: routeNames.done, params: { orderId: outcome.orderId } })
      break
    case 'failed':
      showFailureToast(outcome.problem, t('failedToOrderItems'))
      router.push({ name: routeNames.error })
      break
    case 'canceled':
      break
  }
}
</script>

<template>
  <div class="container mx-auto my-4 max-w-4xl">
    <p v-if="!hasUnavailableItems" class="mx-2 text-lg font-medium text-green-500">
      {{ t('orderingCheckAndComplete') }}
    </p>
    <p v-if="hasUnavailableItems" class="mx-2 text-lg font-medium text-red-500">
      {{ t('orderingBlockedByUnavailableItems') }}
    </p>
  </div>
  <div class="container mx-auto my-4 max-w-4xl">
    <div class="mx-2 grid grid-cols-2 items-center lg:grid-cols-3 lg:gap-x-12">
      <table
        class="mt-2 table-fixed border-t border-b lg:col-span-1 lg:row-start-1 lg:mt-0 lg:border"
      >
        <tbody>
          <tr>
            <td>税抜き合計</td>
            <td class="text-right">
              {{ toCurrencyJPY(account?.totalItemsPrice) }}
            </td>
          </tr>
          <tr>
            <td>送料</td>
            <td class="text-right">
              {{ toCurrencyJPY(account?.deliveryCharge) }}
            </td>
          </tr>
          <tr>
            <td>消費税</td>
            <td class="text-right">
              {{ toCurrencyJPY(account?.consumptionTax) }}
            </td>
          </tr>
          <tr>
            <td>合計</td>
            <td class="text-right text-xl font-bold text-red-500">
              {{ toCurrencyJPY(account?.totalPrice) }}
            </td>
          </tr>
        </tbody>
      </table>
      <div class="flex flex-col items-center gap-2 lg:col-end-3">
        <button
          class="w-36 rounded-sm px-4 py-2 font-bold text-white"
          :class="{
            'bg-teal-500 hover:bg-teal-700 disabled:bg-teal-300/50': hasUnavailableItems,
            'bg-orange-500 hover:bg-amber-700 disabled:bg-orange-300/50': !hasUnavailableItems,
          }"
          type="button"
          :disabled="hasUnavailableItems"
          @click="placeOrderAsync()"
        >
          注文を確定する
        </button>
        <button
          class="w-36 rounded-sm px-4 py-2 font-bold text-white"
          :class="{
            'bg-orange-500 hover:bg-amber-700': hasUnavailableItems,
            'bg-teal-500 hover:bg-teal-700': !hasUnavailableItems,
          }"
          type="button"
          @click="goBasket()"
        >
          買い物かごに戻る
        </button>
      </div>
      <table class="mt-2 table-fixed border-t border-b lg:col-span-3 lg:mt-4 lg:border">
        <tbody>
          <tr>
            <td rowspan="5" class="w-24 border-r pl-2">お届け先</td>
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
    <div class="mx-2 mt-8">
      <div
        v-for="item in lines"
        :key="item.displayItemId"
        class="mt-4 grid grid-cols-4 items-center lg:grid-cols-6"
        :class="{
          'bg-red-100': !item.available,
        }"
      >
        <div class="col-span-4 lg:col-span-5">
          <div class="grid grid-cols-3">
            <img
              :src="getFirstAssetUrl(item.assetCodes)"
              :alt="item.name"
              class="pointer-events-none h-40"
            />
            <div class="ml-2">
              <p>{{ item.name }}</p>
              <p class="mt-4">
                {{ `価格: ${toCurrencyJPY(item.unitPrice)}` }}
              </p>
              <p class="mt-4">
                {{ `数量: ${item.quantity}` }}
              </p>
              <p class="mt-4">
                {{ toCurrencyJPY(item.subTotal) }}
              </p>
            </div>
            <p v-if="!item.available" class="font-bold text-red-500">
              {{ t('itemUnavailable') }}
            </p>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>
