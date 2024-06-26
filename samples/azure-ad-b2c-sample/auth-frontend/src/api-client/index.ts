import axios from 'axios';
import * as apiClient from '@/generated/api-client';
import { useAuthenticationStore } from '@/stores/authentication/authentication';

/** axios の共通の設定があればここに定義します。 */
const axiosInstance = axios.create({
  headers: {
    'Content-Type': 'application/json',
  },
  withCredentials: true,
});

/** api-client の共通の Configuration があればここに定義します。 */
function createConfig(): apiClient.Configuration {
  const config = new apiClient.Configuration({
    basePath: import.meta.env.VITE_AXIOS_BASE_ENDPOINT_ORIGIN,
  });

  return config;
}

async function addTokenAsync(config: apiClient.Configuration): Promise<void> {
  const store = useAuthenticationStore();

  // 認証済みの場合、アクセストークンを取得して Configuration に設定します。
  if (store.isAuthenticated) {
    await store.getToken();
    const token = store.getAccessToken;
    config.accessToken = token;
  }
}

export async function getUserApi(): Promise<apiClient.UserApi> {
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
