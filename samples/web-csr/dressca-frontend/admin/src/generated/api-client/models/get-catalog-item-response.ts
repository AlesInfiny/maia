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



/**
 * 
 * @export
 * @interface GetCatalogItemResponse
 */
export interface GetCatalogItemResponse {
    /**
     * 
     * @type {Array<string>}
     * @memberof GetCatalogItemResponse
     */
    'assetCodes'?: Array<string>;
    /**
     * 
     * @type {number}
     * @memberof GetCatalogItemResponse
     */
    'catalogBrandId': number;
    /**
     * 
     * @type {number}
     * @memberof GetCatalogItemResponse
     */
    'catalogCategoryId': number;
    /**
     * 
     * @type {string}
     * @memberof GetCatalogItemResponse
     */
    'description': string;
    /**
     * 
     * @type {number}
     * @memberof GetCatalogItemResponse
     */
    'id': number;
    /**
     * 
     * @type {boolean}
     * @memberof GetCatalogItemResponse
     */
    'isDeleted': boolean;
    /**
     * 
     * @type {string}
     * @memberof GetCatalogItemResponse
     */
    'name': string;
    /**
     * 
     * @type {number}
     * @memberof GetCatalogItemResponse
     */
    'price': number;
    /**
     * 
     * @type {string}
     * @memberof GetCatalogItemResponse
     */
    'productCode': string;
    /**
     * 
     * @type {string}
     * @memberof GetCatalogItemResponse
     */
    'rowVersion': string;
}

