import { defineStore } from 'pinia'
import type {
  GetBasketItemsResponse,
  PutBasketItemsRequest,
  PostBasketItemsRequest,
  BasketItemApiModel,
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
    basket: {} as GetBasketItemsResponse,
    addedItemId: undefined as string | undefined,
    deletedItemIds: [] as Array<string>,
  }),
  actions: {
    /**
     * 指定された陳列品 ID のアイテムを買い物かごに追加します。
     * @param displayItemId 陳列品 ID 。
     * @returns 非同期処理の完了を表す Promise 。
     */
    async add(displayItemId: string) {
      const params: PostBasketItemsRequest = {
        displayItemId,
        addedQuantity: 1,
      }
      await basketItemsApi().postBasketItem(params)
      this.addedItemId = displayItemId
    },
    /**
     * 指定された陳列品 ID のアイテムの数量を指定した値に更新します。
     * @param displayItemId - 更新対象の陳列品 ID。
     * @param newQuantity - 新しい数量。
     * @returns 非同期処理の完了を表す Promise 。
     */
    async update(displayItemId: string, newQuantity: number) {
      const params: PutBasketItemsRequest[] = [
        {
          displayItemId,
          quantity: newQuantity,
        },
      ]
      await basketItemsApi().putBasketItems(params)
    },
    /**
     * 指定された陳列品 ID のアイテムを買い物かごから削除します。
     * @param displayItemId - 削除するアイテムの陳列品 ID 。
     * @returns 非同期処理の完了を表す Promise 。
     */
    async remove(displayItemId: string) {
      await basketItemsApi().deleteBasketItem(displayItemId)
    },
    /**
     * サーバーから最新の買い物かご情報を取得します。
     * API レスポンスの内容を `basket` に格納し、
     * 同時に削除済みの陳列品 ID (`deletedItemIds`) を更新します。
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
     * @returns バスケットレスポンス (`GetBasketItemsResponse`)
     */
    getBasket(state): GetBasketItemsResponse {
      return state.basket
    },
    /**
     * 直近に買い物かごに追加された陳列品の ID を取得します。
     * @param state 状態。
     * @returns 追加された陳列品 ID または `undefined` 。
     */
    getAddedItemId(state): string | undefined {
      return state.addedItemId
    },
    /**
     * 直近に買い物かごに追加された陳列品の情報を取得します。
     * @param state 状態。
     * @returns 追加された `BasketItemApiModel` または `undefined`
     */
    getAddedItem(state): BasketItemApiModel | undefined {
      return state.basket.basketItems?.find((item) => item.displayItemId === state.addedItemId)
    },
    /**
     * 削除済み商品の ID 一覧を取得します。
     * @param state 状態。
     * @returns 削除された陳列品 ID の配列 。
     */
    getDeletedItemIds(state): Array<string> {
      return state.deletedItemIds
    },
  },
})
