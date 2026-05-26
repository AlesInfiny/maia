import { HttpResponse, http } from 'msw'
import { HttpStatusCode } from 'axios'
import type { PostOrderRequest } from '@/generated/api-client'
import { order } from '../data/orders'

export const orderingHandlers = [
  http.post<never, PostOrderRequest, never>('/api/orders', async ({ request }) => {
    const dto: PostOrderRequest = await request.json()
    order.fullName = dto.fullName
    order.postalCode = dto.postalCode
    order.todofuken = dto.todofuken
    order.shikuchoson = dto.shikuchoson
    order.azanaAndOthers = dto.azanaAndOthers

    const id = '018f25d7-0000-7000-8000-0000000006ff'

    return new HttpResponse(null, {
      headers: {
        Location: `http://localhost:5173/api/orders/${id}`,
      },
      status: HttpStatusCode.Created,
    })
  }),
  http.get('/api/orders/:orderId', ({ params }) => {
    const { orderId } = params
    order.id = String(orderId)
    order.orderDate = new Date().toISOString()
    return HttpResponse.json(order, { status: HttpStatusCode.Ok })
  }),
]
