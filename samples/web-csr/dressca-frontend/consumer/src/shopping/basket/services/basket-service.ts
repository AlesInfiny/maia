import type { GetBasketItemsResponse } from '@/system-common/generated/api-client'
import { basketItemsApi } from '@/system-common/api-client'

/**
 * 買い物かごの内容を取得します。
 * @returns 買い物かごの内容。
 */
export async function fetchBasket(): Promise<GetBasketItemsResponse> {
  const response = await basketItemsApi().getBasketItems()
  return response.data
}

/**
 * 指定した陳列品を 1 個、買い物かごに追加します。
 * @param displayItemId 追加する陳列品の ID 。
 * @example
 * await addItemToBasket('display-item-id')
 */
export async function addItemToBasket(displayItemId: string): Promise<void> {
  await basketItemsApi().postBasketItem({ displayItemId, addedQuantity: 1 })
}

/**
 * 買い物かご内の陳列品の数量を更新します。
 * @param displayItemId 更新する陳列品の ID 。
 * @param quantity 更新後の数量。
 * @example
 * await updateItemInBasket('display-item-id', 5)
 */
export async function updateItemInBasket(displayItemId: string, quantity: number): Promise<void> {
  await basketItemsApi().putBasketItems([{ displayItemId, quantity }])
}

/**
 * 買い物かごから指定した陳列品を削除します。
 * @param displayItemId 削除する陳列品の ID 。
 * @example
 * await removeItemFromBasket('display-item-id')
 */
export async function removeItemFromBasket(displayItemId: string): Promise<void> {
  await basketItemsApi().deleteBasketItem(displayItemId)
}
