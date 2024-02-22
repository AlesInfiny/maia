import axios from 'axios';
import * as apiClient from '@/generated/api-client';

/** api-client の共通の Configuration があればここに定義します。 */
const config = new apiClient.Configuration({});

/** axios の共通の設定があればここに定義します。 */
const axiosInstance = axios.create({});

const assetsApi = new apiClient.AssetApi(config, '', axiosInstance);
const userApi = new apiClient.UserApi(config, '', axiosInstance);
const basketItemsApi = new apiClient.BasketItemApi(config, '', axiosInstance);
const catalogBrandsApi = new apiClient.CatalogBrandApi(
  config,
  '',
  axiosInstance,
);
const catalogCategoriesApi = new apiClient.CatalogCategoryApi(
  config,
  '',
  axiosInstance,
);
const catalogItemsApi = new apiClient.CatalogItemApi(config, '', axiosInstance);
const ordersApi = new apiClient.OrderApi(config, '', axiosInstance);

export {
  assetsApi,
  userApi,
  basketItemsApi,
  catalogBrandsApi,
  catalogCategoriesApi,
  catalogItemsApi,
  ordersApi,
};
