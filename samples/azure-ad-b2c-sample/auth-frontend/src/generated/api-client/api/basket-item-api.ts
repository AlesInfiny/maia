/* tslint:disable */
/* eslint-disable */
/**
 * Dressca
 * ECサイトDressca
 *
 * The version of the OpenAPI document: v1
 * 
 *
 * NOTE: This class is auto generated by OpenAPI Generator (https://openapi-generator.tech).
 * https://openapi-generator.tech
 * Do not edit the class manually.
 */


import globalAxios, { AxiosPromise, AxiosInstance, AxiosRequestConfig } from 'axios';
import { Configuration } from '../configuration';
// Some imports not used depending on template conditions
// @ts-ignore
import { DUMMY_BASE_URL, assertParamExists, setApiKeyToObject, setBasicAuthToObject, setBearerAuthToObject, setOAuthToObject, setSearchParams, serializeDataIfNeeded, toPathString, createRequestFunction } from '../common';
// @ts-ignore
import { BASE_PATH, COLLECTION_FORMATS, RequestArgs, BaseAPI, RequiredError } from '../base';
// @ts-ignore
import { BasketResponse } from '../models';
// @ts-ignore
import { PostBasketItemsRequest } from '../models';
// @ts-ignore
import { PutBasketItemRequest } from '../models';
/**
 * BasketItemApi - axios parameter creator
 * @export
 */
