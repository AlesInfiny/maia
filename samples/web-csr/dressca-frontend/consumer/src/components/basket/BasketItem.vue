<script setup lang="ts">
import { computed } from 'vue'
import type { BasketItemResponse } from '@/generated/api-client/models/basket-item-response'
import { TrashIcon } from '@heroicons/vue/24/outline'
import * as yup from 'yup'
import { useField, useForm } from 'vee-validate'
import { currencyHelper } from '@/shared/helpers/currencyHelper'
import { assetHelper } from '@/shared/helpers/assetHelper'
import { i18n } from '@/locales/i18n'

const { t } = i18n.global
const props = defineProps<{
  item: BasketItemResponse
  available: boolean
}>()

const emit = defineEmits<{
  (e: 'update', catalogItemId: number, quantity: number): void
  (e: 'remove', catalogItemId: number): void
}>()

// フォーム固有のバリデーション定義
const formSchema = yup.object({
  quantity: yup.number().integer().min(1).max(999),
})

const { meta, resetForm } = useForm({
  validationSchema: formSchema,
  initialValues: { quantity: props.item.quantity },
})
const { value: quantity } = useField<number>('quantity')
const { toCurrencyJPY } = currencyHelper()
const { getFirstAssetUrl } = assetHelper()

const isUpdateDisabled = computed(() => !(meta.value.valid && meta.value.dirty))

const update = () => {
  resetForm({ values: { quantity: quantity.value } })
  emit('update', props.item.catalogItemId, quantity.value)
}

const remove = () => {
  resetForm({ values: { quantity: props.item.quantity } })
  emit('remove', props.item.catalogItemId)
}
</script>

<template>
  <div class="col-span-4 lg:col-span-5">
    <div class="grid grid-cols-2">
      <img
        :src="getFirstAssetUrl(item.catalogItem?.assetCodes)"
        :alt="item.catalogItem?.name"
        class="pointer-events-none h-40"
      />
      <div class="ml-2">
        <p>{{ item.catalogItem?.name }}</p>
        <p class="mt-4">{{ toCurrencyJPY(item.unitPrice) }}</p>
        <p v-if="!available" class="mt-4 font-bold text-red-500">
          {{ t('itemUnavailable') }}
        </p>
      </div>
    </div>
  </div>
  <div class="lg:col-span-3">
    <div class="grid grid-cols-1 lg:grid-cols-3">
      <div class="grid place-items-end lg:col-span-2 lg:flex lg:flex-row lg:items-center">
        <div class="mt-2 mr-2 ml-2 basis-3/5 text-right lg:pr-10">
          <label>
            <input
              v-model.number="quantity"
              type="number"
              min="1"
              max="999"
              class="w-full border-b px-4 py-2 placeholder-gray-500/50 focus:border-b-2 focus:border-indigo-500 focus:outline-hidden"
              :disabled="!available"
            />
          </label>
        </div>
        <div class="basis-2/5">
          <button
            type="button"
            class="mt-2 mr-2 w-12 rounded-sm border border-blue-500 bg-transparent py-2 font-semibold text-blue-700 hover:bg-blue-500 hover:text-white disabled:cursor-not-allowed disabled:border-blue-500 disabled:bg-transparent disabled:text-blue-700"
            :disabled="isUpdateDisabled"
            @click="update()"
          >
            更新
          </button>
        </div>
      </div>
      <div class="mt-2 mr-2 mb-1 ml-4 grid place-items-end">
        <TrashIcon class="h-8 w-8 text-gray-500 hover:text-gray-700" @click="remove()" />
      </div>
    </div>
    <div class="mt-4 mr-3 text-right">
      小計：
      <span>{{ toCurrencyJPY(item.subTotal) }}</span>
    </div>
  </div>
</template>
