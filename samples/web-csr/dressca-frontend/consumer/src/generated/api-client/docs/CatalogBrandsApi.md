# CatalogBrandsApi

All URIs are relative to *http://localhost:8080*

|Method | HTTP request | Description|
|------------- | ------------- | -------------|
|[**getCatalogBrands**](#getcatalogbrands) | **GET** /api/catalog-brands | カタログブランドの一覧を取得します。|

# **getCatalogBrands**
> Array<CatalogBrandResponse> getCatalogBrands()

カタログブランドの一覧を取得します。

### Example

```typescript
import {
    CatalogBrandsApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new CatalogBrandsApi(configuration);

const { status, data } = await apiInstance.getCatalogBrands();
```

### Parameters
This endpoint does not have any parameters.


### Return type

**Array<CatalogBrandResponse>**

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
|**200** | 成功。 |  -  |

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

