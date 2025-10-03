import type { OrderResponse, PostOrderRequest } from '@/generated/api-client'
import { ordersApi } from '@/api-client'

/**
 * 注文を新規作成します。
 * CheckoutView.vue から呼び出され、注文成功時には注文 ID を返します。
 * 返された注文 ID をもとに `ordering/done/:orderId` へ遷移することを想定しています。
 * @param fullName - 注文者の氏名
 * @param postalCode - 郵便番号
 * @param todofuken - 都道府県
 * @param shikuchoson - 市区町村
 * @param azanaAndOthers - 番地・建物名などその他住所
 * @returns 成功した注文の注文 ID
 * @example
 * const orderId = await postOrder(
 *   '山田 太郎',
 *   '1000001',
 *   '東京都',
 *   '千代田区千代田',
 *   '1-1-1 皇居前'
 * )
 * // → 生成された注文 ID を返す
 */
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
  const orderResponse = await ordersApi().postOrder(postOrderInput)
  const url = new URL(orderResponse.headers.location)
  return Number(url.pathname.split('/').pop())
}

/**
 * 注文 ID を指定して注文情報を取得します。
 * `ordering/done/:orderId` の onMounted() から呼び出されることを想定しています。
 * @param orderId - 取得対象の注文 ID
 * @returns 注文情報 (`OrderResponse`)
 * @example
 * const order = await getOrder(123)
 * console.log(order.fullName) // 注文者の名前
 */
export async function getOrder(orderId: number): Promise<OrderResponse> {
  const orderResultResponse = await ordersApi().getById(orderId)
  return orderResultResponse.data
}
