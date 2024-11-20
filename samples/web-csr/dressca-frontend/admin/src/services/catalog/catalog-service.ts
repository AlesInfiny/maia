import {
  catalogBrandsApi,
  catalogCategoriesApi,
  catalogItemsApi,
} from '@/api-client';
import type {
  CatalogBrandResponse,
  CatalogCategoryResponse,
  CatalogItemResponse,
  PagedListOfCatalogItemResponse,
  PostCatalogItemRequest,
  PutCatalogItemRequest,
} from '@/generated/api-client';

export async function fetchCategories(): Promise<CatalogCategoryResponse[]> {
  const response = await catalogCategoriesApi.getCatalogCategories();
  const categories = response.data;
  categories.unshift({ id: 0, name: 'すべて' });
  return categories;
}

export async function fetchBrands(): Promise<CatalogBrandResponse[]> {
  const response = await catalogBrandsApi.getCatalogBrands();
  const brands = response.data;
  brands.unshift({ id: 0, name: 'すべて' });
  return brands;
}

export async function fetchItems(
  categoryId: number,
  brandId: number,
  page?: number,
): Promise<PagedListOfCatalogItemResponse> {
  const response = await catalogItemsApi.getByQuery(
    brandId === 0 ? undefined : brandId,
    categoryId === 0 ? undefined : categoryId,
    page,
    undefined,
  );
  return response.data;
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
