import { HttpResponse, http } from 'msw'
import { HttpStatusCode } from 'axios'
import type {
  AccountResponse,
  BasketItemResponse,
  PostBasketItemsRequest,
  PutBasketItemsRequest,
} from '@/generated/api-client'
import { deletedItemId } from '../data/catalog-items'
import { basket, basketItems } from '../data/basket-items'

/**
 * 買い物かごアイテムのリストからアイテム毎の小計金額を計算します。
 * @param originalBasketItems 買い物かごアイテムのリスト。
 * @returns 小計金額が計算済みの買い物かごアイテムのリスト。
 */
function calcBasketItemsSubTotal(originalBasketItems: BasketItemResponse[]): BasketItemResponse[] {
  if (!originalBasketItems) {
    return originalBasketItems
  }
  return originalBasketItems.map(({ catalogItem, catalogItemId, quantity, unitPrice }) => ({
    catalogItem,
    catalogItemId,
    quantity,
    unitPrice,
    subTotal: unitPrice * quantity,
  }))
}

/**
 * 小計金額から買い物かごの会計情報を計算します。
 * @param subTotals 小計金額のリスト。
 * @returns 買い物かごの会計情報。
 */
function calcBasketAccount(subTotals: number[]): AccountResponse {
  const totalItemsPrice = subTotals.reduce((total, subTotal) => total + subTotal, 0)
  const consumptionTaxRate = 0.1
  const deliveryCharge = totalItemsPrice >= 5000 ? 0 : 500
  const consumptionTax = Math.floor((totalItemsPrice + deliveryCharge) * consumptionTaxRate)
  const totalPrice = totalItemsPrice + consumptionTax + deliveryCharge
  return {
    consumptionTax,
    consumptionTaxRate,
    deliveryCharge,
    totalItemsPrice,
    totalPrice,
  }
}

export const basketsHandlers = [
  http.get('/api/basket-items', () => {
    return HttpResponse.json(basket, {
      status: HttpStatusCode.Ok,
    })
  }),
  http.post<never, PostBasketItemsRequest, never>('/api/basket-items', async ({ request }) => {
    const dto: PostBasketItemsRequest = await request.json()

    const target = basket.basketItems?.filter(
      (item) => item.catalogItemId === Number(dto.catalogItemId),
    )
    if (target) {
      if (target.length === 0) {
        const addBasketItem = basketItems.find((item) => item.catalogItemId === dto.catalogItemId)
        if (typeof addBasketItem !== 'undefined') {
          addBasketItem.quantity = dto.addedQuantity ?? 0
          basket.basketItems?.push(addBasketItem)

          // 追加したアイテムがカタログから削除済みのアイテムだった場合、
          // 買い物かごの削除済みアイテム ID リストに ID を追加します。
          if (addBasketItem.catalogItemId === deletedItemId) {
            basket.deletedItemIds?.push(addBasketItem.catalogItemId)
          }
        }
      } else {
        target[0].quantity += dto.addedQuantity ?? 0
      }
    }
    basket.basketItems = calcBasketItemsSubTotal(basket.basketItems ?? [])
    const subTotals = basket.basketItems.map((item) => item.subTotal)
    basket.account = calcBasketAccount(subTotals)
    return new HttpResponse(null, { status: HttpStatusCode.Created })
  }),
  http.put<never, PutBasketItemsRequest[], never>('/api/basket-items', async ({ request }) => {
    const dto: PutBasketItemsRequest[] = await request.json()
    let response = new HttpResponse(null, {
      status: HttpStatusCode.NoContent,
    })
    dto.forEach((putBasketItem) => {
      const target = basket.basketItems?.filter(
        (item) => item.catalogItemId === putBasketItem.catalogItemId,
      )
      if (target) {
        if (target.length === 0) {
          response = new HttpResponse(null, {
            status: HttpStatusCode.BadRequest,
          })
        }
        target[0].quantity = putBasketItem.quantity
      }
    })
    basket.basketItems = calcBasketItemsSubTotal(basket.basketItems ?? [])
    const subTotals = basket.basketItems.map((item) => item.subTotal)
    basket.account = calcBasketAccount(subTotals)
    return response
  }),
  http.delete('/api/basket-items/:catalogItemId', ({ params }) => {
    const { catalogItemId } = params
    basket.basketItems = basket.basketItems?.filter(
      (item) => item.catalogItemId !== Number(catalogItemId),
    )
    basket.basketItems = calcBasketItemsSubTotal(basket.basketItems ?? [])
    const subTotals = basket.basketItems.map((item) => item.subTotal)
    basket.account = calcBasketAccount(subTotals)
    // 削除したアイテムがカタログから削除済みのアイテムだった場合、
    // 買い物かごの削除済みアイテム ID リストを空にします。
    if (Number(catalogItemId) === deletedItemId) {
      basket.deletedItemIds = []
    }
    return new HttpResponse(null, { status: HttpStatusCode.NoContent })
  }),
]
