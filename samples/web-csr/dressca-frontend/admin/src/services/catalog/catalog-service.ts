import { catalogItemsApi } from '@/api-client';
import type {
  CatalogItemResponse,
  PostCatalogItemRequest,
  PutCatalogItemRequest,
} from '@/generated/api-client';
import { useCatalogStore } from '@/stores/catalog/catalog';

export async function fetchCategoriesAndBrands() {
  const catalogStore = useCatalogStore();
  await catalogStore.fetchCategories();
  await catalogStore.fetchBrands();
}

export async function fetchItems(categoryId: number, brandsId: number) {
  const catalogStore = useCatalogStore();
  await catalogStore.fetchItems(categoryId, brandsId);
}

export async function fetchItem(itemId: number): Promise<CatalogItemResponse> {
  const itemResponse = await catalogItemsApi.getById(itemId);
  return itemResponse.data;
}

export async function postCatalogItem(
  name: string,
  description: string,
  price: number,
  productCode: string,
  catalogCategoryId: number,
  catalogBrandId: number,
) {
  const postCatalogItemInput: PostCatalogItemRequest = {
    name,
    description,
    price,
    productCode,
    catalogCategoryId,
    catalogBrandId,
  };
  await catalogItemsApi.postCatalogItem(postCatalogItemInput);
}

export async function updateCatalogItem(
  id: number,
  name: string,
  description: string,
  price: number,
  productCode: string,
  catalogCategoryId: number,
  catalogBrandId: number,
  rowVersion: string,
) {
  const putCatalogItemRequest: PutCatalogItemRequest = {
    name,
    description,
    price,
    productCode,
    catalogCategoryId,
    catalogBrandId,
    rowVersion,
  };
  await catalogItemsApi.putCatalogItem(id, putCatalogItemRequest);
}

export async function deleteCatalogItem(id: number) {
  await catalogItemsApi.deleteCatalogItem(id);
}