export const BasketItemApiAxiosParamCreator = function (configuration?: Configuration) {
    return {
        /**
         * 買い物かごから指定したカタログアイテム Id の商品を削除します.<br>catalogItemId には買い物かご内に存在するカタログアイテム Id を指定してください. カタログアイテム Id は 1 以上の整数です.0以下の値を指定したり、整数値ではない値を指定した場合 HTTP 400 を返却します. 買い物かご内に指定したカタログアイテムの商品が存在しない場合、 HTTP 404 を返却します.
         * @summary 買い物かごから指定したカタログアイテム Id の商品を削除します.
         * @param {number} catalogItemId 
         * @param {*} [options] Override http request option.
         * @throws {RequiredError}
         */
        deleteBasketItemAsync: async (catalogItemId: number, options: AxiosRequestConfig = {}): Promise<RequestArgs> => {
            // verify required parameter 'catalogItemId' is not null or undefined
            assertParamExists('deleteBasketItemAsync', 'catalogItemId', catalogItemId)
            const localVarPath = `/api/basket-items/{catalogItemId}`
                .replace(`{${"catalogItemId"}}`, encodeURIComponent(String(catalogItemId)));
            // use dummy base URL string because the URL constructor only accepts absolute URLs.
            const localVarUrlObj = new URL(localVarPath, DUMMY_BASE_URL);
            let baseOptions;
            if (configuration) {
                baseOptions = configuration.baseOptions;
            }

            const localVarRequestOptions = { method: 'DELETE', ...baseOptions, ...options};
            const localVarHeaderParameter = {} as any;
            const localVarQueryParameter = {} as any;


    
            setSearchParams(localVarUrlObj, localVarQueryParameter);
            let headersFromBaseOptions = baseOptions && baseOptions.headers ? baseOptions.headers : {};
            localVarRequestOptions.headers = {...localVarHeaderParameter, ...headersFromBaseOptions, ...options.headers};

            return {
                url: toPathString(localVarUrlObj),
                options: localVarRequestOptions,
            };
        },
        /**
         * 買い物かごアイテムの一覧を返却する.
         * @summary 買い物かごアイテムの一覧を取得する.
         * @param {*} [options] Override http request option.
         * @throws {RequiredError}
         */
        getBasketItems: async (options: AxiosRequestConfig = {}): Promise<RequestArgs> => {
            const localVarPath = `/api/basket-items`;
            // use dummy base URL string because the URL constructor only accepts absolute URLs.
            const localVarUrlObj = new URL(localVarPath, DUMMY_BASE_URL);
            let baseOptions;
            if (configuration) {
                baseOptions = configuration.baseOptions;
            }

            const localVarRequestOptions = { method: 'GET', ...baseOptions, ...options};
            const localVarHeaderParameter = {} as any;
            const localVarQueryParameter = {} as any;


    
            setSearchParams(localVarUrlObj, localVarQueryParameter);
            let headersFromBaseOptions = baseOptions && baseOptions.headers ? baseOptions.headers : {};
            localVarRequestOptions.headers = {...localVarHeaderParameter, ...headersFromBaseOptions, ...options.headers};

            return {
                url: toPathString(localVarUrlObj),
                options: localVarRequestOptions,
            };
        },
        /**
         * 買い物かごに商品を追加します.<br>この API では、システムに登録されていないカタログアイテム Id を指定した場合 HTTP 400 を返却します.また買い物かごに追加していないカタログアイテムを指定した場合、その商品を買い物かごに追加します.すでに買い物かごに追加されているカタログアイテムを指定した場合、指定した数量、買い物かご内の数量を追加します.<br>買い物かご内のカタログアイテムの数量が 0 未満になるように減じることはできません. 計算の結果数量が 0 未満になる場合 HTTP 500 を返却します.
         * @summary 買い物かごに商品を追加します.
         * @param {PostBasketItemsRequest} postBasketItemsRequest 
         * @param {*} [options] Override http request option.
         * @throws {RequiredError}
         */
        postBasketItem: async (postBasketItemsRequest: PostBasketItemsRequest, options: AxiosRequestConfig = {}): Promise<RequestArgs> => {
            // verify required parameter 'postBasketItemsRequest' is not null or undefined
            assertParamExists('postBasketItem', 'postBasketItemsRequest', postBasketItemsRequest)
            const localVarPath = `/api/basket-items`;
            // use dummy base URL string because the URL constructor only accepts absolute URLs.
            const localVarUrlObj = new URL(localVarPath, DUMMY_BASE_URL);
            let baseOptions;
            if (configuration) {
                baseOptions = configuration.baseOptions;
            }

            const localVarRequestOptions = { method: 'POST', ...baseOptions, ...options};
            const localVarHeaderParameter = {} as any;
            const localVarQueryParameter = {} as any;


    
            localVarHeaderParameter['Content-Type'] = 'application/json';

            setSearchParams(localVarUrlObj, localVarQueryParameter);
            let headersFromBaseOptions = baseOptions && baseOptions.headers ? baseOptions.headers : {};
            localVarRequestOptions.headers = {...localVarHeaderParameter, ...headersFromBaseOptions, ...options.headers};
            localVarRequestOptions.data = serializeDataIfNeeded(postBasketItemsRequest, localVarRequestOptions, configuration)

            return {
                url: toPathString(localVarUrlObj),
                options: localVarRequestOptions,
            };
        },
        /**
         * 買い物かごアイテム内の数量を変更します. 買い物かご内に存在しないカタログアイテム ID は指定できません.<br>この API では、買い物かご内に存在する商品の数量を変更できます. 買い物かご内に存在しないカタログアイテム Id を指定すると HTTP 400 を返却します.<br>またシステムに登録されていないカタログアイテム Id を指定した場合も HTTP 400 を返却します.
         * @summary 買い物かごアイテム内の数量を変更します.
         * @param {Array<PutBasketItemRequest>} putBasketItemRequest 
         * @param {*} [options] Override http request option.
         * @throws {RequiredError}
         */
        putBasketItem: async (putBasketItemRequest: Array<PutBasketItemRequest>, options: AxiosRequestConfig = {}): Promise<RequestArgs> => {
            // verify required parameter 'putBasketItemRequest' is not null or undefined
            assertParamExists('putBasketItem', 'putBasketItemRequest', putBasketItemRequest)
            const localVarPath = `/api/basket-items`;
            // use dummy base URL string because the URL constructor only accepts absolute URLs.
            const localVarUrlObj = new URL(localVarPath, DUMMY_BASE_URL);
            let baseOptions;
            if (configuration) {
                baseOptions = configuration.baseOptions;
            }

            const localVarRequestOptions = { method: 'PUT', ...baseOptions, ...options};
            const localVarHeaderParameter = {} as any;
            const localVarQueryParameter = {} as any;


    
            localVarHeaderParameter['Content-Type'] = 'application/json';

            setSearchParams(localVarUrlObj, localVarQueryParameter);
            let headersFromBaseOptions = baseOptions && baseOptions.headers ? baseOptions.headers : {};
            localVarRequestOptions.headers = {...localVarHeaderParameter, ...headersFromBaseOptions, ...options.headers};
            localVarRequestOptions.data = serializeDataIfNeeded(putBasketItemRequest, localVarRequestOptions, configuration)

            return {
                url: toPathString(localVarUrlObj),
                options: localVarRequestOptions,
            };
        },
    }
};

