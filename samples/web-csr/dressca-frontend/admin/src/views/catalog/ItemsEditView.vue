<script setup lang="ts">
import { onMounted, reactive } from 'vue';
import { storeToRefs } from 'pinia';
import {
  fetchItem,
  updateCatalogItem,
  deleteCatalogItem,
  fetchCategoriesAndBrands,
} from '@/services/catalog/catalog-service';
import { assetHelper } from '@/shared/helpers/assetHelper';
import { useCatalogStore } from '@/stores/catalog/catalog';
import { showToast } from '@/services/notification/notificationService';
import { errorHandler } from '@/shared/error-handler/error-handler';
import ConfirmationModal from '@/components/ConfirmationModal.vue';
import NotificationModal from '@/components/NotificationModal.vue';
import { useRoute, useRouter } from 'vue-router';
import { useForm } from 'vee-validate';
import { catalogItemSchema } from '@/validation/validation-items';
import {
  ConflictError,
  NotFoundError,
} from '@/shared/error-handler/custom-error';
import type { CatalogItemResponse } from '@/generated/api-client';

const catalogStore = useCatalogStore();
const router = useRouter();
const route = useRoute();
const id = Number(route.params.itemId);
const { getCategories, getBrands } = storeToRefs(catalogStore);
const { getFirstAssetUrl } = assetHelper();

interface ItemState {
  id: number;
  name: string;
  description: string;
  price: number;
  productCode: string;
  categoryId: number;
  brandId: number;
  assetCodes: string[] | undefined;
  rowVersion: string;
}

const currentItemState: ItemState = reactive({
  id: 0,
  name: '',
  description: '',
  price: 0,
  productCode: '',
  categoryId: 0,
  brandId: 0,
  assetCodes: [''],
  rowVersion: '',
});

const modalState = reactive({
  showDeleteConfirm: false,
  showDeleteNotice: false,
  showUpdateConfirm: false,
  showUpdateNotice: false,
});

const { errors, values, meta, defineField, setValues } = useForm({
  validationSchema: catalogItemSchema,
});

const [name] = defineField('name');
const [description] = defineField('description');
const [price] = defineField('price');
const [productCode] = defineField('productCode');

const isInvalid = () => {
  return !meta.value.valid;
};

const state: ItemState = reactive({
  id: 0,
  name,
  description,
  price,
  productCode,
  categoryId: 0,
  brandId: 0,
  assetCodes: [''],
  rowVersion: '',
});

const closeDeleteNotice = () => {
  modalState.showDeleteNotice = false;
  router.push({ name: 'catalog/items' });
};

const closeUpdateNotice = () => {
  modalState.showUpdateNotice = false;
};

const setCurrentItemState = (item: CatalogItemResponse) => {
  currentItemState.id = item.id;
  currentItemState.name = item.name;
  currentItemState.description = item.description;
  currentItemState.price = item.price;
  currentItemState.productCode = item.productCode;
  currentItemState.categoryId = item.catalogCategoryId;
  currentItemState.brandId = item.catalogBrandId;
  currentItemState.assetCodes = item.assetCodes;
};

const initItemAsync = async (itemId: number) => {
  await fetchCategoriesAndBrands();
  const item = await fetchItem(itemId);
  setCurrentItemState(item);
  setValues({
    name: item.name,
    description: item.description,
    price: item.price,
    productCode: item.productCode,
  });
  state.id = item.id;
  state.categoryId = item.catalogCategoryId;
  state.brandId = item.catalogBrandId;
  state.assetCodes = item.assetCodes;
  state.rowVersion = item.rowVersion;
};

const reFetchItemAsync = async (itemId: number) => {
  await fetchCategoriesAndBrands();
  const item = await fetchItem(itemId);
  setCurrentItemState(item);
  state.rowVersion = item.rowVersion;
};

onMounted(async () => {
  await initItemAsync(id);
});

const deleteItemAsync = async () => {
  try {
    await deleteCatalogItem(state.id);
    modalState.showDeleteNotice = true;
  } catch (error) {
    if (error instanceof NotFoundError) {
      errorHandler(error, () => {
        showToast('更新対象のカタログアイテムが見つかりませんでした。');
        router.push({ name: '/catalog/items' });
      });
    } else {
      errorHandler(error, () => {
        showToast('カタログアイテムの削除に失敗しました。');
      });
    }
  } finally {
    modalState.showDeleteConfirm = false;
  }
};

