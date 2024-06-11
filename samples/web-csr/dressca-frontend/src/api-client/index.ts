import axios from 'axios';
import * as apiClient from '@/generated/api-client';

/** api-client の共通の Configuration があればここに定義します。 */
const config = new apiClient.Configuration({});

/** axios の共通の設定があればここに定義します。 */
const axiosInstance = axios.create({});

const assetsApi = new apiClient.AssetsApi(config, '', axiosInstance);
const basketItemsApi = new apiClient.BasketItemsApi(config, '', axiosInstance);
const catalogBrandsApi = new apiClient.CatalogBrandsApi(
  config,
  '',
  axiosInstance,
);
const catalogCategoriesApi = new apiClient.CatalogCategoriesApi(
  config,
  '',
  axiosInstance,
);
const catalogItemsApi = new apiClient.CatalogItemsApi(
  config,
  '',
  axiosInstance,
);
const ordersApi = new apiClient.OrdersApi(config, '', axiosInstance);

export {
  assetsApi,
  basketItemsApi,
  catalogBrandsApi,
  catalogCategoriesApi,
  catalogItemsApi,
  ordersApi,
};
