/* tslint:disable */
/* eslint-disable */
/**
 * OpenAPI definition
 * No description provided (generated by Openapi Generator https://github.com/openapitools/openapi-generator)
 *
 * The version of the OpenAPI document: v0
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
import { PagedCatalogItemDto } from '../models';
/**
 * CatalogItemsControllerApi - axios parameter creator
 * @export
 */
export const CatalogItemsControllerApiAxiosParamCreator = function (configuration?: Configuration) {
    return {
        /**
         * 
         * @param {number} [brandId] 
         * @param {number} [categoryId] 
         * @param {number} [page] 
         * @param {number} [pageSize] 
         * @param {*} [options] Override http request option.
         * @throws {RequiredError}
         */
        getByQuery: async (brandId?: number, categoryId?: number, page?: number, pageSize?: number, options: AxiosRequestConfig = {}): Promise<RequestArgs> => {
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
 * CatalogItemsControllerApi - functional programming interface
 * @export
 */
export const CatalogItemsControllerApiFp = function(configuration?: Configuration) {
    const localVarAxiosParamCreator = CatalogItemsControllerApiAxiosParamCreator(configuration)
    return {
        /**
         * 
         * @param {number} [brandId] 
         * @param {number} [categoryId] 
         * @param {number} [page] 
         * @param {number} [pageSize] 
         * @param {*} [options] Override http request option.
         * @throws {RequiredError}
         */
        async getByQuery(brandId?: number, categoryId?: number, page?: number, pageSize?: number, options?: AxiosRequestConfig): Promise<(axios?: AxiosInstance, basePath?: string) => AxiosPromise<PagedCatalogItemDto>> {
            const localVarAxiosArgs = await localVarAxiosParamCreator.getByQuery(brandId, categoryId, page, pageSize, options);
            return createRequestFunction(localVarAxiosArgs, globalAxios, BASE_PATH, configuration);
        },
    }
};

/**
 * CatalogItemsControllerApi - factory interface
 * @export
 */
export const CatalogItemsControllerApiFactory = function (configuration?: Configuration, basePath?: string, axios?: AxiosInstance) {
    const localVarFp = CatalogItemsControllerApiFp(configuration)
    return {
        /**
         * 
         * @param {number} [brandId] 
         * @param {number} [categoryId] 
         * @param {number} [page] 
         * @param {number} [pageSize] 
         * @param {*} [options] Override http request option.
         * @throws {RequiredError}
         */
        getByQuery(brandId?: number, categoryId?: number, page?: number, pageSize?: number, options?: any): AxiosPromise<PagedCatalogItemDto> {
            return localVarFp.getByQuery(brandId, categoryId, page, pageSize, options).then((request) => request(axios, basePath));
        },
    };
};

/**
 * CatalogItemsControllerApi - object-oriented interface
 * @export
 * @class CatalogItemsControllerApi
 * @extends {BaseAPI}
 */
export class CatalogItemsControllerApi extends BaseAPI {
    /**
     * 
     * @param {number} [brandId] 
     * @param {number} [categoryId] 
     * @param {number} [page] 
     * @param {number} [pageSize] 
     * @param {*} [options] Override http request option.
     * @throws {RequiredError}
     * @memberof CatalogItemsControllerApi
     */
    public getByQuery(brandId?: number, categoryId?: number, page?: number, pageSize?: number, options?: AxiosRequestConfig) {
        return CatalogItemsControllerApiFp(this.configuration).getByQuery(brandId, categoryId, page, pageSize, options).then((request) => request(this.axios, this.basePath));
    }
}
