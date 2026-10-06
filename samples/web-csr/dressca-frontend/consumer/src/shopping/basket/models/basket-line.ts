import type {
  BasketItemApiModel,
  GetBasketItemsResponse,
} from '@/system-common/generated/api-client'

/**
 * 買い物かごに入っている陳列品の 1 行です。
 */
export interface BasketLine {
  displayItemId: string
  name: string
  assetCodes: string[] | undefined
  unitPrice: number
  quantity: number
  subTotal: number
  /** 購入できるかどうかです。カタログから削除された陳列品は購入できません。 */
  available: boolean
}

/**
 * 買い物かごの会計です。
 */
export interface BasketAccount {
  totalItemsPrice: number
  deliveryCharge: number
  consumptionTax: number
  totalPrice: number
}

/**
 * API のレスポンスを買い物かごの行に変換します。
 * @param item 買い物かごのアイテムのレスポンス。
 * @param deletedItemIds カタログから削除された陳列品の ID 。
 * @returns 買い物かごの行。
 */
function toBasketLine(item: BasketItemApiModel, deletedItemIds: string[]): BasketLine {
  return {
    displayItemId: item.displayItemId,
    name: item.displayItem?.name ?? '',
    assetCodes: item.displayItem?.assetCodes,
    unitPrice: item.unitPrice,
    quantity: item.quantity,
    subTotal: item.subTotal,
    available: !deletedItemIds.includes(item.displayItemId),
  }
}

/**
 * API のレスポンスを買い物かごの行の一覧に変換します。
 * @param basket 買い物かごのレスポンス。
 * @param deletedItemIds カタログから削除された陳列品の ID 。
 * @returns 買い物かごの行の一覧。
 */
export function toBasketLines(
  basket: GetBasketItemsResponse,
  deletedItemIds: string[],
): BasketLine[] {
  return (basket.basketItems ?? []).map((item) => toBasketLine(item, deletedItemIds))
}

/**
 * API のレスポンスを買い物かごの会計に変換します。
 * @param basket 買い物かごのレスポンス。
 * @returns 買い物かごの会計。読み込みが完了していない場合は undefined 。
 */
export function toBasketAccount(basket: GetBasketItemsResponse): BasketAccount | undefined {
  if (!basket.account) {
    return undefined
  }
  const { totalItemsPrice, deliveryCharge, consumptionTax, totalPrice } = basket.account
  return { totalItemsPrice, deliveryCharge, consumptionTax, totalPrice }
}
