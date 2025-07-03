import { useBasketStore } from '@/stores/basket/basket'

export async function fetchBasket() {
  const basketStore = useBasketStore()
  await basketStore.fetch()
}

export async function addItemToBasket(itemId: number) {
  const basketStore = useBasketStore()
  await basketStore.add(itemId)
  await basketStore.fetch()
}

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
