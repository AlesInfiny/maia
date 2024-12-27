<script setup lang="ts">
import { computed } from 'vue';
import type { BasketItemResponse } from '@/generated/api-client/models/basket-item-response';
import { TrashIcon } from '@heroicons/vue/24/outline';
import * as yup from 'yup';
import { useField, useForm } from 'vee-validate';
import { currencyHelper } from '@/shared/helpers/currencyHelper';
import { assetHelper } from '@/shared/helpers/assetHelper';

const props = defineProps<{
  item: BasketItemResponse;
}>();

const emit = defineEmits<{
  (e: 'update', catalogItemId: number, quantity: number): void;
  (e: 'remove', catalogItemId: number): void;
}>();

// フォーム固有のバリデーション定義
const formSchema = yup.object({
  quantity: yup.number().integer().min(1).max(999),
});

const { meta, resetForm } = useForm({
  validationSchema: formSchema,
  initialValues: { quantity: props.item.quantity },
});
const { value: quantity } = useField<number>('quantity');
const { toCurrencyJPY } = currencyHelper();
const { getFirstAssetUrl } = assetHelper();

const isUpdateDisabled = computed(
  () => !(meta.value.valid && meta.value.dirty),
);

const update = () => {
  resetForm({ values: { quantity: quantity.value } });
  emit('update', props.item.catalogItemId, quantity.value);
};

const remove = () => {
  resetForm({ values: { quantity: props.item.quantity } });
  emit('remove', props.item.catalogItemId);
};
</script>

<template>
  <div class="col-span-4 lg:col-span-5">
    <div class="grid grid-cols-2">
      <img
        :src="getFirstAssetUrl(item.catalogItem?.assetCodes)"
        :alt="item.catalogItem?.name"
        class="h-[150px] pointer-events-none"
      />
      <div class="ml-2">
        <p>{{ item.catalogItem?.name }}</p>
        <p class="mt-4">{{ toCurrencyJPY(item.unitPrice) }}</p>
      </div>
    </div>
  </div>
  <div class="lg:col-span-3">
    <div class="grid grid-cols-1 lg:grid-cols-3">
      <div
        class="lg:col-span-2 grid place-items-end lg:flex lg:flex-row lg:items-center"
      >
        <div class="basis-3/5 mt-2 ml-2 mr-2 lg:pr-10 text-right">
          <label>
            <input
              v-model.number="quantity"
              type="number"
              min="1"
              max="999"
              class="w-full px-4 py-2 border-b focus:outline-none focus:border-b-2 focus:border-indigo-500 placeholder-gray-500 placeholder-opacity-50"
            />
          </label>
        </div>
        <div class="basis-2/5">
          <button
            type="button"
            class="w-12 mt-2 mr-2 py-2 rounded bg-transparent disabled:bg-transparent hover:bg-blue-500 font-semibold text-blue-700 disabled:text-blue-700 hover:text-white border border-blue-500 disabled:border-blue-500 disabled:cursor-not-allowed"
            :disabled="isUpdateDisabled"
            @click="update()"
          >
            更新
          </button>
        </div>
      </div>
      <div class="mt-2 mb-1 ml-4 mr-2 grid place-items-end">
        <TrashIcon
          class="h-8 w-8 text-gray-500 hover:text-gray-700"
          @click="remove()"
        />
      </div>
    </div>
    <div class="text-right mt-4 mr-3">
      小計：
      <span>{{ toCurrencyJPY(item.subTotal) }}</span>
    </div>
  </div>
</template>
