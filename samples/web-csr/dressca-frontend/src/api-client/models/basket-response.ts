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


import { AccountResponse } from './account-response';
import { BasketItemResponse } from './basket-item-response';

/**
 * 
 * @export
 * @interface BasketResponse
 */
export interface BasketResponse {
    /**
     * 
     * @type {string}
     * @memberof BasketResponse
     */
    'buyerId'?: string;
    /**
     * 
     * @type {AccountResponse}
     * @memberof BasketResponse
     */
    'account'?: AccountResponse;
    /**
     * 
     * @type {Array<BasketItemResponse>}
     * @memberof BasketResponse
     */
    'basketItems'?: Array<BasketItemResponse>;
}

