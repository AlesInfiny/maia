<script setup lang="ts">
import { watch } from 'vue'
import { routeNames } from '@/app/router/route-names'
import { useDisplayItemList, CarouselSlider } from '@/shopping/public-api'
import { showFailureToast } from '@/business-common/services/notification-service'
import { LoadingSpinnerOverlay } from '@/system-common/components/LoadingSpinnerOverlay'
import { useRouter } from 'vue-router'
import { currencyHelper } from '@/system-common/helpers/currency-helper'
import { assetHelper } from '@/business-common/helpers/asset-helper'
import { i18n } from '@/system-common/locales/i18n'

const router = useRouter()
const { t } = i18n.global
const { status, specialContents, categories, brands, filter, items, addToBasket } =
  useDisplayItemList()

const { toCurrencyJPY } = currencyHelper()
const { getFirstAssetUrl, getAssetUrl } = assetHelper()

/**
 * 陳列品の読み込みに失敗したとき、利用者に通知します。
 */
watch(status, (newStatus) => {
  if (newStatus.kind === 'failed') {
    showFailureToast(newStatus.problem, t('failedToGetItems'))
  }
})

/**
 * 陳列品を買い物かごに入れ、買い物かご画面へ遷移します。
 * 失敗した場合は、利用者に通知します。
 * @param displayItemId 陳列品の ID 。
 */
const addBasket = async (displayItemId: string) => {
  const outcome = await addToBasket(displayItemId)
  switch (outcome.kind) {
    case 'added':
      router.push({ name: routeNames.basket })
      break
    case 'failed':
      showFailureToast(outcome.problem, t('failedToAddItemToCarts'))
      break
    case 'canceled':
      break
  }
}
</script>

<template>
  <div class="container mx-auto">
    <LoadingSpinnerOverlay :show="status.kind === 'loading'"></LoadingSpinnerOverlay>
    <div v-if="status.kind !== 'loading'">
      <div class="m-4 flex justify-center">
        <CarouselSlider :items="specialContents" class="h-auto w-full">
          <template #default="{ item }">
            <img
              :src="getAssetUrl(item.assetCode)"
              alt="Special Contents"
              class="pointer-events-none m-auto max-h-90 min-w-0"
            />
          </template>
        </CarouselSlider>
      </div>
      <div class="flex justify-center">
        <div class="my-4 grid grid-cols-1 text-lg lg:grid-cols-2 lg:gap-24">
          <div>
            <label for="category-select" class="mr-2 font-bold"> カテゴリ </label>
            <select id="category-select" v-model="filter.categoryId" class="w-48 border-2">
              <option v-for="category in categories" :key="category.id" :value="category.id">
                {{ category.name }}
              </option>
            </select>
          </div>
          <div class="mt-2 lg:mt-0">
            <label for="brand-select" class="mr-2 font-bold"> ブランド </label>
            <select id="brand-select" v-model="filter.brandId" class="w-48 border-2">
              <option v-for="brand in brands" :key="brand.id" :value="brand.id">
                {{ brand.name }}
              </option>
            </select>
          </div>
        </div>
      </div>
      <div class="flex justify-center">
        <div class="mb-4 grid grid-cols-1 md:grid-cols-2 md:gap-6 lg:grid-cols-4 lg:gap-6">
          <div v-for="item in items" :key="item.id">
            <div class="mx-auto w-60 justify-center p-2 md:border-2 lg:border-2">
              <img class="h-45" :src="getFirstAssetUrl(item.assetCodes)" :alt="item.name" />
              <div class="w-full">
                <p class="text-md mb-2 w-full">
                  {{ item.brandName }}
                </p>
                <p class="text-lg font-bold">
                  {{ toCurrencyJPY(item.price) }}
                </p>
                <div class="mt-4 flex items-center justify-center">
                  <button
                    class="rounded-sm bg-blue-500 px-4 py-2 font-bold text-white hover:bg-blue-700"
                    type="submit"
                    @click="addBasket(item.id)"
                  >
                    買い物かごに入れる
                  </button>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>
