<script setup lang="ts">
import { watch } from 'vue'
import { routeNames } from '@/app/router/route-names'
import { useBasket, BasketItem } from '@/shopping/public-api'
import { showFailureToast, showToast } from '@/business-common/services/notification-service'
import { useRouter } from 'vue-router'
import { i18n } from '@/system-common/locales/i18n'
import { LoadingSpinnerOverlay } from '@/system-common/components/LoadingSpinnerOverlay'
import { currencyHelper } from '@/system-common/helpers/currency-helper'
import { assetHelper } from '@/business-common/helpers/asset-helper'

const router = useRouter()
const { toCurrencyJPY } = currencyHelper()
const { getFirstAssetUrl } = assetHelper()
const { t } = i18n.global
const {
  status,
  lines,
  account,
  isEmpty,
  hasUnavailableItems,
  addedItem,
  changeQuantity,
  remove,
  proceedToCheckout,
} = useBasket()

/**
 * 買い物かごを読み込んだとき、購入できない陳列品が入っていれば利用者に通知します。
 * 読み込みに失敗したときも、利用者に通知します。
 */
watch(status, (newStatus) => {
  if (newStatus.kind === 'failed') {
    showFailureToast(newStatus.problem, t('failedToGetCarts'))
  } else if (newStatus.kind === 'ready' && hasUnavailableItems.value) {
    showToast(t('basketContainsUnavailableItem'))
  }
})

const goDisplayItem = () => {
  router.push({ name: routeNames.displayItem })
}

/**
 * 陳列品の数量を変更します。
 * 失敗した場合は、利用者に通知します。
 * @param displayItemId 陳列品の ID 。
 * @param newQuantity 変更後の数量。
 */
const update = async (displayItemId: string, newQuantity: number) => {
  const outcome = await changeQuantity(displayItemId, newQuantity)
  if (outcome.kind === 'failed') {
    showFailureToast(outcome.problem, t('failedToChangeQuantities'))
  }
}

/**
 * 陳列品を買い物かごから削除します。
 * 失敗した場合は、利用者に通知します。
 * @param displayItemId 陳列品の ID 。
 */
const removeItem = async (displayItemId: string) => {
  const outcome = await remove(displayItemId)
  if (outcome.kind === 'failed') {
    showFailureToast(outcome.problem, t('failedToDeleteItems'))
  }
}

/**
 * 注文に進めることを確認し、注文確認画面へ遷移します。
 * 購入できない陳列品が入っている場合や、確認に失敗した場合は、利用者に通知します。
 */
const order = async () => {
  const outcome = await proceedToCheckout()
  switch (outcome.kind) {
    case 'ready':
      router.push({ name: routeNames.checkout })
      break
    case 'containsUnavailableItems':
      showToast(t('basketContainsUnavailableItem'))
      break
    case 'failed':
      showFailureToast(outcome.problem, t('failedToGetCarts'))
      break
    case 'canceled':
      break
  }
}
</script>

<template>
  <div class="container mx-auto my-4 max-w-4xl">
    <LoadingSpinnerOverlay :show="status.kind === 'loading'"></LoadingSpinnerOverlay>
    <div v-if="status.kind !== 'loading'">
      <div v-if="addedItem" class="mx-2">
        <span class="text-lg font-medium text-green-500">
          {{ t('addedItemsToBasket') }}
        </span>
        <div class="mt-4 grid grid-cols-1 items-center lg:grid-cols-3">
          <img
            :src="getFirstAssetUrl(addedItem.assetCodes)"
            :alt="addedItem.name"
            class="pointer-events-none m-auto h-40"
          />
          <span class="text-center lg:text-left">
            {{ addedItem.name }}
          </span>
          <span class="text-center lg:text-left">
            {{ toCurrencyJPY(addedItem.unitPrice) }}
          </span>
        </div>
      </div>

      <div v-if="isEmpty" class="mx-2 mt-4">
        <span class="text-2xl font-medium">
          {{ t('noItemsInBasket') }}
        </span>
      </div>
      <div v-if="!isEmpty" class="mx-2 mt-8">
        <span class="text-2xl font-medium">現在のカートの中身</span>
        <div class="mt-4 hidden grid-cols-1 items-center lg:grid lg:grid-cols-5">
          <div class="text-center text-lg font-medium lg:col-span-3">商品</div>
          <div class="text-right text-lg font-medium lg:col-span-1">数量</div>
        </div>
        <div
          v-for="line in lines"
          :key="line.displayItemId"
          class="mt-4 grid grid-cols-5 items-center lg:grid-cols-8"
          :class="{
            'bg-red-100': !line.available,
          }"
        >
          <BasketItem
            :item="line"
            :available="line.available"
            @update="update"
            @remove="removeItem"
          ></BasketItem>
        </div>
        <hr class="mt-4" />
        <div class="mt-4 mr-2 text-right">
          <table class="inline-block border-separate">
            <tbody>
              <tr>
                <th>税抜き合計</th>
                <td>{{ toCurrencyJPY(account?.totalItemsPrice) }}</td>
              </tr>
              <tr>
                <th>送料</th>
                <td>{{ toCurrencyJPY(account?.deliveryCharge) }}</td>
              </tr>
              <tr>
                <th>消費税</th>
                <td>{{ toCurrencyJPY(account?.consumptionTax) }}</td>
              </tr>
              <tr>
                <th>合計</th>
                <td class="">
                  {{ toCurrencyJPY(account?.totalPrice) }}
                </td>
              </tr>
            </tbody>
          </table>
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
        <span v-if="!isEmpty">
          <button
            data-testId="orderButton"
            class="mt-4 mr-4 w-36 rounded-sm bg-orange-500 px-4 py-2 font-bold text-white hover:bg-amber-700 disabled:bg-orange-300/50"
            type="submit"
            :disabled="hasUnavailableItems"
            @click="order()"
          >
            レジに進む
          </button>
        </span>
      </div>
    </div>
  </div>
</template>
