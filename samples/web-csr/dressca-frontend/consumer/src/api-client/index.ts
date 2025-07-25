import axios, { HttpStatusCode } from 'axios'
import * as apiClient from '@/generated/api-client'
import {
  HttpError,
  NetworkError,
  ServerError,
  UnauthorizedError,
  UnknownError,
} from '@/shared/error-handler/custom-error'

/** api-client の共通の Configuration があればここに定義します。 */
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
      return Promise.reject(new HttpError(error.message, error))
    }
    return Promise.reject(new UnknownError('Unknown Error', error))
  },
)

const assetsApi = new apiClient.AssetsApi(createConfig(), '', axiosInstance)
const basketItemsApi = new apiClient.BasketItemsApi(createConfig(), '', axiosInstance)
const catalogBrandsApi = new apiClient.CatalogBrandsApi(createConfig(), '', axiosInstance)
const catalogCategoriesApi = new apiClient.CatalogCategoriesApi(createConfig(), '', axiosInstance)
const catalogItemsApi = new apiClient.CatalogItemsApi(createConfig(), '', axiosInstance)
const ordersApi = new apiClient.OrdersApi(createConfig(), '', axiosInstance)

export {
  assetsApi,
  basketItemsApi,
  catalogBrandsApi,
  catalogCategoriesApi,
  catalogItemsApi,
  ordersApi,
  axiosInstance,
}
