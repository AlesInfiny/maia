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

/**
 * api-client の共通の Configuration を生成します。
 * 共通の Configuration があればここに定義してください。
 * @returns 新しい Configuration インスタンス。
 */
function createConfig(): apiClient.Configuration {
  const config = new apiClient.Configuration()
  return config
}

/**
 * 認証済みであれば、Azure AD B2C からアクセストークンを取得し、
 * 指定された Configuration に設定します。
 * @param config 設定対象の Configuration インスタンス。
 * @returns 非同期処理の完了を表す Promise。
 */
async function addTokenAsync(config: apiClient.Configuration): Promise<void> {
  // 認証済みの場合、アクセストークンを取得して Configuration に設定します。
  if (authenticationService.isAuthenticated()) {
    const token = await authenticationService.getTokenAzureADB2C()
    config.accessToken = token
  }
}

/**
 * 認証付きの UsersApi インスタンスを生成して返します。
 * UsersApi の呼び出しには認証が必要なため、内部でトークンを付与します。
 * @returns 認証済みの UsersApi インスタンス。
 */
export async function getUsersApi(): Promise<apiClient.UsersApi> {
  const config = createConfig()

  // UsersApi は認証が必要な API なので、addTokenAsync を呼び出します。
  await addTokenAsync(config)
  const userApi = new apiClient.UsersApi(config, '', axiosInstance)
  return userApi
}

/**
 * 認証不要な ServerTimeApi インスタンスを生成して返します。
 * @returns ServerTimeApi インスタンス。
 */
export function getServerTimeApi(): apiClient.ServerTimeApi {
  const config = createConfig()
  const serverTimeApi = new apiClient.ServerTimeApi(config, '', axiosInstance)
  return serverTimeApi
}
