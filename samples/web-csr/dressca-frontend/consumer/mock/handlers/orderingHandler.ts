import { HttpResponse, http } from 'msw';
import { HttpStatusCode } from 'axios';
import type { PostOrderRequest } from '@/generated/api-client';
import { order } from '../data/orders';

type GetOrderingParams = {
  orderId: string;
};

export const orderingHandlers = [
  http.post<never, PostOrderRequest, never>(
    '/api/orders',
    async ({ request }) => {
      const dto: PostOrderRequest = await request.json();
      order.fullName = dto.fullName;
      order.postalCode = dto.postalCode;
      order.todofuken = dto.todofuken;
      order.shikuchoson = dto.shikuchoson;
      order.azanaAndOthers = dto.azanaAndOthers;

      const id = Math.floor(Math.random() * 1000) + 1;

      return new HttpResponse(null, {
        headers: {
          Location: `http://localhost:5173/api/orders/${id}`,
        },
        status: HttpStatusCode.Created,
      });
    },
  ),
  http.get<GetOrderingParams, never, never>(
    '/api/orders/:orderId',
    async ({ params }) => {
      const { orderId } = params;
      order.id = Number(orderId);
      order.orderDate = new Date().toISOString();
      return HttpResponse.json(order, { status: HttpStatusCode.Ok });
    },
  ),
];
