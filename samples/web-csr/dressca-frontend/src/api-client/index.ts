import axios from 'axios';
import * as apiClient from '@/generated/api-client';

/** api-client の共通の Configuration があればここに定義します。 */
const config = new apiClient.Configuration({});

/** axios の共通の設定があればここに定義します。 */
const axiosInstance = axios.create({});

const assetApi = new apiClient.AssetApi(config, '', axiosInstance);
const basketItemApi = new apiClient.BasketItemApi(config, '', axiosInstance);
const catalogBrandApi = new apiClient.CatalogBrandApi(
  config,
  '',
  axiosInstance,
);
const catalogCategoryApi = new apiClient.CatalogCategoryApi(
  config,
  '',
  axiosInstance,
);
const catalogItemApi = new apiClient.CatalogItemApi(config, '', axiosInstance);
const orderApi = new apiClient.OrderApi(config, '', axiosInstance);

export {
  assetApi,
  basketItemApi,
  catalogBrandApi,
  catalogCategoryApi,
  catalogItemApi,
  orderApi,
};
