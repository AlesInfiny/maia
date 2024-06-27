import { useBasketStore } from '@/stores/basket/basket';

const basketStore = useBasketStore();

export async function fetchBasket() {
  await basketStore.fetch();
}

export async function addItemToBasket(itemId: number) {
  await basketStore.add(itemId);
  await basketStore.fetch();
}

export async function updateItemInBasket(
  catalogItemId: number,
  newQuantity: number,
) {
  // 直前に追加された商品の表示を更新するためIDを削除
  basketStore.deleteAddedItemId();

  await basketStore.update(catalogItemId, newQuantity);
  await basketStore.fetch();
}

export async function removeItemFromBasket(catalogItemId: number) {
  // 直前に追加された商品の表示を更新するためIDを削除
  basketStore.deleteAddedItemId();

  await basketStore.remove(catalogItemId);
  await basketStore.fetch();
}
