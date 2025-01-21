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


import type { Configuration } from '../configuration';
import type { AxiosPromise, AxiosInstance, RawAxiosRequestConfig } from 'axios';
import globalAxios from 'axios';
// Some imports not used depending on template conditions
// @ts-ignore
import { DUMMY_BASE_URL, assertParamExists, setApiKeyToObject, setBasicAuthToObject, setBearerAuthToObject, setOAuthToObject, setSearchParams, serializeDataIfNeeded, toPathString, createRequestFunction } from '../common';
// @ts-ignore
import { BASE_PATH, COLLECTION_FORMATS, type RequestArgs, BaseAPI, RequiredError, operationServerMap } from '../base';
/**
 * AssetsApi - axios parameter creator
 * @export
 */
export const AssetsApiAxiosParamCreator = function (configuration?: Configuration) {
    return {
        /**
         * 与えられたアセットコードに対応するアセットを返却します。
         * @summary アセットを取得します。
         * @param {string} assetCode アセットコード
         * @param {*} [options] Override http request option.
         * @throws {RequiredError}
         */
        get: async (assetCode: string, options: RawAxiosRequestConfig = {}): Promise<RequestArgs> => {
            // verify required parameter 'assetCode' is not null or undefined
            assertParamExists('get', 'assetCode', assetCode)
            const localVarPath = `/api/assets/{assetCode}`
                .replace(`{${"assetCode"}}`, encodeURIComponent(String(assetCode)));
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
    }
};

/**
 * AssetsApi - functional programming interface
 * @export
 */
export const AssetsApiFp = function(configuration?: Configuration) {
    const localVarAxiosParamCreator = AssetsApiAxiosParamCreator(configuration)
    return {
        /**
         * 与えられたアセットコードに対応するアセットを返却します。
         * @summary アセットを取得します。
         * @param {string} assetCode アセットコード
         * @param {*} [options] Override http request option.
         * @throws {RequiredError}
         */
        async get(assetCode: string, options?: RawAxiosRequestConfig): Promise<(axios?: AxiosInstance, basePath?: string) => AxiosPromise<File>> {
            const localVarAxiosArgs = await localVarAxiosParamCreator.get(assetCode, options);
            const localVarOperationServerIndex = configuration?.serverIndex ?? 0;
            const localVarOperationServerBasePath = operationServerMap['AssetsApi.get']?.[localVarOperationServerIndex]?.url;
            return (axios, basePath) => createRequestFunction(localVarAxiosArgs, globalAxios, BASE_PATH, configuration)(axios, localVarOperationServerBasePath || basePath);
        },
    }
};

/**
 * AssetsApi - factory interface
 * @export
 */
export const AssetsApiFactory = function (configuration?: Configuration, basePath?: string, axios?: AxiosInstance) {
    const localVarFp = AssetsApiFp(configuration)
    return {
        /**
         * 与えられたアセットコードに対応するアセットを返却します。
         * @summary アセットを取得します。
         * @param {string} assetCode アセットコード
         * @param {*} [options] Override http request option.
         * @throws {RequiredError}
         */
        get(assetCode: string, options?: RawAxiosRequestConfig): AxiosPromise<File> {
            return localVarFp.get(assetCode, options).then((request) => request(axios, basePath));
        },
    };
};

/**
 * AssetsApi - object-oriented interface
 * @export
 * @class AssetsApi
 * @extends {BaseAPI}
 */
export class AssetsApi extends BaseAPI {
    /**
     * 与えられたアセットコードに対応するアセットを返却します。
     * @summary アセットを取得します。
     * @param {string} assetCode アセットコード
     * @param {*} [options] Override http request option.
     * @throws {RequiredError}
     * @memberof AssetsApi
     */
    public get(assetCode: string, options?: RawAxiosRequestConfig) {
        return AssetsApiFp(this.configuration).get(assetCode, options).then((request) => request(this.axios, this.basePath));
    }
}

