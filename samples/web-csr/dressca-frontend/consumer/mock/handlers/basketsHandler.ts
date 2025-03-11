import { HttpResponse, http } from 'msw';
import type {
  PostBasketItemsRequest,
  PutBasketItemsRequest,
} from '@/generated/api-client';
import { HttpStatusCode } from 'axios';
import { basket, basketItems } from '../data/basketItems';

function calcBasketAccount() {
  let totalItemsPrice = 0;
  basket.basketItems?.forEach((item) => {
    // eslint-disable-next-line no-param-reassign
    item.subTotal = item.unitPrice * item.quantity;
    totalItemsPrice += item.subTotal;
  });
  if (!basket || !basket.account) {
    return;
  }
  basket.account.consumptionTaxRate = 0.1;
  basket.account.totalItemsPrice = totalItemsPrice;
  const deliveryCharge = totalItemsPrice >= 5000 ? 0 : 500;
  basket.account.deliveryCharge = deliveryCharge;
  const consumptionTax = Math.floor((totalItemsPrice + deliveryCharge) * 0.1);
  basket.account.consumptionTax = consumptionTax;
  basket.account.totalPrice = totalItemsPrice + consumptionTax + deliveryCharge;
}

export const basketsHandlers = [
  http.get('/api/basket-items', () => {
    return HttpResponse.json(basket, {
      status: HttpStatusCode.Ok,
    });
  }),
  http.post<PostBasketItemsRequest, never, never>(
    '/api/basket-items',
    async ({ request }) => {
      const dto: PostBasketItemsRequest = await request.json();
      const target = basket.basketItems?.filter(
        (item) => item.catalogItemId === dto.catalogItemId,
      );
      if (target) {
        if (target && target.length === 0) {
          const addBasketItem = basketItems.find(
            (item) => item.catalogItemId === dto.catalogItemId,
          );
          if (typeof addBasketItem !== 'undefined') {
            addBasketItem.quantity = dto.addedQuantity ?? 0;
            basket.basketItems?.push(addBasketItem);
          }
        } else {
          target[0].quantity += dto.addedQuantity ?? 0;
        }
      }
      calcBasketAccount();
      return new HttpResponse(null, { status: HttpStatusCode.Created });
    },
  ),
  http.put<PutBasketItemsRequest, never, never>(
    '/api/basket-items',
    async ({ request }) => {
      const dto: PutBasketItemsRequest = await request.json();
      dto.forEach((putBasketItem) => {
        const target = basket.basketItems?.filter(
          (item) => item.catalogItemId === putBasketItem.catalogItemId,
        );
        if (target) {
          if (target.length === 0) {
            res.writeHead(400, { 'Content-Type': 'application/json' });
            res.end();
          } else {
            target[0].quantity = putBasketItem.quantity;
          }
        }
      });
      calcBasketAccount();

      return new HttpResponse(null, { status: HttpStatusCode.NoContent });
    },
  ),
  http.delete('/api/basket-items/:catalogItemId', async () => {
    return new HttpResponse(null, { status: HttpStatusCode.NoContent });
  }),
];
