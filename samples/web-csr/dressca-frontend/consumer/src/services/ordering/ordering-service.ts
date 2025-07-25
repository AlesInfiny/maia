import type { OrderResponse, PostOrderRequest } from '@/generated/api-client'
import { ordersApi } from '@/api-client'

// CheckoutView.vue から呼び出されて注文
// 注文成功したら注文IDを返す
// CheckoutView.vue が注文IDを受け取って ordering/done/:orderId に遷移
export async function postOrder(
  fullName: string,
  postalCode: string,
  todofuken: string,
  shikuchoson: string,
  azanaAndOthers: string,
): Promise<number> {
  const postOrderInput: PostOrderRequest = {
    fullName,
    postalCode,
    todofuken,
    shikuchoson,
    azanaAndOthers,
  }
  const orderResponse = await ordersApi.postOrder(postOrderInput)
  const url = new URL(orderResponse.headers.location)
  return Number(url.pathname.split('/').pop())
}

// ordering/done/:orderId の onMounted() から呼び出されて注文情報を取得
export async function getOrder(orderId: number): Promise<OrderResponse> {
  const orderResultResponse = await ordersApi.getById(orderId)
  return orderResultResponse.data
}
