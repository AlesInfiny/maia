# CatalogItemsApi

All URIs are relative to *http://localhost:8080*

|Method | HTTP request | Description|
|------------- | ------------- | -------------|
|[**getByQuery**](#getbyquery) | **GET** /api/catalog-items | カタログアイテムを検索して返します。|

# **getByQuery**
> PagedListOfCatalogItemResponse getByQuery()

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

**PagedListOfCatalogItemResponse**

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json, application/problem+json


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
|**200** | 成功。 |  -  |
|**400** | リクエストエラー。 |  -  |

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

