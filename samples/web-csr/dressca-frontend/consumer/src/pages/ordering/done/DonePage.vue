<script setup lang="ts">
import { watch } from 'vue'
import { routeNames } from '@/app/router/route-names'
import { useRoute, useRouter } from 'vue-router'
import { i18n } from '@/system-common/locales/i18n'
import { useOrderResult } from '@/shopping/public-api'
import { showFailureToast } from '@/business-common/services/notification-service'
import { currencyHelper } from '@/system-common/helpers/currency-helper'
import { assetHelper } from '@/business-common/helpers/asset-helper'
import { LoadingSpinnerOverlay } from '@/system-common/components/LoadingSpinnerOverlay'

const router = useRouter()
const route = useRoute(routeNames.done)
const { toCurrencyJPY } = currencyHelper()
const { getFirstAssetUrl } = assetHelper()
const { t } = i18n.global
const { status, order } = useOrderResult(route.params.orderId)

/**
 * 注文結果の読み込みに失敗したとき、利用者に通知して陳列品画面へ遷移します。
 */
watch(status, (newStatus) => {
  if (newStatus.kind === 'failed') {
    showFailureToast(newStatus.problem, t('failedToOrderInformation'))
    router.push({ name: routeNames.displayItem })
  }
})

const goDisplayItem = () => {
  router.push({ name: routeNames.displayItem })
}
</script>
<template>
  <LoadingSpinnerOverlay :show="status.kind === 'loading'"></LoadingSpinnerOverlay>
  <div v-if="order">
    <div class="container mx-auto my-4 max-w-4xl">
      <span class="text-lg font-medium text-green-500">
        {{ t('orderingCompleted') }}
      </span>
    </div>
    <div class="container mx-auto my-4 max-w-4xl">
      <div class="mx-2 grid grid-cols-1 items-center lg:grid-cols-3 lg:gap-x-12">
        <table
          class="mt-2 table-fixed border-t border-b lg:col-span-1 lg:row-start-1 lg:mt-0 lg:border"
        >
          <tbody>
            <tr>
              <td>税抜き合計</td>
              <td class="text-right">
                {{ toCurrencyJPY(order.account?.totalItemsPrice) }}
              </td>
            </tr>
            <tr>
              <td>送料</td>
              <td class="text-right">
                {{ toCurrencyJPY(order.account?.deliveryCharge) }}
              </td>
            </tr>
            <tr>
              <td>消費税</td>
              <td class="text-right">
                {{ toCurrencyJPY(order.account?.consumptionTax) }}
              </td>
            </tr>
            <tr>
              <td>合計</td>
              <td class="text-right text-xl font-bold text-red-500">
                {{ toCurrencyJPY(order.account?.totalPrice) }}
              </td>
            </tr>
          </tbody>
        </table>
        <table class="mt-2 table-fixed border-t border-b lg:col-span-2 lg:mt-4 lg:border">
          <tbody>
            <tr>
              <td rowspan="5" class="w-24 border-r pl-2">お届け先</td>
              <td class="pl-2">{{ order.address.fullName }}</td>
            </tr>
            <tr>
              <td class="pl-2">{{ `〒${order.address.postalCode}` }}</td>
            </tr>
            <tr>
              <td class="pl-2">{{ order.address.todofuken }}</td>
            </tr>
            <tr>
              <td class="pl-2">{{ order.address.shikuchoson }}</td>
            </tr>
            <tr>
              <td class="pl-2">{{ order.address.azanaAndOthers }}</td>
            </tr>
          </tbody>
        </table>
      </div>
      <div class="mx-2 mt-8">
        <div
          v-for="item in order.items"
          :key="item.id"
          class="mt-4 grid grid-cols-5 items-center lg:grid-cols-8"
        >
          <div class="col-span-4 lg:col-span-5">
            <div class="grid grid-cols-2">
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
            </div>
          </div>
        </div>
      </div>
      <div class="flex justify-between">
        <button
          class="mt-4 ml-4 w-36 rounded-sm bg-teal-500 px-4 py-2 font-bold text-white hover:bg-teal-700"
          type="submit"
          @click="goDisplayItem()"
        >
          買い物を続ける
        </button>
      </div>
    </div>
  </div>
</template>
