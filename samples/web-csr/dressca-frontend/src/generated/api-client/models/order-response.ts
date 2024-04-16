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
import { AccountResponse } from './account-response';
// May contain unused imports in some cases
// @ts-ignore
import { OrderItemResponse } from './order-item-response';

/**
 * 
 * @export
 * @interface OrderResponse
 */
export interface OrderResponse {
    /**
     * 
     * @type {AccountResponse}
     * @memberof OrderResponse
     */
    'account'?: AccountResponse;
    /**
     * 
     * @type {string}
     * @memberof OrderResponse
     */
    'azanaAndOthers': string;
    /**
     * 
     * @type {string}
     * @memberof OrderResponse
     */
    'buyerId': string;
    /**
     * 
     * @type {string}
     * @memberof OrderResponse
     */
    'fullName': string;
    /**
     * 
     * @type {number}
     * @memberof OrderResponse
     */
    'id': number;
    /**
     * 
     * @type {string}
     * @memberof OrderResponse
     */
    'orderDate': string;
    /**
     * 
     * @type {Array<OrderItemResponse>}
     * @memberof OrderResponse
     */
    'orderItems'?: Array<OrderItemResponse>;
    /**
     * 
     * @type {string}
     * @memberof OrderResponse
     */
    'postalCode': string;
    /**
     * 
     * @type {string}
     * @memberof OrderResponse
     */
    'shikuchoson': string;
    /**
     * 
     * @type {string}
     * @memberof OrderResponse
     */
    'todofuken': string;
}

