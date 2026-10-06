<script setup lang="ts">
import { ref, watch } from 'vue'
import { routeNames } from '@/app/router/route-names'
import { storeToRefs } from 'pinia'
import { useCatalogItemEditor } from '@/catalog-management/public-api'
import { ConfirmationModal } from '@/system-common/components/ConfirmationModal'
import { NotificationModal } from '@/system-common/components/NotificationModal'
import { assetHelper } from '@/business-common/helpers/asset-helper'
import { showToast } from '@/business-common/services/notification-service'
import { useRoute, useRouter } from 'vue-router'
import { useAuthenticationStore, Roles } from '@/security/public-api'
import { LoadingSpinnerOverlay } from '@/system-common/components/LoadingSpinnerOverlay'

const authenticationStore = useAuthenticationStore()
const { isInRole } = storeToRefs(authenticationStore)
const router = useRouter()
const route = useRoute(routeNames.catalogItemsEdit)
const { getFirstAssetUrl } = assetHelper()
// ルートのパラメーターは値で渡します。
// ゲッターで渡すと、他の画面へ遷移したとき、この画面が破棄される前に空のパラメーターで読み込み直してしまいます。
const { status, current, form, categories, brands, update, remove } = useCatalogItemEditor(
  route.params.itemId,
)

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
 * アイテムの読み込みに失敗したとき、利用者に通知します。
 * アイテムが見つからない場合は、アイテム一覧画面へ遷移します。
 */
watch(status, (newStatus) => {
  if (newStatus === 'notFound') {
    showToast('対象のアイテムが見つかりませんでした。')
    router.push({ name: routeNames.catalogItems })
  } else if (newStatus === 'failed') {
    showToast('アイテムの取得に失敗しました。')
  }
})

/**
 * 削除通知モーダルを閉じます。
 * 表示する編集対象のアイテムがなくなるので、
 * アイテム一覧画面へ遷移します。
 */
const closeDeleteNotice = () => {
  showDeleteNotice.value = false
  router.push({ name: routeNames.catalogItems })
}

/**
 * 更新通知モーダルを閉じます。
 */
const closeUpdateNotice = () => {
  showUpdateNotice.value = false
}

/**
 * カタログからアイテムを削除し、結果を利用者に通知します。
 */
const removeItemAsync = async () => {
  try {
    const outcome = await remove()
    switch (outcome.kind) {
      case 'removed':
        showDeleteNotice.value = true
        break
      case 'notFound':
        showToast('削除対象のカタログアイテムが見つかりませんでした。')
        router.push({ name: routeNames.catalogItems })
        break
      case 'conflict':
        showToast('カタログアイテムの更新と削除が競合しました。もう一度削除してください。')
        break
      case 'failed':
        showToast('カタログアイテムの削除に失敗しました。')
        break
      case 'canceled':
        break
    }
  } finally {
    showDeleteConfirm.value = false
  }
}

/**
 * カタログ上のアイテムを更新し、結果を利用者に通知します。
 */
