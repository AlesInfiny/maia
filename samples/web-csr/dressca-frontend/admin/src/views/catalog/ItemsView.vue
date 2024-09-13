<script setup lang="ts">
import { onMounted } from 'vue';
import { useRouter } from 'vue-router';
import {
  fetchCategoriesAndBrands,
  fetchItems,
} from '@/services/catalog/catalog-service';
import { storeToRefs } from 'pinia';
import { useCatalogStore } from '@/stores/catalog/catalog';
import { currencyHelper } from '@/shared/helpers/currencyHelper';
import { assetHelper } from '@/shared/helpers/assetHelper';
import { errorHandler } from '@/shared/error-handler/error-handler';
import { showToast } from '@/services/notification/notificationService';

const router = useRouter();
const catalogStore = useCatalogStore();
const { getItems } = storeToRefs(catalogStore);

const { toCurrencyJPY } = currencyHelper();
const { getFirstAssetUrl } = assetHelper();

onMounted(async () => {
  try {
    await fetchCategoriesAndBrands();
    await fetchItems(0, 0);
  } catch (error) {
    errorHandler(error, () => {
      showToast('カタログアイテムの取得に失敗しました。');
    });
  }
});

const goAdd = () => {
  router.push({ name: 'catalog/items/add' });
};

const goEdit = (id: number) => {
  router.push({ name: 'catalog/items/edit', params: { itemId: id } });
};
</script>

<template>
  <div class="container mx-auto gap-6">
    <div class="flex justify-center p-8 text-5xl font-bold">
      カタログアイテム一覧
    </div>
    <div class="mx-2 my-8 flex justify-end">
      <button
        type="button"
        class="rounded bg-green-600 px-4 py-2 text-xl font-bold text-white hover:bg-green-800"
        @click="goAdd"
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
        </tr>
      </thead>
      <tbody>
        <tr v-for="item in getItems" :key="item.id">
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
            {{ catalogStore.getCategoryName(item.catalogCategoryId) }}
          </td>
          <td class="border">
            {{ catalogStore.getBrandName(item.catalogBrandId) }}
          </td>
          <td class="border text-center">
            <button
              type="button"
              class="rounded bg-blue-600 px-4 py-2 font-bold text-white hover:bg-blue-800"
              @click="goEdit(item.id)"
            >
              編集
            </button>
          </td>
        </tr>
      </tbody>
    </table>
  </div>
</template>
