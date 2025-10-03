import axios, { HttpStatusCode } from 'axios'
import * as apiClient from '@/generated/api-client'
import {
  ConflictError,
  HttpError,
  NetworkError,
  NotFoundError,
  ServerError,
  UnauthorizedError,
  UnknownError,
} from '@/shared/error-handler/custom-error'

/**
 * api-client の共通の Configuration を生成します。
 * 共通の Configuration があればここに定義してください。
 * @returns 新しい Configuration インスタンス。
 */
function createConfig(): apiClient.Configuration {
  const config = new apiClient.Configuration()
  return config
}

/** axios の共通の設定があればここに定義します。 */
export const axiosInstance = axios.create({
  baseURL: import.meta.env.VITE_AXIOS_BASE_ENDPOINT_ORIGIN,
  headers: {
    'Content-Type': 'application/json',
  },
  withCredentials: true,
})

/** レスポンスのステータスコードに応じてカスタムエラーを割り当てます。 */
axiosInstance.interceptors.response.use(
  (response) => response,
  (error) => {
    if (axios.isAxiosError(error)) {
      if (!error.response) {
        return Promise.reject(new NetworkError(error.message, error))
      }
      if (error.response.status === Number(HttpStatusCode.InternalServerError)) {
        return Promise.reject(new ServerError(error.message, error))
      }
      if (error.response.status === Number(HttpStatusCode.Unauthorized)) {
        return Promise.reject(new UnauthorizedError(error.message, error))
      }
      if (error.response.status === Number(HttpStatusCode.NotFound)) {
        return Promise.reject(new NotFoundError(error.message, error))
      }
      if (error.response.status === Number(HttpStatusCode.Conflict)) {
        return Promise.reject(new ConflictError(error.message, error))
      }
      return Promise.reject(new HttpError(error.message, error))
    }
    return Promise.reject(new UnknownError('Unknown Error', error))
  },
)

/**
 * カタログブランド API のクライアントを生成します。
 * @returns CatalogBrandsApi インスタンス
 */
function catalogBrandsApi() {
  const catalogBrandsApi = new apiClient.CatalogBrandsApi(createConfig(), '', axiosInstance)
  return catalogBrandsApi
}

/**
 * カタログカテゴリ API のクライアントを生成します。
 * @returns CatalogCategoriesApi インスタンス
 */
function catalogCategoriesApi() {
  const catalogCategoriesApi = new apiClient.CatalogCategoriesApi(createConfig(), '', axiosInstance)
  return catalogCategoriesApi
}

/**
 * カタログアイテム API のクライアントを生成します。
 * @returns CatalogItemsApi インスタンス
 */
function catalogItemsApi() {
  const catalogItemsApi = new apiClient.CatalogItemsApi(createConfig(), '', axiosInstance)
  return catalogItemsApi
}

/**
 * ユーザー API のクライアントを生成します。
 * @returns UsersApi インスタンス
 */
function usersApi() {
  const usersApi = new apiClient.UsersApi(createConfig(), '', axiosInstance)
  return usersApi
}

export { catalogBrandsApi, catalogCategoriesApi, catalogItemsApi, usersApi }
