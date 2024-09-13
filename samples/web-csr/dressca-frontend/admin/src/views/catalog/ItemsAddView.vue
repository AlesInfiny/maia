<script setup lang="ts">
import { reactive, onMounted } from 'vue';
import { storeToRefs } from 'pinia';
import { useCatalogStore } from '@/stores/catalog/catalog';
import {
  postCatalogItem,
  fetchCategoriesAndBrands,
} from '@/services/catalog/catalog-service';
import { showToast } from '@/services/notification/notificationService';
import { errorHandler } from '@/shared/error-handler/error-handler';
import NotificationModal from '@/components/NotificationModal.vue';
import { useRouter } from 'vue-router';
import { useForm } from 'vee-validate';
import { catalogItemSchema } from '@/validation/validation-items';

const { errors, values, meta, defineField } = useForm({
  validationSchema: catalogItemSchema,
  initialValues: {
    name: 'テスト用アイテム',
    description: 'テスト用アイテムです。',
    price: 1980,
    productCode: 'T001',
  },
});

const [name] = defineField('name');
const [description] = defineField('description');
const [price] = defineField('price');
const [productCode] = defineField('productCode');

const isInvalid = () => {
  return !meta.value.valid;
};

const state = reactive({
  categoryId: 1,
  brandId: 1,
});

const catalogStore = useCatalogStore();
const { getCategories, getBrands } = storeToRefs(catalogStore);
const router = useRouter();

const modalState = reactive({
  showAddNotice: false,
});

const AddItem = async () => {
  try {
    await postCatalogItem(
      values.name,
      values.description,
      values.price,
      values.productCode,
      state.categoryId,
      state.brandId,
    );
    modalState.showAddNotice = true;
  } catch (error) {
    errorHandler(error, () => {
      showToast('カタログアイテムの追加に失敗しました。');
    });
  }
};

const closeAddNotice = () => {
  modalState.showAddNotice = false;
  router.push({ name: 'catalog/items' });
};

onMounted(async () => {
  try {
    await fetchCategoriesAndBrands();
  } catch (error) {
    errorHandler(error, () => {
      showToast('カテゴリとブランド情報の取得に失敗しました。');
    });
  }
});
</script>

<template>
  <NotificationModal
    :show="modalState.showAddNotice"
    header="追加成功"
    body="カタログアイテムを追加しました。"
    @close="closeAddNotice"
  ></NotificationModal>

  <div
    class="container mx-auto flex flex-col items-center justify-center gap-6"
  >
    <div class="p-8 text-5xl font-bold">カタログアイテム追加</div>
    <form class="text-xl">
      <div class="mb-4">
        <label for="item-name" class="mb-2 block font-bold">アイテム名</label>
        <input
          id="item-name"
          v-model="name"
          type="text"
          name="item-name"
          class="w-full border border-gray-300 px-4 py-2"
        />
        <p class="px-2 py-2 text-base text-red-800">{{ errors.name }}</p>
      </div>
      <div class="mb-4">
        <label for="description" class="mb-2 block font-bold">説明</label>
        <textarea
          id="description"
          v-model="description"
          name="description"
          class="w-full border border-gray-300 px-4 py-2"
        ></textarea>
        <p class="px-2 py-2 text-base text-red-800">{{ errors.description }}</p>
      </div>
      <div class="mb-4">
        <label for="unit-price" class="mb-2 block font-bold">単価</label>
        <input
          id="unit-price"
          v-model.number="price"
          type="text"
          name="unit-price"
          class="w-full border border-gray-300 px-4 py-2"
        />
        <p class="px-2 py-2 text-base text-red-800">{{ errors.price }}</p>
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
      </div>
      <p class="px-2 py-2 text-base text-red-800">{{ errors.productCode }}</p>
      <div class="mb-4">
        <label for="category" class="mb-2 block font-bold">カテゴリ</label>
        <select
          id="category"
          v-model="state.categoryId"
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
      <button
        type="button"
        class="rounded bg-blue-600 px-4 py-2 font-bold text-white hover:bg-blue-800 disabled:bg-blue-500 disabled:opacity-50"
        :disabled="isInvalid()"
        @click="AddItem"
      >
        追加
      </button>
    </form>
  </div>
</template>
