import axios from 'axios';
import * as apiClient from '@/generated/api-client';
import {
  ConflictError,
  HttpError,
  NetworkError,
  NotFoundError,
  ServerError,
  UnauthorizedError,
} from '@/shared/error-handler/custom-error';

/** api-client の共通の Configuration があればここに定義します。 */
function createConfig(): apiClient.Configuration {
  const config = new apiClient.Configuration();
  return config;
}

/** axios の共通の設定があればここに定義します。 */
const axiosInstance = axios.create({
  baseURL: import.meta.env.VITE_AXIOS_BASE_ENDPOINT_ORIGIN,
  headers: {
    'Content-Type': 'application/json',
  },
  withCredentials: true,
});

/** レスポンスのステータスコードに応じてカスタムエラーを割り当てます。 */
axiosInstance.interceptors.response.use(
  (response) => response,
  (error) => {
    if (axios.isAxiosError(error)) {
      if (!error.response) {
        return Promise.reject(new NetworkError('Network Error', error));
      }
      if (error.response.status === 500) {
        return Promise.reject(new ServerError('Server Error', error));
      }
      if (error.response.status === 401) {
        return Promise.reject(
          new UnauthorizedError('Unauthorized Error', error),
        );
      }
      if (error.response.status === 404) {
        return Promise.reject(new NotFoundError('NotFound Error', error));
      }
      if (error.response.status === 409) {
        return Promise.reject(new ConflictError('Conflict Error', error));
      }
      return Promise.reject(new HttpError(error.message, error));
    }
    return Promise.reject(error);
  },
);

/**
 * カタログブランド API のクライアントです。
 */
const catalogBrandsApi = new apiClient.CatalogBrandsApi(
  createConfig(),
  '',
  axiosInstance,
);

/**
 * カタログカテゴリ API のクライアントです。
 */
const catalogCategoriesApi = new apiClient.CatalogCategoriesApi(
  createConfig(),
  '',
  axiosInstance,
);

/**
 * カタログアイテム API のクライアントです。
 */
const catalogItemsApi = new apiClient.CatalogItemsApi(
  createConfig(),
  '',
  axiosInstance,
);

/**
 * ユーザー API のクライアントです。
 */
const UsersApi = new apiClient.UsersApi(createConfig(), '', axiosInstance);
export { catalogBrandsApi, catalogCategoriesApi, catalogItemsApi, UsersApi };
