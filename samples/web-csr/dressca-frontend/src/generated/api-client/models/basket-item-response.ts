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


// May contain unused imports in some cases
// @ts-ignore
import type { CatalogItemSummaryResponse } from './catalog-item-summary-response';

/**
 * 
 * @export
 * @interface BasketItemResponse
 */
export interface BasketItemResponse {
    /**
     * 
     * @type {CatalogItemSummaryResponse}
     * @memberof BasketItemResponse
     */
    'catalogItem'?: CatalogItemSummaryResponse;
    /**
     * 
     * @type {number}
     * @memberof BasketItemResponse
     */
    'catalogItemId': number;
    /**
     * 
     * @type {number}
     * @memberof BasketItemResponse
     */
    'quantity': number;
    /**
     * 
     * @type {number}
     * @memberof BasketItemResponse
     */
    'subTotal': number;
    /**
     * 
     * @type {number}
     * @memberof BasketItemResponse
     */
    'unitPrice': number;
}

