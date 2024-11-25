/* tslint:disable */
/* eslint-disable */
/**
 * Dressca Web API
 * Dressca の Web API 仕様
 *
 * The version of the OpenAPI document: 1.0.0
 * 
 *
 * NOTE: This class is auto generated by OpenAPI Generator (https://openapi-generator.tech).
 * https://openapi-generator.tech
 * Do not edit the class manually.
 */



/**
 * カタログにアイテムを追加する処理のリクエストデータを表します。
 * @export
 * @interface PostCatalogItemRequest
 */
export interface PostCatalogItemRequest {
    /**
     * アイテム名を取得または設定します。             
     * @type {string}
     * @memberof PostCatalogItemRequest
     */
    'name': string;
    /**
     * 説明を取得または設定します。             
     * @type {string}
     * @memberof PostCatalogItemRequest
     */
    'description': string;
    /**
     * 単価を取得または設定します。
     * @type {number}
     * @memberof PostCatalogItemRequest
     */
    'price': number;
    /**
     * 商品コードを取得または設定します。             
     * @type {string}
     * @memberof PostCatalogItemRequest
     */
    'productCode': string;
    /**
     * カタログカテゴリIDを取得または設定します。             
     * @type {number}
     * @memberof PostCatalogItemRequest
     */
    'catalogCategoryId': number;
    /**
     * カタログブランドIDを取得または設定します。             
     * @type {number}
     * @memberof PostCatalogItemRequest
     */
    'catalogBrandId': number;
}