const updateItemAsync = async () => {
  try {
    await updateCatalogItem(
      state.id,
      values.name,
      values.description,
      values.price,
      values.productCode,
      state.categoryId,
      state.brandId,
      state.rowVersion,
    );
    initItemAsync(id);
    modalState.showUpdateNotice = true;
  } catch (error) {
    if (error instanceof NotFoundError) {
      showToast('更新対象のカタログアイテムが見つかりませんでした。');
      router.push({ name: 'catalog/items' });
    } else if (error instanceof ConflictError) {
      errorHandler(error, async () => {
        showToast(
          'カタログアイテムの更新が競合しました。もう一度更新してください。',
        );
        await reFetchItemAsync(id);
      });
    } else {
      errorHandler(error, async () => {
        showToast('カタログアイテムの更新に失敗しました。');
      });
    }
  } finally {
    modalState.showUpdateConfirm = false;
  }
};
</script>

<template>
  <ConfirmationModal
    :show="modalState.showDeleteConfirm"
    header="カタログアイテムを削除しますか？"
    body="カタログアイテムを削除します。削除したアイテムは復元できません。"
    @confirm="deleteItemAsync"
    @cancel="modalState.showDeleteConfirm = false"
  ></ConfirmationModal>

  <NotificationModal
    :show="modalState.showDeleteNotice"
    header="削除成功"
    body="カタログアイテムを削除しました。"
    @close="closeDeleteNotice"
  ></NotificationModal>

  <ConfirmationModal
    :show="modalState.showUpdateConfirm"
    header="カタログアイテムを更新しますか？"
    body="カタログアイテムを更新します。更新したアイテムは元に戻せません。"
    @confirm="updateItemAsync"
    @cancel="modalState.showUpdateConfirm = false"
  ></ConfirmationModal>

  <NotificationModal
    :show="modalState.showUpdateNotice"
    header="更新成功"
    body="カタログアイテムを更新しました。"
    @close="closeUpdateNotice"
  ></NotificationModal>

  <div class="container mx-auto gap-6">
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
            <label for="item-name" class="mb-2 block font-bold"
              >アイテム名</label
            >
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
            <label for="product-code" class="mb-2 block font-bold"
              >商品コード</label
            >
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
                v-for="category in getCategories.filter(
                  (category) => category.id !== 0,
                )"
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
                v-for="brand in getBrands.filter((brand) => brand.id !== 0)"
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
              v-model.number="state.id"
              type="text"
              name="item-id"
              class="w-full border border-gray-300 px-4 py-2"
              disabled
            />
          </div>
          <div class="mb-4">
            <label for="item-name" class="mb-2 block font-bold"
              >アイテム名</label
            >
            <input
              id="item-name"
              v-model="name"
              type="text"
              name="item-name"
              class="w-full border border-gray-300 px-4 py-2"
            />
            <p class="px-1 py-1 text-base text-red-800">{{ errors.name }}</p>
          </div>
          <div class="mb-4">
            <label for="description" class="mb-2 block font-bold">説明</label>
            <textarea
              id="description"
              v-model="description"
              name="description"
              class="w-full border border-gray-300 px-4 py-2"
            ></textarea>
            <p class="px-1 py-1 text-base text-red-800">
              {{ errors.description }}
            </p>
          </div>
          <div class="mb-4">
            <label for="unit-price" class="mb-2 block font-bold">単価</label>
            <input
              id="unit-price"
              v-model.number="price"
              name="unit-price"
              class="w-full border border-gray-300 px-4 py-2"
            />
            <p class="px-1 py-1 text-base text-red-800">{{ errors.price }}</p>
          </div>
          <div class="mb-4">
            <label for="product-code" class="mb-2 block font-bold"
              >商品コード</label
            >
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
              v-model.number="state.categoryId"
              name="category"
              class="w-full border border-gray-300 px-4 py-2"
            >
              <option
                v-for="category in getCategories.filter(
                  (category) => category.id !== 0,
                )"
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
              v-model.number="state.brandId"
              name="brand"
              class="w-full border border-gray-300 px-4 py-2"
            >
              <option
                v-for="brand in getBrands.filter((brand) => brand.id !== 0)"
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
              :src="getFirstAssetUrl(state.assetCodes)"
              :alt="values.name"
            />
          </div>
          <div class="flex justify-end">
            <button
              type="button"
              class="rounded bg-red-800 px-4 py-2 font-bold text-white hover:bg-red-900"
              @click="modalState.showDeleteConfirm = true"
            >
              削除
            </button>

            <button
              type="button"
              class="rounded bg-blue-600 px-4 py-2 font-bold text-white hover:bg-blue-800 disabled:bg-blue-500 disabled:opacity-50"
              :disabled="isInvalid()"
              @click="modalState.showUpdateConfirm = true"
            >
              更新
            </button>
          </div>
        </form>
      </div>
    </div>
  </div>
</template>
