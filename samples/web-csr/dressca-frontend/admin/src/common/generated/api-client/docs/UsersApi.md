# UsersApi

All URIs are relative to *http://localhost:8081*

|Method | HTTP request | Description|
|------------- | ------------- | -------------|
|[**getLoginUser**](#getloginuser) | **GET** /api/users | ログイン中のユーザーの情報を取得します。|

# **getLoginUser**
> GetLoginUserResponse getLoginUser()

ログイン中のユーザーの情報を取得します。

### Example

```typescript
import {
    UsersApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new UsersApi(configuration);

const { status, data } = await apiInstance.getLoginUser();
```

### Parameters
This endpoint does not have any parameters.


### Return type

**GetLoginUserResponse**

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

