<script setup lang="ts">
import { onMounted, ref } from 'vue'
import { storeToRefs } from 'pinia'
import {
  fetchItem,
  updateCatalogItem,
  deleteCatalogItem,
  fetchCategoriesAndBrands,
} from '@/services/catalog/catalog-service'
import { assetHelper } from '@/shared/helpers/assetHelper'
import { showToast } from '@/services/notification/notificationService'
import ConfirmationModal from '@/components/ConfirmationModal.vue'
import NotificationModal from '@/components/NotificationModal.vue'
import { useRoute, useRouter } from 'vue-router'
import { useForm } from 'vee-validate'
import { catalogItemSchema } from '@/validation/validation-items'
import { ConflictError, NotFoundError } from '@/shared/error-handler/custom-error'
import type {
  GetCatalogBrandsResponse,
  GetCatalogCategoriesResponse,
  GetCatalogItemResponse,
} from '@/generated/api-client'
import { useAuthenticationStore } from '@/stores/authentication/authentication'
import { Roles } from '@/shared/constants/roles'
import { LoadingSpinnerOverlay } from '@/components/common/LoadingSpinnerOverlay'
import { useCustomErrorHandler } from '@/shared/error-handler/custom-error-handler'

const handleErrorAsync = useCustomErrorHandler()
const authenticationStore = useAuthenticationStore()
const { isInRole } = storeToRefs(authenticationStore)
const router = useRouter()
const route = useRoute()
const id = Number(route.params.itemId)
const { getFirstAssetUrl } = assetHelper()

/**
 * アイテムの情報を表すインターフェースです。
 * リアクティブな状態を型付けするために必要です。
 */
interface ItemState {
  id: number
  name: string
  description: string
  price: string
  productCode: string
  categoryId: number
  brandId: number
  assetCodes: string[] | undefined
  rowVersion: string
  isDeleted: boolean
}

const { errors, values, meta, defineField, setValues } = useForm({
  validationSchema: catalogItemSchema,
})

const [itemName] = defineField('itemName')
const [itemDescription] = defineField('itemDescription')
const [price] = defineField('price')
const [productCode] = defineField('productCode')

const isInvalid = () => {
  return !meta.value.valid
}

/**
 * 編集中のアイテムの状態です。
 */
const editingItemState = ref<ItemState>({
  id: 0,
  name: '',
  description: '',
  price: '',
  productCode: '',
  categoryId: 0,
  brandId: 0,
  assetCodes: [''],
  rowVersion: '',
  isDeleted: false,
})

/**
 * 現在のアイテムの状態です。
 */
