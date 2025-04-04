import { HttpResponse, http } from 'msw';
import { HttpStatusCode } from 'axios';
import type {
  PostBasketItemsRequest,
  PutBasketItemsRequest,
} from '@/generated/api-client';
import { basket, basketItems } from '../data/basket-items';

function calcBasketAccount() {
  if (!basket || !basket.account) {
    return;
  }
  // undefined になる場合は 0 を代入
  const totalItemsPrice = basket.basketItems?.length
    ? basket.basketItems
        .map((item) => {
          // eslint-disable-next-line no-param-reassign
          item.subTotal = item.unitPrice * item.quantity;
          return item.subTotal;
        })
        .reduce((total, subTotal) => total + subTotal, 0)
    : 0;
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
  http.post<never, PostBasketItemsRequest, never>(
    '/api/basket-items',
    async ({ request }) => {
      const dto: PostBasketItemsRequest = await request.json();

      const target = basket.basketItems?.filter(
        (item) => item.catalogItemId === Number(dto.catalogItemId),
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
  http.put<never, PutBasketItemsRequest[], never>(
    '/api/basket-items',
    async ({ request }) => {
      const dto: PutBasketItemsRequest[] = await request.json();
      let response = new HttpResponse(null, {
        status: HttpStatusCode.NoContent,
      });
      dto.forEach((putBasketItem) => {
        const target = basket.basketItems?.filter(
          (item) => item.catalogItemId === putBasketItem.catalogItemId,
        );
        if (target) {
          if (target.length === 0) {
            response = new HttpResponse(null, {
              status: HttpStatusCode.BadRequest,
            });
          }
          target[0].quantity = putBasketItem.quantity;
        }
      });
      calcBasketAccount();

      return response;
    },
  ),
  http.delete('/api/basket-items/:catalogItemId', async ({ params }) => {
    const { catalogItemId } = params;
    basket.basketItems = basket.basketItems?.filter(
      (item) => item.catalogItemId !== Number(catalogItemId),
    );
    calcBasketAccount();
    return new HttpResponse(null, { status: HttpStatusCode.NoContent });
  }),
];
