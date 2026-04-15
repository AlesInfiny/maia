# CatalogCategoriesApi

All URIs are relative to *http://localhost:8081*

|Method | HTTP request | Description|
|------------- | ------------- | -------------|
|[**getCatalogCategories**](#getcatalogcategories) | **GET** /api/catalog-categories | カタログカテゴリの一覧を取得します。|

# **getCatalogCategories**
> Array<GetCatalogCategoriesResponse> getCatalogCategories()

カタログカテゴリの一覧を取得します。

### Example

```typescript
import {
    CatalogCategoriesApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new CatalogCategoriesApi(configuration);

const { status, data } = await apiInstance.getCatalogCategories();
```

### Parameters
This endpoint does not have any parameters.


### Return type

**Array<GetCatalogCategoriesResponse>**

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
|**200** | 成功。 |  -  |
|**401** | 未認証。 |  -  |
|**500** | サーバーエラー。 |  -  |

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