/**
 * BasketItemApi - functional programming interface
 * @export
 */
export const BasketItemApiFp = function(configuration?: Configuration) {
    const localVarAxiosParamCreator = BasketItemApiAxiosParamCreator(configuration)
    return {
        /**
         * 買い物かごから指定したカタログアイテム Id の商品を削除します.<br>catalogItemId には買い物かご内に存在するカタログアイテム Id を指定してください. カタログアイテム Id は 1 以上の整数です.0以下の値を指定したり、整数値ではない値を指定した場合 HTTP 400 を返却します. 買い物かご内に指定したカタログアイテムの商品が存在しない場合、 HTTP 404 を返却します.
         * @summary 買い物かごから指定したカタログアイテム Id の商品を削除します.
         * @param {number} catalogItemId 
         * @param {*} [options] Override http request option.
         * @throws {RequiredError}
         */
        async deleteBasketItemAsync(catalogItemId: number, options?: AxiosRequestConfig): Promise<(axios?: AxiosInstance, basePath?: string) => AxiosPromise<void>> {
            const localVarAxiosArgs = await localVarAxiosParamCreator.deleteBasketItemAsync(catalogItemId, options);
            return createRequestFunction(localVarAxiosArgs, globalAxios, BASE_PATH, configuration);
        },
        /**
         * 買い物かごアイテムの一覧を返却する.
         * @summary 買い物かごアイテムの一覧を取得する.
         * @param {*} [options] Override http request option.
         * @throws {RequiredError}
         */
        async getBasketItems(options?: AxiosRequestConfig): Promise<(axios?: AxiosInstance, basePath?: string) => AxiosPromise<BasketResponse>> {
            const localVarAxiosArgs = await localVarAxiosParamCreator.getBasketItems(options);
            return createRequestFunction(localVarAxiosArgs, globalAxios, BASE_PATH, configuration);
        },
        /**
         * 買い物かごに商品を追加します.<br>この API では、システムに登録されていないカタログアイテム Id を指定した場合 HTTP 400 を返却します.また買い物かごに追加していないカタログアイテムを指定した場合、その商品を買い物かごに追加します.すでに買い物かごに追加されているカタログアイテムを指定した場合、指定した数量、買い物かご内の数量を追加します.<br>買い物かご内のカタログアイテムの数量が 0 未満になるように減じることはできません. 計算の結果数量が 0 未満になる場合 HTTP 500 を返却します.
         * @summary 買い物かごに商品を追加します.
         * @param {PostBasketItemsRequest} postBasketItemsRequest 
         * @param {*} [options] Override http request option.
         * @throws {RequiredError}
         */
        async postBasketItem(postBasketItemsRequest: PostBasketItemsRequest, options?: AxiosRequestConfig): Promise<(axios?: AxiosInstance, basePath?: string) => AxiosPromise<void>> {
            const localVarAxiosArgs = await localVarAxiosParamCreator.postBasketItem(postBasketItemsRequest, options);
            return createRequestFunction(localVarAxiosArgs, globalAxios, BASE_PATH, configuration);
        },
        /**
         * 買い物かごアイテム内の数量を変更します. 買い物かご内に存在しないカタログアイテム ID は指定できません.<br>この API では、買い物かご内に存在する商品の数量を変更できます. 買い物かご内に存在しないカタログアイテム Id を指定すると HTTP 400 を返却します.<br>またシステムに登録されていないカタログアイテム Id を指定した場合も HTTP 400 を返却します.
         * @summary 買い物かごアイテム内の数量を変更します.
         * @param {Array<PutBasketItemRequest>} putBasketItemRequest 
         * @param {*} [options] Override http request option.
         * @throws {RequiredError}
         */
        async putBasketItem(putBasketItemRequest: Array<PutBasketItemRequest>, options?: AxiosRequestConfig): Promise<(axios?: AxiosInstance, basePath?: string) => AxiosPromise<void>> {
            const localVarAxiosArgs = await localVarAxiosParamCreator.putBasketItem(putBasketItemRequest, options);
            return createRequestFunction(localVarAxiosArgs, globalAxios, BASE_PATH, configuration);
        },
    }
};

