import { defineStore } from 'pinia'
import type {
  BasketResponse,
  PutBasketItemsRequest,
  PostBasketItemsRequest,
  BasketItemResponse,
} from '@/generated/api-client'
import { basketItemsApi } from '@/api-client'

/**
 * 買い物かごの状態および操作を管理するストアです。
 *
 * 買い物かご内の商品追加・更新・削除や、
 * サーバーからの買い物かご情報の取得を行います。
 *
 * API クライアント (`basketItemsApi`) を通してバックエンドと通信し、
 * 最新の買い物かごの状態を保持します。
 */
export const useBasketStore = defineStore('basket', {
  state: () => ({
    basket: {} as BasketResponse,
    addedItemId: undefined as number | undefined,
    deletedItemIds: [] as Array<number>,
  }),
  actions: {
    /**
     * 指定されたカタログアイテム ID のアイテムを買い物かごに追加します。
     * @param catalogItemId カタログアイテム ID 。
     * @returns 非同期処理の完了を表す Promise 。
     */
    async add(catalogItemId: number) {
      const params: PostBasketItemsRequest = {
        catalogItemId,
        addedQuantity: 1,
      }
      await basketItemsApi().postBasketItem(params)
      this.addedItemId = catalogItemId
    },
    /**
     * 指定されたカタログアイテム ID のアイテムの数量を指定した値に更新します。
     * @param catalogItemId - 更新対象のカタログアイテムの ID。
     * @param newQuantity - 新しい数量。
     * @returns 非同期処理の完了を表す Promise 。
     */
    async update(catalogItemId: number, newQuantity: number) {
      const params: PutBasketItemsRequest[] = [
        {
          catalogItemId,
          quantity: newQuantity,
        },
      ]
      await basketItemsApi().putBasketItems(params)
    },
    /**
     * 指定されたカタログアイテムID のアイテムを買い物かごから削除します。
     * @param catalogItemId - 削除するアイテムのカタログアイテム ID 。
     * @returns 非同期処理の完了を表す Promise 。
     */
    async remove(catalogItemId: number) {
      await basketItemsApi().deleteBasketItem(catalogItemId)
    },
    /**
     * サーバーから最新の買い物かご情報を取得します。
     * API レスポンスの内容を `basket` に格納し、
     * 同時に削除済みのカタログアイテム ID (`deletedItemIds`) を更新します。
     * @returns 非同期処理の完了を表す Promise 。
     */
    async fetch() {
      const response = await basketItemsApi().getBasketItems()
      this.basket = response.data
      this.deletedItemIds = response.data.deletedItemIds ?? []
    },
    /**
     * `addedItemId` の値をリセットします。
     */
    deleteAddedItemId() {
      this.addedItemId = undefined
    },
  },
  getters: {
    /**
     * 現在の買い物かご全体の情報を取得します。
     * @param state 状態。
     * @returns バスケットレスポンス (`BasketResponse`)
     */
    getBasket(state): BasketResponse {
      return state.basket
    },
    /**
     * 直近に買い物かごに追加されたカタログアイテムの情報を取得します。
     * @param state 状態。
     * @returns 追加された `BasketItemResponse` または `undefined` 。
     */
    getAddedItemId(state): number | undefined {
      return state.addedItemId
    },
    /**
     * 直近に買い物かごに追加されたカタログアイテムの情報を取得します。
     * @param state 状態。
     * @returns 追加された `BasketItemResponse` または `undefined`
     */
    getAddedItem(state): BasketItemResponse | undefined {
      return state.basket.basketItems?.find((item) => item.catalogItemId === state.addedItemId)
    },
    /**
     * 削除済み商品の ID 一覧を取得します。
     * @param state 状態。
     * @returns 削除されたカタログアイテム ID の配列 。
     */
    getDeletedItemIds(state): Array<number> {
      return state.deletedItemIds
    },
  },
})
