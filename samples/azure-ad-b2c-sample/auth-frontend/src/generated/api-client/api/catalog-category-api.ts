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
import { CatalogCategory } from '../models';
/**
 * CatalogCategoryApi - axios parameter creator
 * @export
 */
export const CatalogCategoryApiAxiosParamCreator = function (configuration?: Configuration) {
    return {
        /**
         * カタログカテゴリの一覧を取得します.
         * @summary カタログカテゴリの一覧を取得します.
         * @param {*} [options] Override http request option.
         * @throws {RequiredError}
         */
        getCatalogCategories: async (options: AxiosRequestConfig = {}): Promise<RequestArgs> => {
            const localVarPath = `/api/catalog-categories`;
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
 * CatalogCategoryApi - functional programming interface
 * @export
 */
export const CatalogCategoryApiFp = function(configuration?: Configuration) {
    const localVarAxiosParamCreator = CatalogCategoryApiAxiosParamCreator(configuration)
    return {
        /**
         * カタログカテゴリの一覧を取得します.
         * @summary カタログカテゴリの一覧を取得します.
         * @param {*} [options] Override http request option.
         * @throws {RequiredError}
         */
        async getCatalogCategories(options?: AxiosRequestConfig): Promise<(axios?: AxiosInstance, basePath?: string) => AxiosPromise<Array<CatalogCategory>>> {
            const localVarAxiosArgs = await localVarAxiosParamCreator.getCatalogCategories(options);
            return createRequestFunction(localVarAxiosArgs, globalAxios, BASE_PATH, configuration);
        },
    }
};

/**
 * CatalogCategoryApi - factory interface
 * @export
 */
export const CatalogCategoryApiFactory = function (configuration?: Configuration, basePath?: string, axios?: AxiosInstance) {
    const localVarFp = CatalogCategoryApiFp(configuration)
    return {
        /**
         * カタログカテゴリの一覧を取得します.
         * @summary カタログカテゴリの一覧を取得します.
         * @param {*} [options] Override http request option.
         * @throws {RequiredError}
         */
        getCatalogCategories(options?: any): AxiosPromise<Array<CatalogCategory>> {
            return localVarFp.getCatalogCategories(options).then((request) => request(axios, basePath));
        },
    };
};

/**
 * CatalogCategoryApi - object-oriented interface
 * @export
 * @class CatalogCategoryApi
 * @extends {BaseAPI}
 */
export class CatalogCategoryApi extends BaseAPI {
    /**
     * カタログカテゴリの一覧を取得します.
     * @summary カタログカテゴリの一覧を取得します.
     * @param {*} [options] Override http request option.
     * @throws {RequiredError}
     * @memberof CatalogCategoryApi
     */
    public getCatalogCategories(options?: AxiosRequestConfig) {
        return CatalogCategoryApiFp(this.configuration).getCatalogCategories(options).then((request) => request(this.axios, this.basePath));
    }
}
