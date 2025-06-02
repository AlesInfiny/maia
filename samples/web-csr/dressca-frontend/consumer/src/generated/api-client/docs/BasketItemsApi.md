# BasketItemsApi

All URIs are relative to *http://localhost:8080*

|Method | HTTP request | Description|
|------------- | ------------- | -------------|
|[**deleteBasketItem**](#deletebasketitem) | **DELETE** /api/basket-items/{catalogItemId} | 買い物かごから指定したカタログアイテム ID の商品を削除します。|
|[**getBasketItems**](#getbasketitems) | **GET** /api/basket-items | 買い物かごアイテムの一覧を取得します。|
|[**postBasketItem**](#postbasketitem) | **POST** /api/basket-items | 買い物かごに商品を追加します。|
|[**putBasketItems**](#putbasketitems) | **PUT** /api/basket-items | 買い物かごアイテム内の数量を変更します。|

# **deleteBasketItem**
> deleteBasketItem()

買い物かごから指定したカタログアイテム ID の商品を削除します。<br>catalogItemId には買い物かご内に存在するカタログアイテム ID を指定してください。カタログアイテム ID は 1 以上の整数です。0 以下の値を指定したり、整数値ではない値を指定した場合 HTTP 400 を返却します。買い物かご内に指定したカタログアイテムの商品が存在しない場合、 HTTP 404 を返却します。

### Example

```typescript
import {
    BasketItemsApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new BasketItemsApi(configuration);

let catalogItemId: number; // (default to undefined)

const { status, data } = await apiInstance.deleteBasketItem(
    catalogItemId
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **catalogItemId** | [**number**] |  | defaults to undefined|


### Return type

void (empty response body)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/problem+json


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
|**204** | 成功。 |  -  |
|**400** | リクエストエラー。 |  -  |
|**404** | 買い物かご内に指定したカタログアイテム ID がありません。 |  -  |

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **getBasketItems**
> BasketResponse getBasketItems()

買い物かごアイテムの一覧を返却します。

### Example

```typescript
import {
    BasketItemsApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new BasketItemsApi(configuration);

const { status, data } = await apiInstance.getBasketItems();
```

### Parameters
This endpoint does not have any parameters.


### Return type

**BasketResponse**

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

# **postBasketItem**
> postBasketItem(postBasketItemsRequest)

買い物かごに商品を追加します。<br>この API では、システムに登録されていないカタログアイテム ID を指定した場合 HTTP 400 を返却します。また買い物かごに追加していないカタログアイテムを指定した場合、その商品を買い物かごに追加します。すでに買い物かごに追加されているカタログアイテムを指定した場合、指定した数量、買い物かご内の数量を追加します。<br>買い物かご内のカタログアイテムの数量が 0 未満になるように減じることはできません。計算の結果数量が 0 未満になる場合 HTTP 500 を返却します。

### Example

```typescript
import {
    BasketItemsApi,
    Configuration,
    PostBasketItemsRequest
} from './api';

const configuration = new Configuration();
const apiInstance = new BasketItemsApi(configuration);

let postBasketItemsRequest: PostBasketItemsRequest; //

const { status, data } = await apiInstance.postBasketItem(
    postBasketItemsRequest
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **postBasketItemsRequest** | **PostBasketItemsRequest**|  | |


### Return type

void (empty response body)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/problem+json


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
|**201** | 作成完了。 |  -  |
|**400** | リクエストエラー。 |  -  |
|**500** | サーバーエラー。 |  -  |

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **putBasketItems**
> putBasketItems(putBasketItemsRequest)

買い物かごアイテム内の数量を変更します。買い物かご内に存在しないカタログアイテム ID は指定できません。<br>この API では、買い物かご内に存在する商品の数量を変更できます。買い物かご内に存在しないカタログアイテム ID を指定すると HTTP 400 を返却します。<br>またシステムに登録されていないカタログアイテム ID を指定した場合も HTTP 400 を返却します。

### Example

```typescript
import {
    BasketItemsApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new BasketItemsApi(configuration);

let putBasketItemsRequest: Array<PutBasketItemsRequest>; //

const { status, data } = await apiInstance.putBasketItems(
    putBasketItemsRequest
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **putBasketItemsRequest** | **Array<PutBasketItemsRequest>**|  | |


### Return type

void (empty response body)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/problem+json


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
|**204** | 成功。 |  -  |
|**400** | リクエストエラー。 |  -  |

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

