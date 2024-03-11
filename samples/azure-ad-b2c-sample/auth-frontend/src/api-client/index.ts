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
axiosInstance.interceptors.request.use(async (request) => {
  const store = useAuthenticationStore();

  if (store.isAuthenticated) {
    await store.getToken();
    const token = store.getAccessToken;
    request.headers['Authorization'] = `Bearer ${token}`;
  }
  return request;
});

const userApi = new apiClient.UserApi(config, '', axiosInstance);

export { userApi };
