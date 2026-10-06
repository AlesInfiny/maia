<script setup lang="ts">
import { ref, watch } from 'vue'
import { routeNames } from '@/app/router/route-names'
import { storeToRefs } from 'pinia'
import { useCatalogItemCreator, NotificationModal } from '@/catalog-management/public-api'
import { showToast } from '@/business-common/services/notification-service'
import { useRouter } from 'vue-router'
import { useAuthenticationStore, Roles } from '@/security/public-api'
import { LoadingSpinnerOverlay } from '@/system-common/components/LoadingSpinnerOverlay'

const router = useRouter()
const authenticationStore = useAuthenticationStore()
const { isInRole } = storeToRefs(authenticationStore)
const { status, form, categories, brands, create } = useCatalogItemCreator({
  itemName: 'テスト用アイテム',
  itemDescription: 'テスト用アイテムです。',
  price: '1980',
  productCode: 'T001',
})

/**
 * リアクティブなモーダルの開閉状態です。
 */
const showAddNotice = ref(false)

/**
 * カテゴリとブランドの読み込みに失敗したとき、利用者に通知します。
 */
watch(status, (newStatus) => {
  if (newStatus === 'failed') {
    showToast('カテゴリとブランド情報の取得に失敗しました。')
  }
})

/**
 * アイテムをカタログに追加し、結果を利用者に通知します。
 * 追加に成功したら、成功を通知するモーダルを開きます。
 */
const addItemAsync = async () => {
  const outcome = await create()
  switch (outcome.kind) {
    case 'created':
      showAddNotice.value = true
      break
    case 'failed':
      showToast('カタログアイテムの追加に失敗しました。')
      break
    case 'canceled':
      break
  }
}

/**
 * 追加成功通知のモーダルを閉じます。
 * アイテム一覧画面に遷移します。
 */
const closeAddNotice = () => {
  showAddNotice.value = false
  router.push({ name: routeNames.catalogItems })
}
</script>

<template>
  <NotificationModal
    :show="showAddNotice"
    header="追加成功"
    body="カタログアイテムを追加しました。"
    @close="closeAddNotice"
  ></NotificationModal>
  <LoadingSpinnerOverlay :show="status === 'loading'"></LoadingSpinnerOverlay>
  <div
    v-if="status === 'ready'"
    class="container mx-auto flex flex-col items-center justify-center gap-6"
  >
    <div class="p-8 text-5xl font-bold">カタログアイテム追加</div>
    <form class="text-xl">
      <div class="mb-4">
        <label for="item-name" class="mb-2 block font-bold">アイテム名</label>
        <input
          id="item-name"
          v-model="form.itemName"
          type="text"
          name="item-name"
          class="w-full border border-gray-300 px-4 py-2"
        />
        <p class="px-2 py-2 text-base text-red-800">{{ form.errors.itemName }}</p>
      </div>
      <div class="mb-4">
        <label for="item-description" class="mb-2 block font-bold">説明</label>
        <textarea
          id="item-description"
          v-model="form.itemDescription"
          name="item-description"
          class="w-full border border-gray-300 px-4 py-2"
        ></textarea>
        <p class="px-2 py-2 text-base text-red-800">
          {{ form.errors.itemDescription }}
        </p>
      </div>
      <div class="mb-4">
        <label for="unit-price" class="mb-2 block font-bold">単価</label>
        <input
          id="unit-price"
          v-model="form.price"
          type="text"
          name="unit-price"
          class="w-full border border-gray-300 px-4 py-2"
        />
        <p class="px-2 py-2 text-base text-red-800">{{ form.errors.price }}</p>
      </div>
      <div class="mb-4">
        <label for="product-code" class="mb-2 block font-bold">商品コード</label>
        <input
          id="product-code"
          v-model="form.productCode"
          name="product-code"
          class="w-full border border-gray-300 px-4 py-2"
        />
      </div>
      <p class="px-2 py-2 text-base text-red-800">{{ form.errors.productCode }}</p>
      <div class="mb-4">
        <label for="category" class="mb-2 block font-bold">カテゴリ</label>
        <select
          id="category"
          v-model="form.categoryId"
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
      <button
        type="button"
        class="rounded-sm bg-blue-600 px-4 py-2 font-bold text-white hover:bg-blue-800 disabled:bg-blue-500/50"
        :disabled="!form.isValid || !isInRole(Roles.ADMIN)"
        @click="addItemAsync()"
      >
        追加
      </button>
    </form>
  </div>
</template>
