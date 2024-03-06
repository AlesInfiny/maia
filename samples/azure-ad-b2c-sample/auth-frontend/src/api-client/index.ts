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
axiosInstance.interceptors.request.use(
  async (config: InternalAxiosRequestConfig) => {
    const store = useAuthenticationStore();
    if (store.isAuthenticated) {
      await store.getToken();
      const token = store.accessToken;
      config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
  },
);

const userApi = new apiClient.UserApi(config, '', axiosInstance);

export { userApi };
