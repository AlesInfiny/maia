import axios from 'axios';
import * as apiClient from '@/generated/api-client';
import { authenticationService } from '@/services/authentication/authentication-service';

/** axios の共通の設定があればここに定義します。 */
const axiosInstance = axios.create({
  baseURL: import.meta.env.VITE_AXIOS_BASE_ENDPOINT_ORIGIN,
  headers: {
    'Content-Type': 'application/json',
  },
  withCredentials: true,
});

/** api-client の共通の Configuration があればここに定義します。 */
function createConfig(): apiClient.Configuration {
  const config = new apiClient.Configuration();
  return config;
}

async function addTokenAsync(config: apiClient.Configuration): Promise<void> {
  // 認証済みの場合、アクセストークンを取得して Configuration に設定します。
  if (await authenticationService.isAuthenticated()) {
    const token = await authenticationService.getTokenAzureADB2C();
    // eslint-disable-next-line no-param-reassign
    config.accessToken = token;
  }
}

export async function getUsersApi(): Promise<apiClient.UserApi> {
  const config = createConfig();

  // UsersApi は認証が必要な API なので、addTokenAsync を呼び出します。
  await addTokenAsync(config);
  const userApi = new apiClient.UserApi(config, '', axiosInstance);
  return userApi;
}

export async function getServerTimeApi(): Promise<apiClient.ServerTimeApi> {
  const config = createConfig();
  const serverTimeApi = new apiClient.ServerTimeApi(config, '', axiosInstance);
  return serverTimeApi;
}
