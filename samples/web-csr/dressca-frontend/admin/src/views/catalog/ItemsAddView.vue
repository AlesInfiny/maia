<script setup lang="ts">
import { onMounted, ref } from 'vue'
import { storeToRefs } from 'pinia'
import { fetchCategoriesAndBrands, postCatalogItem } from '@/services/catalog/catalog-service'
import { showToast } from '@/services/notification/notificationService'
import NotificationModal from '@/components/NotificationModal.vue'
import { useRouter } from 'vue-router'
import { useForm } from 'vee-validate'
import { catalogItemSchema } from '@/validation/validation-items'
import type { GetCatalogBrandsResponse, GetCatalogCategoriesResponse } from '@/generated/api-client'
import { useAuthenticationStore } from '@/stores/authentication/authentication'
import { Roles } from '@/shared/constants/roles'
import { LoadingSpinnerOverlay } from '@/components/common/LoadingSpinnerOverlay'
import { useCustomErrorHandler } from '@/shared/error-handler/custom-error-handler'

const router = useRouter()
const handleErrorAsync = useCustomErrorHandler()
const authenticationStore = useAuthenticationStore()
const { isInRole } = storeToRefs(authenticationStore)

const { errors, values, meta, defineField } = useForm({
  validationSchema: catalogItemSchema,
  initialValues: {
    itemName: 'テスト用アイテム',
    itemDescription: 'テスト用アイテムです。',
    price: '1980',
    productCode: 'T001',
  },
})

const [itemName] = defineField('itemName')
const [itemDescription] = defineField('itemDescription')
const [price] = defineField('price')
const [productCode] = defineField('productCode')

const isInvalid = () => {
  return !meta.value.valid
}

/**
 * 選択されているカタログカテゴリの ID です。
 */
const selectedCategoryId = ref(1)

/**
 * 選択されているカタログブランドの ID です。
 */
const selectedBrandId = ref(1)

/**
 * リアクティブなカタログブランドの状態です。
 */
const catalogBrands = ref<GetCatalogBrandsResponse[]>([{ id: 0, name: '' }])

/**
 * リアクティブなカタログカテゴリの状態です。
 */
const catalogCategories = ref<GetCatalogCategoriesResponse[]>([{ id: 0, name: '' }])

/**
 * リアクティブなモーダルの開閉状態です。
 */
const showAddNotice = ref(false)

/**
 * ローディングスピナーの表示の状態です。
 */
const showLoading = ref(true)

/**
 * アイテムをカタログに追加します。
 * 追加に成功したら、成功を通知するモーダルを開きます。
 */
const AddItem = async () => {
  try {
    await postCatalogItem(
      values.itemName,
      values.itemDescription,
      values.price,
      values.productCode,
      selectedCategoryId.value,
      selectedBrandId.value,
    )
    showAddNotice.value = true
  } catch (error) {
    await handleErrorAsync(error, () => {
      showToast('カタログアイテムの追加に失敗しました。')
    })
  }
}

/**
 * 追加成功通知のモーダルを閉じます。
 * アイテム一覧画面に遷移します。
 */
const closeAddNotice = () => {
  showAddNotice.value = false
  router.push({ name: 'catalog/items' })
}

/**
 * コンポーネントがマウントされた後に呼び出されるライフサイクルフックです。
 * 選択可能なカタログブランドとカテゴリの情報を最新化するために、
 * カタログブランドの情報、カタログカテゴリの情報を取得して、
 * それぞれの状態を更新します。
 */
onMounted(async () => {
  showLoading.value = true
  try {
    ;[catalogCategories.value, catalogBrands.value] = await fetchCategoriesAndBrands()
  } catch (error) {
    await handleErrorAsync(error, () => {
      showToast('カテゴリとブランド情報の取得に失敗しました。')
    })
  } finally {
    showLoading.value = false
  }
})
</script>

<template>
  <NotificationModal
    :show="showAddNotice"
    header="追加成功"
    body="カタログアイテムを追加しました。"
    @close="closeAddNotice"
  ></NotificationModal>
  <LoadingSpinnerOverlay :show="showLoading"></LoadingSpinnerOverlay>
  <div
    v-if="!showLoading"
    class="container mx-auto flex flex-col items-center justify-center gap-6"
  >
    <div class="p-8 text-5xl font-bold">カタログアイテム追加</div>
    <form class="text-xl">
      <div class="mb-4">
        <label for="item-name" class="mb-2 block font-bold">アイテム名</label>
        <input
          id="item-name"
          v-model="itemName"
          type="text"
          name="item-name"
          class="w-full border border-gray-300 px-4 py-2"
        />
        <p class="px-2 py-2 text-base text-red-800">{{ errors.itemName }}</p>
      </div>
      <div class="mb-4">
        <label for="item-description" class="mb-2 block font-bold">説明</label>
        <textarea
          id="item-description"
          v-model="itemDescription"
          name="item-description"
          class="w-full border border-gray-300 px-4 py-2"
        ></textarea>
        <p class="px-2 py-2 text-base text-red-800">
          {{ errors.itemDescription }}
        </p>
      </div>
      <div class="mb-4">
        <label for="unit-price" class="mb-2 block font-bold">単価</label>
        <input
          id="unit-price"
          v-model="price"
          type="text"
          name="unit-price"
          class="w-full border border-gray-300 px-4 py-2"
        />
        <p class="px-2 py-2 text-base text-red-800">{{ errors.price }}</p>
      </div>
      <div class="mb-4">
        <label for="product-code" class="mb-2 block font-bold">商品コード</label>
        <input
          id="product-code"
          v-model="productCode"
          name="product-code"
          class="w-full border border-gray-300 px-4 py-2"
        />
      </div>
      <p class="px-2 py-2 text-base text-red-800">{{ errors.productCode }}</p>
      <div class="mb-4">
        <label for="category" class="mb-2 block font-bold">カテゴリ</label>
        <select
          id="category"
          v-model="selectedCategoryId"
          class="w-full border border-gray-300 px-4 py-2"
        >
          <option
            v-for="category in catalogCategories.filter((category) => category.id !== 0)"
            :key="category.id"
            :value="category.id"
          >
            {{ category.name }}
          </option>
        </select>
      </div>

      <div class="mb-4">
        <label for="brand" class="mb-2 block font-bold">ブランド</label>
        <select
          id="brand"
          v-model.number="selectedBrandId"
          name="brand"
          class="w-full border border-gray-300 px-4 py-2"
        >
          <option
            v-for="brand in catalogBrands.filter((brand) => brand.id !== 0)"
            :key="brand.id"
            :value="brand.id"
          >
            {{ brand.name }}
          </option>
        </select>
      </div>
      <button
        type="button"
        class="rounded-sm bg-blue-600 px-4 py-2 font-bold text-white hover:bg-blue-800 disabled:bg-blue-500/50"
        :disabled="isInvalid() || !isInRole(Roles.ADMIN)"
        @click="AddItem()"
      >
        追加
      </button>
    </form>
  </div>
</template>
