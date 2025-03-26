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
import type { Link } from '../models';
/**
 * ActuatorApi - axios parameter creator
 * @export
 */
export const ActuatorApiAxiosParamCreator = function (configuration?: Configuration) {
    return {
        /**
         * 
         * @summary Actuator web endpoint \'health\'
         * @param {*} [options] Override http request option.
         * @throws {RequiredError}
         */
        health: async (options: RawAxiosRequestConfig = {}): Promise<RequestArgs> => {
            const localVarPath = `/api/health`;
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
         * 
         * @summary Actuator root web endpoint
         * @param {*} [options] Override http request option.
         * @throws {RequiredError}
         */
        links: async (options: RawAxiosRequestConfig = {}): Promise<RequestArgs> => {
            const localVarPath = `/api`;
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
 * ActuatorApi - functional programming interface
 * @export
 */
export const ActuatorApiFp = function(configuration?: Configuration) {
    const localVarAxiosParamCreator = ActuatorApiAxiosParamCreator(configuration)
    return {
        /**
         * 
         * @summary Actuator web endpoint \'health\'
         * @param {*} [options] Override http request option.
         * @throws {RequiredError}
         */
        async health(options?: RawAxiosRequestConfig): Promise<(axios?: AxiosInstance, basePath?: string) => AxiosPromise<object>> {
            const localVarAxiosArgs = await localVarAxiosParamCreator.health(options);
            const localVarOperationServerIndex = configuration?.serverIndex ?? 0;
            const localVarOperationServerBasePath = operationServerMap['ActuatorApi.health']?.[localVarOperationServerIndex]?.url;
            return (axios, basePath) => createRequestFunction(localVarAxiosArgs, globalAxios, BASE_PATH, configuration)(axios, localVarOperationServerBasePath || basePath);
        },
        /**
         * 
         * @summary Actuator root web endpoint
         * @param {*} [options] Override http request option.
         * @throws {RequiredError}
         */
        async links(options?: RawAxiosRequestConfig): Promise<(axios?: AxiosInstance, basePath?: string) => AxiosPromise<{ [key: string]: { [key: string]: Link; }; }>> {
            const localVarAxiosArgs = await localVarAxiosParamCreator.links(options);
            const localVarOperationServerIndex = configuration?.serverIndex ?? 0;
            const localVarOperationServerBasePath = operationServerMap['ActuatorApi.links']?.[localVarOperationServerIndex]?.url;
            return (axios, basePath) => createRequestFunction(localVarAxiosArgs, globalAxios, BASE_PATH, configuration)(axios, localVarOperationServerBasePath || basePath);
        },
    }
};

/**
 * ActuatorApi - factory interface
 * @export
 */
export const ActuatorApiFactory = function (configuration?: Configuration, basePath?: string, axios?: AxiosInstance) {
    const localVarFp = ActuatorApiFp(configuration)
    return {
        /**
         * 
         * @summary Actuator web endpoint \'health\'
         * @param {*} [options] Override http request option.
         * @throws {RequiredError}
         */
        health(options?: RawAxiosRequestConfig): AxiosPromise<object> {
            return localVarFp.health(options).then((request) => request(axios, basePath));
        },
        /**
         * 
         * @summary Actuator root web endpoint
         * @param {*} [options] Override http request option.
         * @throws {RequiredError}
         */
        links(options?: RawAxiosRequestConfig): AxiosPromise<{ [key: string]: { [key: string]: Link; }; }> {
            return localVarFp.links(options).then((request) => request(axios, basePath));
        },
    };
};

/**
 * ActuatorApi - object-oriented interface
 * @export
 * @class ActuatorApi
 * @extends {BaseAPI}
 */
export class ActuatorApi extends BaseAPI {
    /**
     * 
     * @summary Actuator web endpoint \'health\'
     * @param {*} [options] Override http request option.
     * @throws {RequiredError}
     * @memberof ActuatorApi
     */
    public health(options?: RawAxiosRequestConfig) {
        return ActuatorApiFp(this.configuration).health(options).then((request) => request(this.axios, this.basePath));
    }

    /**
     * 
     * @summary Actuator root web endpoint
     * @param {*} [options] Override http request option.
     * @throws {RequiredError}
     * @memberof ActuatorApi
     */
    public links(options?: RawAxiosRequestConfig) {
        return ActuatorApiFp(this.configuration).links(options).then((request) => request(this.axios, this.basePath));
    }
}

