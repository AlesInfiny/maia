import axios, { HttpStatusCode } from 'axios'
import * as apiClient from '@/generated/api-client'
import { authenticationService } from '@/services/authentication/authentication-service'
import {
  HttpError,
  NetworkError,
  ServerError,
  UnauthorizedError,
  UnknownError,
} from '@/shared/custom-errors'

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
      return Promise.reject(new HttpError(error.message, error))
    }
    return Promise.reject(new UnknownError('Unknown Error', error))
  },
)

/** api-client の共通の Configuration があればここに定義します。 */
function createConfig(): apiClient.Configuration {
  const config = new apiClient.Configuration()
  return config
}

async function addTokenAsync(config: apiClient.Configuration): Promise<void> {
  // 認証済みの場合、アクセストークンを取得して Configuration に設定します。
  if (authenticationService.isAuthenticated()) {
    const token = await authenticationService.getTokenEntraExternalId()
    config.accessToken = token
  }
}

export async function getUsersApi(): Promise<apiClient.UsersApi> {
  const config = createConfig()

  // UsersApi は認証が必要な API なので、addTokenAsync を呼び出します。
  await addTokenAsync(config)
  const userApi = new apiClient.UsersApi(config, '', axiosInstance)
  return userApi
}

export function getServerTimeApi(): apiClient.ServerTimeApi {
  const config = createConfig()
  const serverTimeApi = new apiClient.ServerTimeApi(config, '', axiosInstance)
  return serverTimeApi
}
