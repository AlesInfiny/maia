import axios from 'axios';
import * as apiClient from '@/generated/api-client';
import { useAuthenticationStore } from '@/stores/authentication/authentication';

/** api-client の共通の Configuration があればここに定義します。 */
const config = new apiClient.Configuration({
  basePath: import.meta.env.VITE_AXIOS_BASE_ENDPOINT_ORIGIN,
});

/** axios の共通の設定があればここに定義します。 */
const axiosInstance = axios.create({
  headers: {
    'Content-Type': 'application/json',
  },
});
axiosInstance.interceptors.request.use(async (request) => {
  const store = useAuthenticationStore();

  if (store.isAuthenticated) {
    await store.getToken();
    const token = store.getAccessToken;
    console.log(`token value: ${token}`);
    request.headers['Authorization'] = `Bearer ${token}`;
  }
  return request;
});

const assetsApi = new apiClient.AssetApi(config, '', axiosInstance);
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
const userApi = new apiClient.UserApi(config, '', axiosInstance);

export {
  assetsApi,
  basketItemsApi,
  catalogBrandsApi,
  catalogCategoriesApi,
  catalogItemsApi,
  ordersApi,
  userApi,
};
