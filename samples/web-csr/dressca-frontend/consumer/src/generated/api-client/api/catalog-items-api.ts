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
// @ts-ignore
import type { PagedListOfCatalogItemResponse } from '../models';
// @ts-ignore
import type { ProblemDetail } from '../models';
/**
 * CatalogItemsApi - axios parameter creator
 * @export
 */
export const CatalogItemsApiAxiosParamCreator = function (configuration?: Configuration) {
    return {
        /**
         * カタログアイテムを検索して返します。
         * @summary カタログアイテムを検索して返します。
         * @param {number} [brandId] 
         * @param {number} [categoryId] 
         * @param {number} [page] 
         * @param {number} [pageSize] 
         * @param {*} [options] Override http request option.
         * @throws {RequiredError}
         */
        getByQuery: async (brandId?: number, categoryId?: number, page?: number, pageSize?: number, options: RawAxiosRequestConfig = {}): Promise<RequestArgs> => {
            const localVarPath = `/api/catalog-items`;
            // use dummy base URL string because the URL constructor only accepts absolute URLs.
            const localVarUrlObj = new URL(localVarPath, DUMMY_BASE_URL);
            let baseOptions;
            if (configuration) {
                baseOptions = configuration.baseOptions;
            }

            const localVarRequestOptions = { method: 'GET', ...baseOptions, ...options};
            const localVarHeaderParameter = {} as any;
            const localVarQueryParameter = {} as any;

            if (brandId !== undefined) {
                localVarQueryParameter['brandId'] = brandId;
            }

            if (categoryId !== undefined) {
                localVarQueryParameter['categoryId'] = categoryId;
            }

            if (page !== undefined) {
                localVarQueryParameter['page'] = page;
            }

            if (pageSize !== undefined) {
                localVarQueryParameter['pageSize'] = pageSize;
            }


    
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
 * CatalogItemsApi - functional programming interface
 * @export
 */
export const CatalogItemsApiFp = function(configuration?: Configuration) {
    const localVarAxiosParamCreator = CatalogItemsApiAxiosParamCreator(configuration)
    return {
        /**
         * カタログアイテムを検索して返します。
         * @summary カタログアイテムを検索して返します。
         * @param {number} [brandId] 
         * @param {number} [categoryId] 
         * @param {number} [page] 
         * @param {number} [pageSize] 
         * @param {*} [options] Override http request option.
         * @throws {RequiredError}
         */
        async getByQuery(brandId?: number, categoryId?: number, page?: number, pageSize?: number, options?: RawAxiosRequestConfig): Promise<(axios?: AxiosInstance, basePath?: string) => AxiosPromise<PagedListOfCatalogItemResponse>> {
            const localVarAxiosArgs = await localVarAxiosParamCreator.getByQuery(brandId, categoryId, page, pageSize, options);
            const localVarOperationServerIndex = configuration?.serverIndex ?? 0;
            const localVarOperationServerBasePath = operationServerMap['CatalogItemsApi.getByQuery']?.[localVarOperationServerIndex]?.url;
            return (axios, basePath) => createRequestFunction(localVarAxiosArgs, globalAxios, BASE_PATH, configuration)(axios, localVarOperationServerBasePath || basePath);
        },
    }
};

/**
 * CatalogItemsApi - factory interface
 * @export
 */
export const CatalogItemsApiFactory = function (configuration?: Configuration, basePath?: string, axios?: AxiosInstance) {
    const localVarFp = CatalogItemsApiFp(configuration)
    return {
        /**
         * カタログアイテムを検索して返します。
         * @summary カタログアイテムを検索して返します。
         * @param {number} [brandId] 
         * @param {number} [categoryId] 
         * @param {number} [page] 
         * @param {number} [pageSize] 
         * @param {*} [options] Override http request option.
         * @throws {RequiredError}
         */
        getByQuery(brandId?: number, categoryId?: number, page?: number, pageSize?: number, options?: RawAxiosRequestConfig): AxiosPromise<PagedListOfCatalogItemResponse> {
            return localVarFp.getByQuery(brandId, categoryId, page, pageSize, options).then((request) => request(axios, basePath));
        },
    };
};

/**
 * CatalogItemsApi - object-oriented interface
 * @export
 * @class CatalogItemsApi
 * @extends {BaseAPI}
 */
export class CatalogItemsApi extends BaseAPI {
    /**
     * カタログアイテムを検索して返します。
     * @summary カタログアイテムを検索して返します。
     * @param {number} [brandId] 
     * @param {number} [categoryId] 
     * @param {number} [page] 
     * @param {number} [pageSize] 
     * @param {*} [options] Override http request option.
     * @throws {RequiredError}
     * @memberof CatalogItemsApi
     */
    public getByQuery(brandId?: number, categoryId?: number, page?: number, pageSize?: number, options?: RawAxiosRequestConfig) {
        return CatalogItemsApiFp(this.configuration).getByQuery(brandId, categoryId, page, pageSize, options).then((request) => request(this.axios, this.basePath));
    }
}

