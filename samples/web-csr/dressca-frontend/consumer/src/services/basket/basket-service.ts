import { useBasketStore } from '@/stores/basket/basket'

/**
 * 買い物かごの内容を取得し、ストアを更新します。
 * @returns Promise<void>
 */
export async function fetchBasket() {
  const basketStore = useBasketStore()
  await basketStore.fetch()
}

/**
 * 指定した陳列品を買い物かごに追加します。
 * 追加後は最新の状態を取得してストアを更新します。
 * @param itemId - 追加する陳列品の ID
 * @returns Promise<void>
 * @example
 * await addItemToBasket('display-item-id')
 */
export async function addItemToBasket(itemId: string) {
  const basketStore = useBasketStore()
  await basketStore.add(itemId)
  await basketStore.fetch()
}

/**
 * 買い物かご内の陳列品の数量を更新します。
 * 更新後は最新の状態を取得してストアを更新します。
 * @param displayItemId - 更新対象の陳列品 ID
 * @param newQuantity - 新しい数量
 * @returns Promise<void>
 * @example
 * await updateItemInBasket('display-item-id', 5)
 */
export async function updateItemInBasket(displayItemId: string, newQuantity: number) {
  const basketStore = useBasketStore()
  // 直前に追加された商品の表示を更新するためIDを削除
  basketStore.deleteAddedItemId()

  try {
    await basketStore.update(displayItemId, newQuantity)
  } finally {
    await basketStore.fetch()
  }
}

/**
 * 買い物かごから指定した陳列品を削除します。
 * 削除後は最新の状態を取得してストアを更新します。
 * @param displayItemId - 削除する陳列品 ID
 * @returns Promise<void>
 * @example
 * await removeItemFromBasket('display-item-id')
 */
export async function removeItemFromBasket(displayItemId: string) {
  const basketStore = useBasketStore()
  // 直前に追加された商品の表示を更新するためIDを削除
  basketStore.deleteAddedItemId()
  try {
    await basketStore.remove(displayItemId)
  } finally {
    await basketStore.fetch()
  }
}
