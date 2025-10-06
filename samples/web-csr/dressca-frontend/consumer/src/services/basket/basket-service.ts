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
 * 指定したカタログアイテムを買い物かごに追加します。
 * 追加後は最新の状態を取得してストアを更新します。
 * @param itemId - 追加するカタログアイテムの ID
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
 * 買い物かご内のカタログアイテムの数量を更新します。
 * 更新後は最新の状態を取得してストアを更新します。
 * @param catalogItemId - 更新対象のカタログアイテム ID
 * @param newQuantity - 新しい数量
 * @returns Promise<void>
 * @example
 * await updateItemInBasket(123, 5)
 */
export async function updateItemInBasket(catalogItemId: number, newQuantity: number) {
  const basketStore = useBasketStore()
  // 直前に追加された商品の表示を更新するためIDを削除
  basketStore.deleteAddedItemId()

  try {
    await basketStore.update(catalogItemId, newQuantity)
  } finally {
    await basketStore.fetch()
  }
}

/**
 * 買い物かごから指定したカタログアイテムを削除します。
 * 削除後は最新の状態を取得してストアを更新します。
 * @param catalogItemId - 削除するカタログアイテム ID
 * @returns Promise<void>
 * @example
 * await removeItemFromBasket(123)
 */
export async function removeItemFromBasket(catalogItemId: number) {
  const basketStore = useBasketStore()
  // 直前に追加された商品の表示を更新するためIDを削除
  basketStore.deleteAddedItemId()
  try {
    await basketStore.remove(catalogItemId)
  } finally {
    await basketStore.fetch()
  }
}
