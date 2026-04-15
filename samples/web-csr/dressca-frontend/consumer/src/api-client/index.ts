import axios, { HttpStatusCode } from 'axios'
import * as apiClient from '@/generated/api-client'
import { getRequestAbortSignal } from '@/api-client/request-abort-manager'
import { authenticationService } from '@/services/authentication/authentication-service'
import {
  HttpError,
  NetworkError,
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
const axiosInstance = axios.create({
  baseURL: import.meta.env.VITE_AXIOS_BASE_ENDPOINT_ORIGIN,
  headers: {
    'Content-Type': 'application/json',
  },
  withCredentials: true,
})

// リクエストインターセプター: すべてのリクエストに AbortController の signal を設定します。
axiosInstance.interceptors.request.use((config) => {
  config.signal = getRequestAbortSignal()
  return config
})

axiosInstance.interceptors.response.use(
  (response) => response,
  (error) => {
    if (axios.isCancel(error)) {
      return Promise.reject(error) // CanceledError を呼び出し元で処理させます。
    }
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
      return Promise.reject(new HttpError(error.message, error))
    }
    return Promise.reject(new UnknownError('Unknown Error', error))
  },
)

/**
 * 認証済みの場合、アクセストークンを取得して Configuration に設定します。
 * @param config 新しい Configuration インスタンス
 */
async function addToken(config: apiClient.Configuration): Promise<void> {
  const { isAuthenticated, getToken } = authenticationService()
  if (isAuthenticated()) {
    const token = await getToken()
    config.accessToken = token
  }
}

/**
 * アセット関連 API のクライアントを生成します。
 * @returns AssetsApi インスタンス
 */
function assetsApi() {
  const assetsApi = new apiClient.AssetsApi(createConfig(), '', axiosInstance)
  return assetsApi
}

/**
 * 買い物かごアイテム関連 API のクライアントを生成します。
 * @returns BasketItemsApi インスタンス
 */
function basketItemsApi() {
  const basketItemsApi = new apiClient.BasketItemsApi(createConfig(), '', axiosInstance)
  return basketItemsApi
}

/**
 * カタログブランド関連 API のクライアントを生成します。
 * @returns CatalogBrandsApi インスタンス
 */
function catalogBrandsApi() {
  const catalogBrandsApi = new apiClient.CatalogBrandsApi(createConfig(), '', axiosInstance)
  return catalogBrandsApi
}

/**
 * カタログカテゴリ関連 API のクライアントを生成します。
 * @returns CatalogCategoriesApi インスタンス
 */
function catalogCategoriesApi() {
  const catalogCategoriesApi = new apiClient.CatalogCategoriesApi(createConfig(), '', axiosInstance)
  return catalogCategoriesApi
}

/**
 * カタログアイテム関連 API のクライアントを生成します。
 * @returns CatalogItemsApi インスタンス
 */
function catalogItemsApi() {
  const catalogItemsApi = new apiClient.CatalogItemsApi(createConfig(), '', axiosInstance)
  return catalogItemsApi
}

/**
 * 注文関連 API のクライアントを生成します。
 * @returns OrdersApi インスタンス
 */
async function ordersApi() {
  const config = createConfig()
  // 認証が必要な API では、addToken を呼び出します。
  await addToken(config)
  const ordersApi = new apiClient.OrdersApi(config, '', axiosInstance)
  return ordersApi
}

export {
  assetsApi,
  basketItemsApi,
  catalogBrandsApi,
  catalogCategoriesApi,
  catalogItemsApi,
  ordersApi,
  axiosInstance,
}
