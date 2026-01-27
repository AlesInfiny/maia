# ActuatorApi

All URIs are relative to *http://localhost:8080*

|Method | HTTP request | Description|
|------------- | ------------- | -------------|
|[**health**](#health) | **GET** /api/health | Actuator web endpoint \&#39;health\&#39;|
|[**links**](#links) | **GET** /api | Actuator root web endpoint|

# **health**
> object health()


### Example

```typescript
import {
    ActuatorApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new ActuatorApi(configuration);

const { status, data } = await apiInstance.health();
```

### Parameters
This endpoint does not have any parameters.


### Return type

**object**

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json, application/vnd.spring-boot.actuator.v2+json, application/vnd.spring-boot.actuator.v3+json


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
|**200** | OK |  -  |

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **links**
> { [key: string]: { [key: string]: Link; }; } links()


### Example

```typescript
import {
    ActuatorApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new ActuatorApi(configuration);

const { status, data } = await apiInstance.links();
```

### Parameters
This endpoint does not have any parameters.


### Return type

**{ [key: string]: { [key: string]: Link; }; }**

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json, application/vnd.spring-boot.actuator.v2+json, application/vnd.spring-boot.actuator.v3+json


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
|**200** | OK |  -  |

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

