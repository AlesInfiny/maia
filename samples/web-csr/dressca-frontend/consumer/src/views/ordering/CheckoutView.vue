<script setup lang="ts">
import { onMounted } from 'vue'
import { useBasketStore } from '@/stores/basket/basket'
import { useUserStore } from '@/stores/user/user'
import { postOrder } from '@/services/ordering/ordering-service'
import { fetchBasket } from '@/services/basket/basket-service'
import { showToast } from '@/services/notification/notificationService'
import { useRouter } from 'vue-router'
import { currencyHelper } from '@/shared/helpers/currencyHelper'
import { assetHelper } from '@/shared/helpers/assetHelper'
import { storeToRefs } from 'pinia'
import { i18n } from '@/locales/i18n'
import { errorMessageFormat } from '@/shared/error-handler/error-message-format'
import { HttpError } from '@/shared/error-handler/custom-error'
import { useCustomErrorHandler } from '@/shared/error-handler/custom-error-handler'

const userStore = useUserStore()
const basketStore = useBasketStore()

const { getBasket } = storeToRefs(basketStore)
const { getAddress } = storeToRefs(userStore)
const router = useRouter()
const handleErrorAsync = useCustomErrorHandler()
const { toCurrencyJPY } = currencyHelper()
const { getFirstAssetUrl } = assetHelper()
const { t } = i18n.global

const checkout = async () => {
  try {
    const orderId = await postOrder(
      getAddress.value.fullName,
      getAddress.value.postalCode,
      getAddress.value.todofuken,
      getAddress.value.shikuchoson,
      getAddress.value.azanaAndOthers,
    )
    router.push({ name: 'ordering/done', params: { orderId } })
  } catch (error) {
    await handleErrorAsync(
      error,
      () => {
        router.push({ name: 'error' })
      },
      (httpError: HttpError) => {
        if (!httpError.response?.exceptionId) {
          showToast(t('failedToOrderItems'))
        } else {
          const message = errorMessageFormat(
            httpError.response.exceptionId,
            httpError.response.exceptionValues,
          )
          showToast(
            message,
            httpError.response.exceptionId,
            httpError.response.title,
            httpError.response.detail,
            httpError.response.status,
            100000,
          )
        }
      },
    )
  }
}
onMounted(async () => {
  await fetchBasket()
  if (getBasket.value.basketItems?.length === 0) {
    router.push('/')
  }
})
</script>

<template>
  <div class="container mx-auto my-4 max-w-4xl">
    <span class="text-lg font-medium text-green-500">
      {{ t('orderingCheckAndComplete') }}
    </span>
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
              {{ toCurrencyJPY(getBasket.account?.totalItemsPrice) }}
            </td>
          </tr>
          <tr>
            <td>送料</td>
            <td class="text-right">
              {{ toCurrencyJPY(getBasket.account?.deliveryCharge) }}
            </td>
          </tr>
          <tr>
            <td>消費税</td>
            <td class="text-right">
              {{ toCurrencyJPY(getBasket.account?.consumptionTax) }}
            </td>
          </tr>
          <tr>
            <td>合計</td>
            <td class="text-right text-xl font-bold text-red-500">
              {{ toCurrencyJPY(getBasket.account?.totalPrice) }}
            </td>
          </tr>
        </tbody>
      </table>
      <button
        class="mx-auto w-36 rounded-sm bg-orange-500 px-4 py-2 font-bold text-white hover:bg-amber-700 lg:col-end-3"
        type="submit"
        @click="checkout()"
      >
        注文を確定する
      </button>
      <table class="mt-2 table-fixed border-t border-b lg:col-span-3 lg:mt-4 lg:border">
        <tbody>
          <tr>
            <td rowspan="5" class="w-24 border-r pl-2">お届け先</td>
            <td class="pl-2">{{ getAddress.fullName }}</td>
          </tr>
          <tr>
            <td class="pl-2">{{ `〒${getAddress.postalCode}` }}</td>
          </tr>
          <tr>
            <td class="pl-2">{{ getAddress.todofuken }}</td>
          </tr>
          <tr>
            <td class="pl-2">{{ getAddress.shikuchoson }}</td>
          </tr>
          <tr>
            <td class="pl-2">{{ getAddress.azanaAndOthers }}</td>
          </tr>
        </tbody>
      </table>
    </div>
    <div class="mx-2 mt-8">
      <div
        v-for="item in getBasket.basketItems"
        :key="item.catalogItemId"
        class="mt-4 grid grid-cols-5 items-center lg:grid-cols-8"
      >
        <div class="col-span-4 lg:col-span-5">
          <div class="grid grid-cols-2">
            <img
              :src="getFirstAssetUrl(item.catalogItem?.assetCodes)"
              :alt="item.catalogItem?.name"
              class="pointer-events-none h-40"
            />
            <div class="ml-2">
              <p>{{ item.catalogItem?.name }}</p>
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
  </div>
</template>