const currentItemState = ref<ItemState>({
  id: 0,
  name: '',
  description: '',
  price: '',
  productCode: '',
  categoryId: 0,
  brandId: 0,
  assetCodes: [''],
  rowVersion: '',
  isDeleted: false,
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
 * 削除確認モーダルの開閉状態です。
 */
const showDeleteConfirm = ref(false)

/**
 * 削除成功通知モーダルの開閉状態です。
 */
const showDeleteNotice = ref(false)

/**
 * 更新確認モーダルの開閉状態です。
 */
const showUpdateConfirm = ref(false)

/**
 * 更新通知モーダルの開閉状態です。
 */
const showUpdateNotice = ref(false)

/**
 * ローディングスピナーの表示の状態です。
 */
const showLoading = ref(true)

/**
 * 削除通知モーダルを閉じます。
 * 表示する編集対象のアイテムがなくなるので、
 * アイテム一覧画面へ遷移します。
 */
const closeDeleteNotice = () => {
  showDeleteNotice.value = false
  router.push({ name: 'catalog/items' })
}

/**
 * 更新通知モーダルを閉じます。
 */
const closeUpdateNotice = () => {
  showUpdateNotice.value = false
}

/**
 * API モデルのアイテムの情報を、画面の現在のアイテムの状態にセットします。
 * @param catalogItemResponse カタログアイテムのレスポンス情報
 */
const setCurrentItemState = (item: GetCatalogItemResponse) => {
  currentItemState.value.id = item.id
  currentItemState.value.name = item.name
  currentItemState.value.description = item.description
  currentItemState.value.price = item.price.toString()
  currentItemState.value.productCode = item.productCode
  currentItemState.value.categoryId = item.catalogCategoryId
  currentItemState.value.brandId = item.catalogBrandId
  currentItemState.value.assetCodes = item.assetCodes
  currentItemState.value.rowVersion = item.rowVersion
  currentItemState.value.isDeleted = item.isDeleted
}

/**
 * カタログアイテムの情報を取得します。
 * @param itemId カタログアイテムID
 */
const getItem = async (itemId: number) => {
  try {
    setCurrentItemState(await fetchItem(itemId))
  } catch (error) {
    if (error instanceof NotFoundError) {
      showToast('対象のアイテムが見つかりませんでした。')
      router.push({ name: 'catalog/items' })
    }
    await handleErrorAsync(error, () => {
      showToast('アイテムの取得に失敗しました。')
    })
  }
}

/**
 * カタログカテゴリとブランドの情報を取得します。
 */
const getCategoriesAndBrands = async () => {
  try {
    ;[catalogCategories.value, catalogBrands.value] = await fetchCategoriesAndBrands()
  } catch (error) {
    await handleErrorAsync(error, () => {
      showToast('カタログアイテムとカテゴリの取得に失敗しました。')
    })
  }
}

/**
 * 対象の ID のアイテムの状態を初期化します。
 * @param itemId カタログアイテムID
 */
const initItemAsync = async (itemId: number) => {
  await getCategoriesAndBrands()
  await getItem(itemId)
  setValues({
    itemName: currentItemState.value.name,
    itemDescription: currentItemState.value.description,
    price: currentItemState.value.price,
    productCode: currentItemState.value.productCode,
  })
  editingItemState.value.id = currentItemState.value.id
  editingItemState.value.categoryId = currentItemState.value.categoryId
  editingItemState.value.brandId = currentItemState.value.brandId
  editingItemState.value.assetCodes = currentItemState.value.assetCodes
  editingItemState.value.rowVersion = currentItemState.value.rowVersion
  editingItemState.value.isDeleted = currentItemState.value.isDeleted
}

/**
 * 対象の ID のアイテムの状態を再取得します。
 * 編集中のアイテムの行バージョンのみを最新化します。
 * @param itemId
 */
const reFetchItemAndInitRowVersionAsync = async (itemId: number) => {
  await getCategoriesAndBrands()
  await getItem(itemId)
  editingItemState.value.rowVersion = currentItemState.value.rowVersion
}

/**
 * コンポーネントがマウントされた後に呼び出されるライフサイクルフックです。
 *
 */
onMounted(async () => {
  showLoading.value = true
  try {
    await initItemAsync(id)
  } finally {
    showLoading.value = false
  }
})

/**
 * カタログからアイテムを削除します。
 */
const deleteItemAsync = async () => {
  try {
    await deleteCatalogItem(editingItemState.value.id, editingItemState.value.rowVersion)
    showDeleteNotice.value = true
  } catch (error) {
    if (error instanceof NotFoundError) {
      await handleErrorAsync(error, () => {
        showToast('削除対象のカタログアイテムが見つかりませんでした。')
        router.push({ name: '/catalog/items' })
      })
    } else if (error instanceof ConflictError) {
      await handleErrorAsync(error, () => {
        showToast('カタログアイテムの更新と削除が競合しました。もう一度削除してください。')
      })
      await reFetchItemAndInitRowVersionAsync(id)
    } else {
      await handleErrorAsync(error, () => {
        showToast('カタログアイテムの削除に失敗しました。')
      })
    }
  } finally {
    showDeleteConfirm.value = false
  }
}

/**
 * カタログ上のアイテムを更新します。
 */
const updateItemAsync = async () => {
  try {
    await updateCatalogItem(
      editingItemState.value.id,
      values.itemName,
      values.itemDescription,
      values.price,
      values.productCode,
      editingItemState.value.categoryId,
      editingItemState.value.brandId,
      editingItemState.value.rowVersion,
      editingItemState.value.isDeleted,
    )
    await initItemAsync(id)
    showUpdateNotice.value = true
  } catch (error) {
    if (error instanceof NotFoundError) {
      showToast('更新対象のカタログアイテムが見つかりませんでした。')
      router.push({ name: 'catalog/items' })
    } else if (error instanceof ConflictError) {
      await handleErrorAsync(error, () => {
        showToast('カタログアイテムの更新が競合しました。もう一度更新してください。')
      })
      await reFetchItemAndInitRowVersionAsync(id)
    } else {
      await handleErrorAsync(error, () => {
        showToast('カタログアイテムの更新に失敗しました。')
      })
    }
  } finally {
    showUpdateConfirm.value = false
  }
}
</script>

<template>
  <ConfirmationModal
    :show="showDeleteConfirm"
    header="カタログアイテムを削除しますか？"
    body="カタログアイテムを削除します。削除したアイテムは復元できません。"
    @confirm="deleteItemAsync"
    @cancel="showDeleteConfirm = false"
  ></ConfirmationModal>

  <NotificationModal
    :show="showDeleteNotice"
    header="削除成功"
    body="カタログアイテムを削除しました。"
    @close="closeDeleteNotice"
  >
  </NotificationModal>

  <ConfirmationModal
    :show="showUpdateConfirm"
    header="カタログアイテムを更新しますか？"
    body="カタログアイテムを更新します。更新したアイテムは元に戻せません。"
    @confirm="updateItemAsync"
    @cancel="showUpdateConfirm = false"
  ></ConfirmationModal>

  <NotificationModal
    :show="showUpdateNotice"
    header="更新成功"
    body="カタログアイテムを更新しました。"
    @close="closeUpdateNotice"
  >
  </NotificationModal>

  <LoadingSpinnerOverlay :show="showLoading"></LoadingSpinnerOverlay>

  <div v-if="!showLoading" class="container mx-auto gap-6">
    <div>
      <div class="flex items-center justify-center p-8 text-5xl font-bold">
        カタログアイテム編集
      </div>
    </div>

    <div class="auto container flex justify-center gap-24">
      <div>
        <div class="m-8 text-4xl">変更前</div>
        <form class="text-xl">
          <div class="mb-6">
            <label for="item-id" class="mb-2 block font-bold">アイテムID</label>
            <input
              id="item-id"
              v-model.number="currentItemState.id"
              type="text"
              name="item-id"
              class="w-full border border-gray-300 px-4 py-2"
              disabled
            />
          </div>
          <div class="mb-6">
            <label for="item-name" class="mb-2 block font-bold">アイテム名</label>
            <input
              id="item-name"
              v-model="currentItemState.name"
              type="text"
              name="item-name"
              class="w-full border border-gray-300 px-4 py-2"
              disabled
            />
          </div>
          <div class="mb-6">
            <label for="description" class="mb-2 block font-bold">説明</label>
            <textarea
              id="description"
              v-model="currentItemState.description"
              name="description"
              class="w-full border border-gray-300 px-4 py-2"
              disabled
            ></textarea>
          </div>
          <div class="mb-6">
            <label for="unit-price" class="mb-2 block font-bold">単価</label>
            <input
              id="unit-price"
              v-model.number="currentItemState.price"
              name="unit-price"
              class="w-full border border-gray-300 px-4 py-2"
              disabled
            />
          </div>
          <div class="mb-6">
            <label for="product-code" class="mb-2 block font-bold">商品コード</label>
            <input
              id="product-code"
              v-model="currentItemState.productCode"
              name="product-code"
              class="w-full border border-gray-300 px-4 py-2"
              disabled
            />
          </div>
          <div class="mb-4">
            <label for="category" class="mb-2 block font-bold">カテゴリ</label>
            <select
              id="category"
              v-model.number="currentItemState.categoryId"
              name="category"
              class="w-full border border-gray-300 bg-gray-100 px-4 py-2"
              disabled
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
              v-model.number="currentItemState.brandId"
              name="brand"
              class="w-full border border-gray-300 bg-gray-100 px-4 py-2"
              disabled
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
          <div class="mb-4">
            <label for="item-id" class="mb-2 block font-bold">画像</label>
            <img
              class="flex h-auto max-w-xs justify-center"
              :src="getFirstAssetUrl(currentItemState.assetCodes)"
              :alt="currentItemState.name"
            />
          </div>
        </form>
      </div>

      <div>
        <div class="m-8 text-4xl">変更後</div>
        <form class="text-xl">
          <div class="mb-6">
            <label for="item-id" class="mb-2 block font-bold">アイテムID</label>
            <input
              id="item-id"
              v-model.number="editingItemState.id"
              type="text"
              name="item-id"
              class="w-full border border-gray-300 px-4 py-2"
              disabled
            />
          </div>
          <div class="mb-4">
            <label for="item-name" class="mb-2 block font-bold">アイテム名</label>
            <input
              id="item-name"
              v-model="itemName"
              type="text"
              name="item-name"
              class="w-full border border-gray-300 px-4 py-2"
            />
            <p class="px-1 py-1 text-base text-red-800">
              {{ errors.itemName }}
            </p>
          </div>
          <div class="mb-4">
            <label for="description" class="mb-2 block font-bold">説明</label>
            <textarea
              id="item-description"
              v-model="itemDescription"
              name="item-description"
              class="w-full border border-gray-300 px-4 py-2"
            ></textarea>
            <p class="px-1 py-1 text-base text-red-800">
              {{ errors.itemDescription }}
            </p>
          </div>
          <div class="mb-4">
            <label for="unit-price" class="mb-2 block font-bold">単価</label>
            <input
              id="unit-price"
              v-model="price"
              name="unit-price"
              class="w-full border border-gray-300 px-4 py-2"
            />
            <p class="px-1 py-1 text-base text-red-800">{{ errors.price }}</p>
          </div>
          <div class="mb-4">
            <label for="product-code" class="mb-2 block font-bold">商品コード</label>
            <input
              id="product-code"
              v-model="productCode"
              name="product-code"
              class="w-full border border-gray-300 px-4 py-2"
            />
            <p class="px-1 py-1 text-base text-red-800">
              {{ errors.productCode }}
            </p>
          </div>
          <div class="mb-4">
            <label for="category" class="mb-2 block font-bold">カテゴリ</label>
            <select
              id="category"
              v-model.number="editingItemState.categoryId"
              name="category"
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
              v-model.number="editingItemState.brandId"
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
          <div class="mb-4">
            <label for="item-id" class="mb-2 block font-bold">画像</label>
            <img
              class="flex h-auto max-w-xs justify-center"
              :src="getFirstAssetUrl(editingItemState.assetCodes)"
              :alt="values.name"
            />
          </div>
          <div class="flex justify-end">
            <!-- サンプルアプリは必ず Admin ロールを持つユーザーとしてログインするようになっているので、削除ボタンが disable になることはありません。-->
            <button
              type="button"
              class="rounded-sm bg-red-800 px-4 py-2 font-bold text-white hover:bg-red-900 disabled:bg-red-500/50"
              :disabled="!isInRole(Roles.ADMIN)"
              @click="showDeleteConfirm = true"
            >
              削除
            </button>

            <button
              type="button"
              class="rounded-sm bg-blue-600 px-4 py-2 font-bold text-white hover:bg-blue-800 disabled:bg-blue-500/50"
              :disabled="isInvalid() || !isInRole(Roles.ADMIN)"
              @click="showUpdateConfirm = true"
            >
              更新
            </button>
          </div>
        </form>
      </div>
    </div>
  </div>
</template>
