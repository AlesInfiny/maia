import {
  catalogBrandsApi,
  catalogCategoriesApi,
  catalogItemsApi,
} from '@/api-client';
import type {
  GetCatalogBrandsResponse,
  GetCatalogCategoriesResponse,
  GetCatalogItemResponse,
  PagedListOfGetCatalogItemResponse,
  PostCatalogItemRequest,
  PutCatalogItemRequest,
} from '@/generated/api-client';

/**
 * カテゴリの情報を取得します。
 * UIの都合で、すべてのカテゴリを表すカテゴリの情報を追加します。
 * @returns カタログカテゴリの配列。
 */
export async function fetchCategories(): Promise<
  GetCatalogCategoriesResponse[]
> {
  const response = await catalogCategoriesApi.getCatalogCategories();
  const categories = response.data;
  categories.unshift({ id: 0, name: 'すべて' });
  return categories;
}

/**
 * ブランドの情報を取得します。
 * UIの都合で、すべてのカテゴリを表すカテゴリの情報を追加します。
 * @returns カテゴリブランドの配列。
 */
export async function fetchBrands(): Promise<GetCatalogBrandsResponse[]> {
  const response = await catalogBrandsApi.getCatalogBrands();
  const brands = response.data;
  brands.unshift({ id: 0, name: 'すべて' });
  return brands;
}

/**
 * カテゴリとブランドの情報を取得します。
 * @returns カテゴリとブランドの情報のタプル。
 */
export async function fetchCategoriesAndBrands(): Promise<
  [GetCatalogCategoriesResponse[], GetCatalogBrandsResponse[]]
> {
  const categories = await fetchCategories();
  const brands = await fetchBrands();
  return [categories, brands];
}

/**
 * 検索条件に合致する、ページネーションされたカタログアイテムのリストを取得します。
 * @param categoryId カテゴリのID。
 * @param brandId ブランドのID。
 * @param page ページ数。
 * @returns ページネーションされたカタログアイテムのリスト。
 */
export async function fetchItems(
  categoryId: number,
  brandId: number,
  page?: number,
): Promise<PagedListOfGetCatalogItemResponse> {
  const response = await catalogItemsApi.getByQuery(
    brandId === 0 ? undefined : brandId,
    categoryId === 0 ? undefined : categoryId,
    page,
    undefined,
  );
  return response.data;
}

/**
 * 対象のIDを持つカタログアイテムの情報を取得します。
 * @param itemId アイテム ID。
 * @returns カタログアイテムの情報。
 */
export async function fetchItem(
  itemId: number,
): Promise<GetCatalogItemResponse> {
  const itemResponse = await catalogItemsApi.getCatalogItem(itemId);
  return itemResponse.data;
}

/**
 * アイテムをカタログに追加します。
 * @param name アイテム名。
 * @param description 説明。
 * @param price 単価。
 * @param productCode 商品コード。
 * @param catalogCategoryId カテゴリ ID。
 * @param catalogBrandId ブランド ID。
 */
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

/**
 * アイテムの情報を更新します。
 * @param id アイテム ID 。
 * @param name アイテム名。
 * @param description  説明。
 * @param price 単価。
 * @param productCode 商品コード。
 * @param catalogCategoryId カテゴリ ID。
 * @param catalogBrandId ブランド ID。
 * @param rowVersion 排他制御のための行バージョン。
 */
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

/**
 * 対象の ID を持つアイテムをカタログから削除します。
 * @param id アイテム ID 。
 */
export async function deleteCatalogItem(id: number, rowVersion: string) {
  await catalogItemsApi.deleteCatalogItem(id, rowVersion);
}