const updateItemAsync = async () => {
  try {
    const outcome = await update()
    switch (outcome.kind) {
      case 'updated':
        showUpdateNotice.value = true
        break
      case 'notFound':
        showToast('更新対象のカタログアイテムが見つかりませんでした。')
        router.push({ name: routeNames.catalogItems })
        break
      case 'conflict':
        showToast('カタログアイテムの更新が競合しました。もう一度更新してください。')
        break
      case 'failed':
        showToast('カタログアイテムの更新に失敗しました。')
        break
      case 'canceled':
        break
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
    @confirm="removeItemAsync"
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

  <LoadingSpinnerOverlay :show="status === 'loading'"></LoadingSpinnerOverlay>

  <div v-if="current" class="container mx-auto gap-6">
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
              :value="current.id"
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
              :value="current.name"
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
              :value="current.description"
              name="description"
              class="w-full border border-gray-300 px-4 py-2"
              disabled
            ></textarea>
          </div>
          <div class="mb-6">
            <label for="unit-price" class="mb-2 block font-bold">単価</label>
            <input
              id="unit-price"
              :value="current.price"
              name="unit-price"
              class="w-full border border-gray-300 px-4 py-2"
              disabled
            />
          </div>
          <div class="mb-6">
            <label for="product-code" class="mb-2 block font-bold">商品コード</label>
            <input
              id="product-code"
              :value="current.productCode"
              name="product-code"
              class="w-full border border-gray-300 px-4 py-2"
              disabled
            />
          </div>
          <div class="mb-4">
            <label for="category" class="mb-2 block font-bold">カテゴリ</label>
            <select
              id="category"
              :value="current.categoryId"
              name="category"
              class="w-full border border-gray-300 bg-gray-100 px-4 py-2"
              disabled
            >
              <option v-for="category in categories" :key="category.id" :value="category.id">
                {{ category.name }}
              </option>
            </select>
          </div>
          <div class="mb-4">
            <label for="brand" class="mb-2 block font-bold">ブランド</label>
            <select
              id="brand"
              :value="current.brandId"
              name="brand"
              class="w-full border border-gray-300 bg-gray-100 px-4 py-2"
              disabled
            >
              <option v-for="brand in brands" :key="brand.id" :value="brand.id">
                {{ brand.name }}
              </option>
            </select>
          </div>
          <div class="mb-4">
            <label for="item-id" class="mb-2 block font-bold">画像</label>
            <img
              class="flex h-auto max-w-xs justify-center"
              :src="getFirstAssetUrl(current.assetCodes)"
              :alt="current.name"
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
              :value="current.id"
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
              v-model="form.itemName"
              type="text"
              name="item-name"
              class="w-full border border-gray-300 px-4 py-2"
            />
            <p class="px-1 py-1 text-base text-red-800">
              {{ form.errors.itemName }}
            </p>
          </div>
          <div class="mb-4">
            <label for="description" class="mb-2 block font-bold">説明</label>
            <textarea
              id="item-description"
              v-model="form.itemDescription"
              name="item-description"
              class="w-full border border-gray-300 px-4 py-2"
            ></textarea>
            <p class="px-1 py-1 text-base text-red-800">
              {{ form.errors.itemDescription }}
            </p>
          </div>
          <div class="mb-4">
            <label for="unit-price" class="mb-2 block font-bold">単価</label>
            <input
              id="unit-price"
              v-model="form.price"
              name="unit-price"
              class="w-full border border-gray-300 px-4 py-2"
            />
            <p class="px-1 py-1 text-base text-red-800">{{ form.errors.price }}</p>
          </div>
          <div class="mb-4">
            <label for="product-code" class="mb-2 block font-bold">商品コード</label>
            <input
              id="product-code"
              v-model="form.productCode"
              name="product-code"
              class="w-full border border-gray-300 px-4 py-2"
            />
            <p class="px-1 py-1 text-base text-red-800">
              {{ form.errors.productCode }}
            </p>
          </div>
          <div class="mb-4">
            <label for="category" class="mb-2 block font-bold">カテゴリ</label>
            <select
              id="category"
              v-model="form.categoryId"
              name="category"
              class="w-full border border-gray-300 px-4 py-2"
            >
              <option v-for="category in categories" :key="category.id" :value="category.id">
                {{ category.name }}
              </option>
            </select>
          </div>
          <div class="mb-4">
            <label for="brand" class="mb-2 block font-bold">ブランド</label>
            <select
              id="brand"
              v-model="form.brandId"
              name="brand"
              class="w-full border border-gray-300 px-4 py-2"
            >
              <option v-for="brand in brands" :key="brand.id" :value="brand.id">
                {{ brand.name }}
              </option>
            </select>
          </div>
          <div class="mb-4">
            <label for="item-id" class="mb-2 block font-bold">画像</label>
            <img
              class="flex h-auto max-w-xs justify-center"
              :src="getFirstAssetUrl(current.assetCodes)"
              :alt="form.itemName"
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
              :disabled="!form.isValid || !isInRole(Roles.ADMIN)"
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