/**
 * BasketItemApi - factory interface
 * @export
 */
export const BasketItemApiFactory = function (configuration?: Configuration, basePath?: string, axios?: AxiosInstance) {
    const localVarFp = BasketItemApiFp(configuration)
    return {
        /**
         * 買い物かごから指定したカタログアイテム Id の商品を削除します.<br>catalogItemId には買い物かご内に存在するカタログアイテム Id を指定してください. カタログアイテム Id は 1 以上の整数です.0以下の値を指定したり、整数値ではない値を指定した場合 HTTP 400 を返却します. 買い物かご内に指定したカタログアイテムの商品が存在しない場合、 HTTP 404 を返却します.
         * @summary 買い物かごから指定したカタログアイテム Id の商品を削除します.
         * @param {number} catalogItemId 
         * @param {*} [options] Override http request option.
         * @throws {RequiredError}
         */
        deleteBasketItemAsync(catalogItemId: number, options?: any): AxiosPromise<void> {
            return localVarFp.deleteBasketItemAsync(catalogItemId, options).then((request) => request(axios, basePath));
        },
        /**
         * 買い物かごアイテムの一覧を返却する.
         * @summary 買い物かごアイテムの一覧を取得する.
         * @param {*} [options] Override http request option.
         * @throws {RequiredError}
         */
        getBasketItems(options?: any): AxiosPromise<BasketResponse> {
            return localVarFp.getBasketItems(options).then((request) => request(axios, basePath));
        },
        /**
         * 買い物かごに商品を追加します.<br>この API では、システムに登録されていないカタログアイテム Id を指定した場合 HTTP 400 を返却します.また買い物かごに追加していないカタログアイテムを指定した場合、その商品を買い物かごに追加します.すでに買い物かごに追加されているカタログアイテムを指定した場合、指定した数量、買い物かご内の数量を追加します.<br>買い物かご内のカタログアイテムの数量が 0 未満になるように減じることはできません. 計算の結果数量が 0 未満になる場合 HTTP 500 を返却します.
         * @summary 買い物かごに商品を追加します.
         * @param {PostBasketItemsRequest} postBasketItemsRequest 
         * @param {*} [options] Override http request option.
         * @throws {RequiredError}
         */
        postBasketItem(postBasketItemsRequest: PostBasketItemsRequest, options?: any): AxiosPromise<void> {
            return localVarFp.postBasketItem(postBasketItemsRequest, options).then((request) => request(axios, basePath));
        },
        /**
         * 買い物かごアイテム内の数量を変更します. 買い物かご内に存在しないカタログアイテム ID は指定できません.<br>この API では、買い物かご内に存在する商品の数量を変更できます. 買い物かご内に存在しないカタログアイテム Id を指定すると HTTP 400 を返却します.<br>またシステムに登録されていないカタログアイテム Id を指定した場合も HTTP 400 を返却します.
         * @summary 買い物かごアイテム内の数量を変更します.
         * @param {Array<PutBasketItemRequest>} putBasketItemRequest 
         * @param {*} [options] Override http request option.
         * @throws {RequiredError}
         */
        putBasketItem(putBasketItemRequest: Array<PutBasketItemRequest>, options?: any): AxiosPromise<void> {
            return localVarFp.putBasketItem(putBasketItemRequest, options).then((request) => request(axios, basePath));
        },
    };
};

