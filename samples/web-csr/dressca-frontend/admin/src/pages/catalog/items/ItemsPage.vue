<script setup lang="ts">
import { watch } from 'vue'
import { routeNames } from '@/business-common/router/route-names'
import { useRouter } from 'vue-router'
import { useCatalogItemList } from '@/catalog-management/public-api'
import { currencyHelper } from '@/system-common/helpers/currency-helper'
import { assetHelper } from '@/business-common/helpers/asset-helper'
import { showToast } from '@/business-common/services/notification-service'
import { LoadingSpinnerOverlay } from '@/system-common/components/LoadingSpinnerOverlay'

const router = useRouter()

const { toCurrencyJPY } = currencyHelper()
const { getFirstAssetUrl } = assetHelper()
const { status, items } = useCatalogItemList()

/**
 * 一覧の読み込みに失敗したとき、利用者に通知します。
 */
watch(status, (newStatus) => {
  if (newStatus === 'failed') {
    showToast('カタログアイテムの取得に失敗しました。')
  }
})

/**
 * アイテム追加画面に遷移します。
 */
const goToAddItem = () => {
  router.push({ name: routeNames.catalogItemsAdd })
}

/**
 * アイテム編集画面に遷移します。
 * @param id カタログアイテムID
 */
const goToEditItem = (id: string) => {
  router.push({ name: routeNames.catalogItemsEdit, params: { itemId: id } })
}
</script>

<template>
  <div class="container mx-auto gap-6">
    <LoadingSpinnerOverlay :show="status === 'loading'"></LoadingSpinnerOverlay>
    <div v-if="status !== 'loading'">
      <div class="flex justify-center p-8 text-5xl font-bold">カタログアイテム一覧</div>
      <div class="mx-2 my-8 flex justify-end">
        <button
          type="button"
          class="rounded-sm bg-green-600 px-4 py-2 text-xl font-bold text-white hover:bg-green-800"
          @click="goToAddItem"
        >
          アイテム追加
        </button>
      </div>
      <table class="table-auto border-separate text-xl">
        <thead class="bg-blue-50">
          <tr>
            <th class="w-20">アイテムID</th>
            <th class="w-60">画像</th>
            <th>アイテム名</th>
            <th>説明</th>
            <th>単価</th>
            <th>商品コード</th>
            <th class="w-20">カテゴリ</th>
            <th>ブランド</th>
            <th class="w-20">操作</th>
            <th>アイテム状態</th>
          </tr>
        </thead>
        <tbody>
          <tr
            v-for="item in items"
            :key="item.id"
            :class="[item.isDeleted ? 'border bg-gray-500' : 'border']"
          >
            <td class="border">{{ item.id }}</td>
            <td class="border">
              <img
                class="object-contain"
                :src="getFirstAssetUrl(item.assetCodes)"
                :alt="item.name"
              />
            </td>
            <td class="border">{{ item.name }}</td>
            <td class="border">{{ item.description }}</td>
            <td class="border">{{ toCurrencyJPY(item.price) }}</td>
            <td class="border">{{ item.productCode }}</td>
            <td class="border">
              {{ item.categoryName }}
            </td>
            <td class="border">
              {{ item.brandName }}
            </td>
            <td class="border text-center">
              <button
                type="button"
                class="rounded-sm bg-blue-600 px-4 py-2 font-bold text-white hover:bg-blue-800"
                @click="goToEditItem(item.id)"
              >
                編集
              </button>
            </td>
            <td class="border">{{ item.isDeleted ? '削除済み' : '' }}</td>
          </tr>
        </tbody>
      </table>
    </div>
  </div>
</template>
