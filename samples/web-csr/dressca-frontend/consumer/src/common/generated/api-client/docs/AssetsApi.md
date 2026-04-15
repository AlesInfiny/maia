# AssetsApi

All URIs are relative to *http://localhost:8080*

|Method | HTTP request | Description|
|------------- | ------------- | -------------|
|[**get**](#get) | **GET** /api/assets/{assetCode} | アセットを取得します。|

# **get**
> File get()

与えられたアセットコードに対応するアセットを返却します。

### Example

```typescript
import {
    AssetsApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new AssetsApi(configuration);

let assetCode: string; //アセットコード (default to undefined)

const { status, data } = await apiInstance.get(
    assetCode
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **assetCode** | [**string**] | アセットコード | defaults to undefined|


### Return type

**File**

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: image/*, application/problem+json


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
|**200** | 成功。 |  -  |
|**404** | アセットコードに対応するアセットがありません。 |  -  |

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

