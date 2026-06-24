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
 * 指定した掲載品を買い物かごに追加します。
 * 追加後は最新の状態を取得してストアを更新します。
 * @param itemId - 追加する掲載品の ID
 * @returns Promise<void>
 * @example
 * await addItemToBasket(123)
 */
export async function addItemToBasket(itemId: number) {
  const basketStore = useBasketStore()
  await basketStore.add(itemId)
  await basketStore.fetch()
}

/**
 * 買い物かご内の掲載品の数量を更新します。
 * 更新後は最新の状態を取得してストアを更新します。
 * @param displayItemId - 更新対象の掲載品 ID
 * @param newQuantity - 新しい数量
 * @returns Promise<void>
 * @example
 * await updateItemInBasket(123, 5)
 */
export async function updateItemInBasket(displayItemId: number, newQuantity: number) {
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
 * 買い物かごから指定した掲載品を削除します。
 * 削除後は最新の状態を取得してストアを更新します。
 * @param displayItemId - 削除する掲載品 ID
 * @returns Promise<void>
 * @example
 * await removeItemFromBasket(123)
 */
export async function removeItemFromBasket(displayItemId: number) {
  const basketStore = useBasketStore()
  // 直前に追加された商品の表示を更新するためIDを削除
  basketStore.deleteAddedItemId()
  try {
    await basketStore.remove(displayItemId)
  } finally {
    await basketStore.fetch()
  }
}
