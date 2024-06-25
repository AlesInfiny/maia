import axios from 'axios';
import * as apiClient from '@/generated/api-client';

/** api-client の共通の Configuration があればここに定義します。 */
function createConfig(): apiClient.Configuration {
  const config = new apiClient.Configuration({
    basePath: import.meta.env.VITE_AXIOS_BASE_ENDPOINT_ORIGIN,
  });
  return config;
}

/** axios の共通の設定があればここに定義します。 */
const axiosInstance = axios.create({
  headers: {
    'Content-Type': 'application/json',
  },
  withCredentials: true,
});

const assetsApi = new apiClient.AssetsApi(createConfig(), '', axiosInstance);
const basketItemsApi = new apiClient.BasketItemsApi(
  createConfig(),
  '',
  axiosInstance,
);
const catalogBrandsApi = new apiClient.CatalogBrandsApi(
  createConfig(),
  '',
  axiosInstance,
);
const catalogCategoriesApi = new apiClient.CatalogCategoriesApi(
  createConfig(),
  '',
  axiosInstance,
);
const catalogItemsApi = new apiClient.CatalogItemsApi(
  createConfig(),
  '',
  axiosInstance,
);
const ordersApi = new apiClient.OrdersApi(createConfig(), '', axiosInstance);

export {
  assetsApi,
  basketItemsApi,
  catalogBrandsApi,
  catalogCategoriesApi,
  catalogItemsApi,
  ordersApi,
};
