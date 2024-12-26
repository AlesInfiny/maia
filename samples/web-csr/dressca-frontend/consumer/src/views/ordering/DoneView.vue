<script setup lang="ts">
import { onMounted, reactive, toRefs } from 'vue';
import { useRouter } from 'vue-router';
import { i18n } from '@/locales/i18n';
import { getOrder } from '@/services/ordering/ordering-service';
import { showToast } from '@/services/notification/notificationService';
import type { OrderResponse } from '@/generated/api-client/models/order-response';
import { currencyHelper } from '@/shared/helpers/currencyHelper';
import { assetHelper } from '@/shared/helpers/assetHelper';
import { useCustomErrorHandler } from '@/shared/error-handler/use-custom-error-handler';
import { errorMessageFormat } from '@/shared/error-handler/error-message-format';
import { HttpError } from '@/shared/error-handler/custom-error';

const router = useRouter();
const customErrorHandler = useCustomErrorHandler();
const props = defineProps<{
  orderId: number;
}>();
const state = reactive({
  lastOrdered: null as OrderResponse | null,
});

const { lastOrdered } = toRefs(state);
const { toCurrencyJPY } = currencyHelper();
const { getFirstAssetUrl } = assetHelper();
const { t } = i18n.global;

const goCatalog = () => {
  router.push({ name: 'catalog' });
};

onMounted(async () => {
  try {
    state.lastOrdered = await getOrder(props.orderId);
  } catch (error) {
    customErrorHandler.handle(
      error,
      () => {
        router.push('/');
      },
      (httpError: HttpError) => {
        if (!httpError.response?.exceptionId) {
          showToast(t('failedToOrderInformation'));
        } else {
          const message = errorMessageFormat(
            httpError.response.exceptionId,
            httpError.response.exceptionValues,
          );
          showToast(
            message,
            httpError.response.exceptionId,
            httpError.response.title,
            httpError.response.detail,
            httpError.response.status,
            100000,
          );
        }
      },
    );
  }
});
</script>

<template>
  <div class="container mx-auto my-4 max-w-4xl">
    <span class="text-lg font-medium text-green-500">
      {{ t('orderingCompleted') }}
    </span>
  </div>
  <div class="container mx-auto my-4 max-w-4xl">
    <div
      class="mx-2 grid grid-cols-1 lg:grid-cols-3 lg:gap-x-12 flex items-center"
    >
      <table
        class="lg:row-start-1 lg:col-span-1 table-fixed mt-2 lg:mt-0 border-t border-b lg:border"
      >
        <tbody>
          <tr>
            <td>税抜き合計</td>
            <td class="text-right">
              {{ toCurrencyJPY(lastOrdered?.account?.totalItemsPrice) }}
            </td>
          </tr>
          <tr>
            <td>送料</td>
            <td class="text-right">
              {{ toCurrencyJPY(lastOrdered?.account?.deliveryCharge) }}
            </td>
          </tr>
          <tr>
            <td>消費税</td>
            <td class="text-right">
              {{ toCurrencyJPY(lastOrdered?.account?.consumptionTax) }}
            </td>
          </tr>
          <tr>
            <td>合計</td>
            <td class="text-right text-xl font-bold text-red-500">
              {{ toCurrencyJPY(lastOrdered?.account?.totalPrice) }}
            </td>
          </tr>
        </tbody>
      </table>
      <table
        class="lg:col-span-2 table-fixed mt-2 lg:mt-4 border-t border-b lg:border"
      >
        <tbody>
          <tr>
            <td rowspan="5" class="w-24 pl-2 border-r">お届け先</td>
            <td class="pl-2">{{ lastOrdered?.fullName }}</td>
          </tr>
          <tr>
            <td class="pl-2">{{ `〒${lastOrdered?.postalCode}` }}</td>
          </tr>
          <tr>
            <td class="pl-2">{{ lastOrdered?.todofuken }}</td>
          </tr>
          <tr>
            <td class="pl-2">{{ lastOrdered?.shikuchoson }}</td>
          </tr>
          <tr>
            <td class="pl-2">{{ lastOrdered?.azanaAndOthers }}</td>
          </tr>
        </tbody>
      </table>
    </div>
    <div class="mt-8 mx-2">
      <div
        v-for="item in lastOrdered?.orderItems"
        :key="item.itemOrdered?.id"
        class="grid grid-cols-5 lg:grid-cols-8 mt-4 flex items-center"
      >
        <div class="col-span-4 lg:col-span-5">
          <div class="grid grid-cols-2">
            <img
              :src="getFirstAssetUrl(item.itemOrdered?.assetCodes)"
              :alt="item.itemOrdered?.name"
              class="h-[150px] pointer-events-none"
            />
            <div class="ml-2">
              <p>{{ item.itemOrdered?.name }}</p>
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
        class="w-36 mt-4 ml-4 bg-teal-500 hover:bg-teal-700 text-white font-bold py-2 px-4 rounded"
        type="submit"
        @click="goCatalog()"
      >
        買い物を続ける
      </button>
    </div>
  </div>
</template>
