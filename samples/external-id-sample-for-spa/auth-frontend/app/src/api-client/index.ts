import axios, { HttpStatusCode } from 'axios'
import * as apiClient from '@/generated/api-client'
import { authenticationService } from '@/services/authentication/authentication-service'
import {
  HttpError,
  NetworkError,
  ServerError,
  UnauthorizedError,
  UnknownError,
} from '@/shared/error-handler/custom-error'

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
 * @returns 新しい Configuration インスタンス
 */
function createConfig(): apiClient.Configuration {
  const config = new apiClient.Configuration()
  return config
}

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
 * ユーザー API のクライアントを生成します。
 * @returns UsersApi インスタンス
 */
export async function getUsersApi(): Promise<apiClient.UsersApi> {
  const config = createConfig()

  // UsersApi は認証が必要な API なので、addToken を呼び出します。
  await addToken(config)
  const userApi = new apiClient.UsersApi(config, '', axiosInstance)
  return userApi
}

/**
 * サーバー時刻 API のクライアントを生成します。
 * @returns ServerTimeApi インスタンス
 */
export function getServerTimeApi(): apiClient.ServerTimeApi {
  const config = createConfig()
  const serverTimeApi = new apiClient.ServerTimeApi(config, '', axiosInstance)
  return serverTimeApi
}
