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
 * @interface PutCatalogItemRequest
 */
export interface PutCatalogItemRequest {
    /**
     * 
     * @type {number}
     * @memberof PutCatalogItemRequest
     */
    'catalogBrandId': number;
    /**
     * 
     * @type {number}
     * @memberof PutCatalogItemRequest
     */
    'catalogCategoryId': number;
    /**
     * 
     * @type {string}
     * @memberof PutCatalogItemRequest
     */
    'description': string;
    /**
     * 
     * @type {string}
     * @memberof PutCatalogItemRequest
     */
    'name': string;
    /**
     * 
     * @type {number}
     * @memberof PutCatalogItemRequest
     */
    'price': number;
    /**
     * 
     * @type {string}
     * @memberof PutCatalogItemRequest
     */
    'productCode': string;
    /**
     * 
     * @type {string}
     * @memberof PutCatalogItemRequest
     */
    'rowVersion': string;
}
