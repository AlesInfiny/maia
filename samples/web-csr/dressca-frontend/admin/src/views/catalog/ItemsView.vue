<script setup lang="ts">
import { onMounted, ref } from 'vue'
import { useRouter } from 'vue-router'
import { fetchCategoriesAndBrands, fetchItems } from '@/services/catalog/catalog-service'
import { currencyHelper } from '@/shared/helpers/currencyHelper'
import { assetHelper } from '@/shared/helpers/assetHelper'
import { showToast } from '@/services/notification/notificationService'
import { LoadingSpinnerOverlay } from '@/components/common/LoadingSpinnerOverlay'
import type {
  GetCatalogBrandsResponse,
  GetCatalogCategoriesResponse,
  PagedListOfGetCatalogItemResponse,
} from '@/generated/api-client'
import { useCustomErrorHandler } from '@/shared/error-handler/custom-error-handler'

const router = useRouter()
const customErrorHandler = useCustomErrorHandler()

const { toCurrencyJPY } = currencyHelper()
const { getFirstAssetUrl } = assetHelper()

/**
 * リアクティブなページネーションされたカタログアイテムの状態です。
 */
const pagedListOfCatalogItem = ref<PagedListOfGetCatalogItemResponse>({
  page: 0,
  totalPages: 0,
  pageSize: 0,
  totalCount: 0,
  hasPrevious: false,
  hasNext: false,
  items: [
    {
      description: '',
      price: 0,
      catalogCategoryId: 0,
      catalogBrandId: 0,
      id: 0,
      name: '',
      productCode: '',
      rowVersion: '',
      isDeleted: false,
    },
  ],
})

/**
 * リアクティブなカタログブランドの状態です。
 */
const catalogBrands = ref<GetCatalogBrandsResponse[]>([{ id: 0, name: '' }])

/**
 * リアクティブなカタログカテゴリの状態です。
 */
const catalogCategories = ref<GetCatalogCategoriesResponse[]>([{ id: 0, name: '' }])

/**
 * ローディングスピナーの表示の状態です。
 */
const showLoading = ref(true)

/**
 * カタログブランドの名前を取得します。
 * @param id カタログブランドID
 */
const getBrandName = (id: number) => {
  return catalogBrands.value.find((item) => item.id === id)?.name
}

/**
 * カタログカテゴリの名前を取得します。
 * @param id カタログカテゴリID
 */
const getCategoryName = (id: number) => {
  return catalogCategories.value.find((item) => item.id === id)?.name
}

/**
 * コンポーネントがマウントされた後に呼び出されるライフサイクルフックです。
 * このページが開かれるたびにカタログアイテムの最新の情報を表示するために、
 * カタログアイテムの情報、カタログブランドの情報、カタログカテゴリの情報を取得します。
 * それぞれの状態を更新します。
 */
onMounted(async () => {
  showLoading.value = true
  try {
    pagedListOfCatalogItem.value = await fetchItems(0, 0)
    ;[catalogCategories.value, catalogBrands.value] = await fetchCategoriesAndBrands()
  } catch (error) {
    customErrorHandler.handle(error, () => {
      showToast('カタログアイテムの取得に失敗しました。')
    })
  } finally {
    showLoading.value = false
  }
})

/**
 * アイテム追加画面に遷移します。
 */
const goToAddItem = () => {
  router.push({ name: 'catalog/items/add' })
}

/**
 * アイテム編集画面に遷移します。
 * @param id カタログアイテムID
 */
const goToEditItem = (id: number) => {
  router.push({ name: 'catalog/items/edit', params: { itemId: id } })
}
</script>

<template>
  <div class="container mx-auto gap-6">
    <LoadingSpinnerOverlay :show="showLoading"></LoadingSpinnerOverlay>
    <div v-if="!showLoading">
      <div class="flex justify-center p-8 text-5xl font-bold">カタログアイテム一覧</div>
      <div class="mx-2 my-8 flex justify-end">
        <button
          type="button"
          class="rounded bg-green-600 px-4 py-2 text-xl font-bold text-white hover:bg-green-800"
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
            v-for="item in pagedListOfCatalogItem.items"
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
              {{ getCategoryName(item.catalogCategoryId) }}
            </td>
            <td class="border">
              {{ getBrandName(item.catalogBrandId) }}
            </td>
            <td class="border text-center">
              <button
                type="button"
                class="rounded bg-blue-600 px-4 py-2 font-bold text-white hover:bg-blue-800"
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