/**
 * BasketItemApi - object-oriented interface
 * @export
 * @class BasketItemApi
 * @extends {BaseAPI}
 */
export class BasketItemApi extends BaseAPI {
    /**
     * 買い物かごから指定したカタログアイテム Id の商品を削除します.<br>catalogItemId には買い物かご内に存在するカタログアイテム Id を指定してください. カタログアイテム Id は 1 以上の整数です.0以下の値を指定したり、整数値ではない値を指定した場合 HTTP 400 を返却します. 買い物かご内に指定したカタログアイテムの商品が存在しない場合、 HTTP 404 を返却します.
     * @summary 買い物かごから指定したカタログアイテム Id の商品を削除します.
     * @param {number} catalogItemId 
     * @param {*} [options] Override http request option.
     * @throws {RequiredError}
     * @memberof BasketItemApi
     */
    public deleteBasketItemAsync(catalogItemId: number, options?: AxiosRequestConfig) {
        return BasketItemApiFp(this.configuration).deleteBasketItemAsync(catalogItemId, options).then((request) => request(this.axios, this.basePath));
    }

    /**
     * 買い物かごアイテムの一覧を返却する.
     * @summary 買い物かごアイテムの一覧を取得する.
     * @param {*} [options] Override http request option.
     * @throws {RequiredError}
     * @memberof BasketItemApi
     */
    public getBasketItems(options?: AxiosRequestConfig) {
        return BasketItemApiFp(this.configuration).getBasketItems(options).then((request) => request(this.axios, this.basePath));
    }

    /**
     * 買い物かごに商品を追加します.<br>この API では、システムに登録されていないカタログアイテム Id を指定した場合 HTTP 400 を返却します.また買い物かごに追加していないカタログアイテムを指定した場合、その商品を買い物かごに追加します.すでに買い物かごに追加されているカタログアイテムを指定した場合、指定した数量、買い物かご内の数量を追加します.<br>買い物かご内のカタログアイテムの数量が 0 未満になるように減じることはできません. 計算の結果数量が 0 未満になる場合 HTTP 500 を返却します.
     * @summary 買い物かごに商品を追加します.
     * @param {PostBasketItemsRequest} postBasketItemsRequest 
     * @param {*} [options] Override http request option.
     * @throws {RequiredError}
     * @memberof BasketItemApi
     */
    public postBasketItem(postBasketItemsRequest: PostBasketItemsRequest, options?: AxiosRequestConfig) {
        return BasketItemApiFp(this.configuration).postBasketItem(postBasketItemsRequest, options).then((request) => request(this.axios, this.basePath));
    }

    /**
     * 買い物かごアイテム内の数量を変更します. 買い物かご内に存在しないカタログアイテム ID は指定できません.<br>この API では、買い物かご内に存在する商品の数量を変更できます. 買い物かご内に存在しないカタログアイテム Id を指定すると HTTP 400 を返却します.<br>またシステムに登録されていないカタログアイテム Id を指定した場合も HTTP 400 を返却します.
     * @summary 買い物かごアイテム内の数量を変更します.
     * @param {Array<PutBasketItemRequest>} putBasketItemRequest 
     * @param {*} [options] Override http request option.
     * @throws {RequiredError}
     * @memberof BasketItemApi
     */
    public putBasketItem(putBasketItemRequest: Array<PutBasketItemRequest>, options?: AxiosRequestConfig) {
        return BasketItemApiFp(this.configuration).putBasketItem(putBasketItemRequest, options).then((request) => request(this.axios, this.basePath));
    }
}
