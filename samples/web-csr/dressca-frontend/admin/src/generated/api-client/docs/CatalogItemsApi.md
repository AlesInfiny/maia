# CatalogItemsApi

All URIs are relative to *http://localhost:8081*

|Method | HTTP request | Description|
|------------- | ------------- | -------------|
|[**deleteCatalogItem**](#deletecatalogitem) | **DELETE** /api/catalog-items/{catalogItemId} | カタログから指定したカタログアイテム ID のアイテムを削除します。|
|[**getByQuery**](#getbyquery) | **GET** /api/catalog-items | カタログアイテムを検索して返します。|
|[**getCatalogItem**](#getcatalogitem) | **GET** /api/catalog-items/{id} | 指定した ID のカタログアイテムを返します。|
|[**postCatalogItem**](#postcatalogitem) | **POST** /api/catalog-items | カタログにアイテムを追加します。|
|[**putCatalogItem**](#putcatalogitem) | **PUT** /api/catalog-items/{catalogItemId} | 指定した ID のカタログアイテムの情報を更新します。|

# **deleteCatalogItem**
> deleteCatalogItem()

カタログから指定したカタログアイテム ID のアイテムを削除します。

### Example

```typescript
import {
    CatalogItemsApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new CatalogItemsApi(configuration);

let catalogItemId: number; // (default to undefined)
let rowVersion: string; // (default to undefined)

const { status, data } = await apiInstance.deleteCatalogItem(
    catalogItemId,
    rowVersion
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **catalogItemId** | [**number**] |  | defaults to undefined|
| **rowVersion** | [**string**] |  | defaults to undefined|


### Return type

void (empty response body)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: Not defined


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
|**204** | 成功。 |  -  |
|**400** | リクエストエラー。 |  -  |
|**401** | 未認証。 |  -  |
|**404** | 指定した ID のアイテムがカタログに存在しません。 |  -  |
|**409** | 競合が発生。 |  -  |
|**500** | サーバーエラー。 |  -  |

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **getByQuery**
> PagedListOfGetCatalogItemResponse getByQuery()

カタログアイテムを検索して返します。

### Example

```typescript
import {
    CatalogItemsApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new CatalogItemsApi(configuration);

let brandId: number; // (optional) (default to 0)
let categoryId: number; // (optional) (default to 0)
let page: number; // (optional) (default to 1)
let pageSize: number; // (optional) (default to 20)

const { status, data } = await apiInstance.getByQuery(
    brandId,
    categoryId,
    page,
    pageSize
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **brandId** | [**number**] |  | (optional) defaults to 0|
| **categoryId** | [**number**] |  | (optional) defaults to 0|
| **page** | [**number**] |  | (optional) defaults to 1|
| **pageSize** | [**number**] |  | (optional) defaults to 20|


### Return type

**PagedListOfGetCatalogItemResponse**

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
|**200** | 成功。 |  -  |
|**400** | リクエストエラー。 |  -  |
|**401** | 未認証。 |  -  |
|**404** | 失敗。 |  -  |
|**500** | サーバーエラー。 |  -  |

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **getCatalogItem**
> GetCatalogItemResponse getCatalogItem()

指定した ID のカタログアイテムを返します。

### Example

```typescript
import {
    CatalogItemsApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new CatalogItemsApi(configuration);

let id: number; // (default to undefined)

const { status, data } = await apiInstance.getCatalogItem(
    id
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **id** | [**number**] |  | defaults to undefined|


### Return type

**GetCatalogItemResponse**

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
|**200** | 成功。 |  -  |
|**400** | リクエストエラー。 |  -  |
|**401** | 未認証。 |  -  |
|**404** | 指定した ID のアイテムがカタログに存在しません。 |  -  |
|**500** | サーバーエラー。 |  -  |

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **postCatalogItem**
> postCatalogItem(postCatalogItemRequest)

カタログにアイテムを追加します。

### Example

```typescript
import {
    CatalogItemsApi,
    Configuration,
    PostCatalogItemRequest
} from './api';

const configuration = new Configuration();
const apiInstance = new CatalogItemsApi(configuration);

let postCatalogItemRequest: PostCatalogItemRequest; //

const { status, data } = await apiInstance.postCatalogItem(
    postCatalogItemRequest
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **postCatalogItemRequest** | **PostCatalogItemRequest**|  | |


### Return type

void (empty response body)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: Not defined


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
|**201** | 成功。 |  -  |
|**400** | リクエストエラー。 |  -  |
|**401** | 未認証。 |  -  |
|**404** | 失敗。 |  -  |
|**500** | サーバーエラー。 |  -  |

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **putCatalogItem**
> putCatalogItem(putCatalogItemRequest)

指定した ID のカタログアイテムの情報を更新します。

### Example

```typescript
import {
    CatalogItemsApi,
    Configuration,
    PutCatalogItemRequest
} from './api';

const configuration = new Configuration();
const apiInstance = new CatalogItemsApi(configuration);

let catalogItemId: number; // (default to undefined)
let putCatalogItemRequest: PutCatalogItemRequest; //

const { status, data } = await apiInstance.putCatalogItem(
    catalogItemId,
    putCatalogItemRequest
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **putCatalogItemRequest** | **PutCatalogItemRequest**|  | |
| **catalogItemId** | [**number**] |  | defaults to undefined|


### Return type

void (empty response body)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: Not defined


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
|**204** | 成功。 |  -  |
|**400** | リクエストエラー。 |  -  |
|**401** | 未認証。 |  -  |
|**404** | 指定した ID のアイテムがカタログに存在しません。 |  -  |
|**409** | 競合が発生。 |  -  |
|**500** | サーバーエラー。 |  -  |

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

