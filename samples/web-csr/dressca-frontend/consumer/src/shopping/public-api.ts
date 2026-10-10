/**
 * shopping コンテキストの公開APIです。
 * pages や他コンテキストは、このモジュール経由でのみ shopping を参照します。
 */
export {
  useDisplayItemList,
  type AddToBasketOutcome,
  type DisplayItemFilter,
  type DisplayItemFilterOption,
  type DisplayItemList,
  type DisplayItemListStatus,
  type DisplayItemSummary,
} from './display-item/composables/use-display-item-list'
export { default as CarouselSlider } from './display-item/components/CarouselSlider.vue'

export {
  useBasket,
  type AddedBasketItem,
  type Basket,
  type BasketStatus,
  type ChangeQuantityOutcome,
  type ProceedToCheckoutOutcome,
  type RemoveFromBasketOutcome,
} from './basket/composables/use-basket'
export type { BasketAccount, BasketLine } from './basket/models/basket-line'
export { default as BasketItem } from './basket/components/BasketItem.vue'

export {
  useCheckout,
  type Checkout,
  type CheckoutStatus,
  type PlaceOrderOutcome,
} from './ordering/composables/use-checkout'
export {
  useOrderResult,
  type OrderedItem,
  type OrderResult,
  type OrderResultStatus,
  type OrderResultView,
} from './ordering/composables/use-order-result'
export type { Address } from './ordering/stores/user.model'
