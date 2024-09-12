<script setup lang="ts">
import { reactive, toRefs, onMounted, watch } from 'vue';
import {
  fetchCategoriesAndBrands,
  fetchItems,
} from '@/services/catalog/catalog-service';
import { addItemToBasket } from '@/services/basket/basket-service';
import { showToast } from '@/services/notification/notificationService';
import { storeToRefs } from 'pinia';
import { useSpecialContentStore } from '@/stores/special-content/special-content';
import { useCatalogStore } from '@/stores/catalog/catalog';
import CarouselSlider from '@/components/common/CarouselSlider.vue';
import Loading from '@/components/common/LoadingSpinner.vue';
import { useRouter } from 'vue-router';
import { currencyHelper } from '@/shared/helpers/currencyHelper';
import { assetHelper } from '@/shared/helpers/assetHelper';
import { errorHandler } from '@/shared/error-handler/error-handler';

const specialContentStore = useSpecialContentStore();
const catalogStore = useCatalogStore();

const { getSpecialContents } = storeToRefs(specialContentStore);
const { getCategories, getBrands, getItems } = storeToRefs(catalogStore);
const router = useRouter();

const state = reactive({
  selectedCategory: 0,
  selectedBrand: 0,
  showLoading: true,
});

const { selectedCategory, selectedBrand } = toRefs(state);
const { toCurrencyJPY } = currencyHelper();
const { getFirstAssetUrl, getAssetUrl } = assetHelper();

const addBasket = async (catalogItemId: number) => {
  try {
    await addItemToBasket(catalogItemId);
    router.push({ name: 'basket' });
  } catch (error) {
    errorHandler(error, () => {
      showToast('カートに追加できませんでした。');
    });
  }
};

onMounted(async () => {
  state.showLoading = true;
  fetchCategoriesAndBrands();
  try {
    await fetchItems(selectedCategory.value, selectedBrand.value);
  } catch (error) {
    errorHandler(error, () => {
      showToast('商品の取得に失敗しました。');
    });
  }
  state.showLoading = false;
});

watch([selectedCategory, selectedBrand], async () => {
  fetchItems(selectedCategory.value, selectedBrand.value);
});
</script>

<template>
  <div class="container mx-auto">
    <Loading :show="state.showLoading"></Loading>
    <div v-if="!state.showLoading">
      <div class="flex justify-center m-4">
        <CarouselSlider :items="getSpecialContents" class="h-auto w-full">
          <template #default="{ item }">
            <img
              :src="getAssetUrl(item.assetCode)"
              alt="Special Contents"
              class="max-h-[350px] min-w-0 m-auto pointer-events-none"
            />
          </template>
        </CarouselSlider>
      </div>
      <div class="flex justify-center">
        <div class="grid lg:gap-24 grid-cols-1 lg:grid-cols-2 my-4 text-lg">
          <div>
            <label class="mr-2 font-bold"
              >カテゴリ
              <select v-model="selectedCategory" class="w-48 border-2">
                <option
                  v-for="category in getCategories"
                  :key="category.id"
                  :value="category.id"
                >
                  {{ category.name }}
                </option>
              </select>
            </label>
          </div>
          <div class="mt-2 lg:mt-0">
            <label class="mr-2 font-bold"
              >ブランド
              <select v-model="selectedBrand" class="w-48 border-2">
                <option
                  v-for="brand in getBrands"
                  :key="brand.id"
                  :value="brand.id"
                >
                  {{ brand.name }}
                </option>
              </select>
            </label>
          </div>
        </div>
      </div>
      <div class="flex justify-center">
        <div
          class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 md:gap-6 lg:gap-6 mb-4"
        >
          <div v-for="item in getItems" :key="item.id">
            <div
              class="justify-center md:border-2 lg:border-2 p-2 h-80 w-240 mx-auto"
            >
              <img
                class="h-[180px]"
                :src="getFirstAssetUrl(item.assetCodes)"
                :alt="item.name"
              />
              <div class="w-full">
                <p class="text-md mb-2 w-full">
                  {{ catalogStore.getBrandName(item.catalogBrandId) }}
                </p>
                <p class="font-bold text-lg">
                  {{ toCurrencyJPY(item.price) }}
                </p>
                <div class="mt-4 flex items-center justify-center">
                  <button
                    class="bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-4 rounded"
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
