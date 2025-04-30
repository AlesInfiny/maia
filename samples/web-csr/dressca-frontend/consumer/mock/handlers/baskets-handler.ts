import { HttpResponse, http } from 'msw';
import { HttpStatusCode } from 'axios';
import type {
  BasketItemResponse,
  BasketResponse,
  PostBasketItemsRequest,
  PutBasketItemsRequest,
} from '@/generated/api-client';
import { basket, basketItems } from '../data/basket-items';

let currentBasket = basket;

function calcBasketAccount(originalBasket: BasketResponse): BasketResponse {
  if (!originalBasket?.account || !originalBasket?.basketItems) {
    return originalBasket;
  }
  const basketItemsCalculatedSubTotal = originalBasket.basketItems.map(
    ({ catalogItem, catalogItemId, quantity, unitPrice }) => ({
      catalogItem,
      catalogItemId,
      quantity,
      unitPrice,
      subTotal: unitPrice * quantity,
    }),
  ) as Array<BasketItemResponse>;
  const totalItemsPrice = basketItemsCalculatedSubTotal.length
    ? basketItemsCalculatedSubTotal
        .map((item) => item.subTotal)
        .reduce((total, subTotal) => total + subTotal, 0)
    : 0;
  const consumptionTaxRate = 0.1;
  const deliveryCharge = totalItemsPrice >= 5000 ? 0 : 500;
  const consumptionTax = Math.floor(
    (totalItemsPrice + deliveryCharge) * consumptionTaxRate,
  );
  const totalPrice = totalItemsPrice + consumptionTax + deliveryCharge;

  return {
    ...originalBasket,
    basketItems: basketItemsCalculatedSubTotal,
    account: {
      ...originalBasket.account,
      consumptionTaxRate,
      totalItemsPrice,
      deliveryCharge,
      consumptionTax,
      totalPrice,
    },
  } as BasketResponse;
}

export const basketsHandlers = [
  http.get('/api/basket-items', () => {
    return HttpResponse.json(currentBasket, {
      status: HttpStatusCode.Ok,
    });
  }),
  http.post<never, PostBasketItemsRequest, never>(
    '/api/basket-items',
    async ({ request }) => {
      const dto: PostBasketItemsRequest = await request.json();

      const target = currentBasket.basketItems?.filter(
        (item) => item.catalogItemId === Number(dto.catalogItemId),
      );
      if (target) {
        if (target.length === 0) {
          const addBasketItem = basketItems.find(
            (item) => item.catalogItemId === dto.catalogItemId,
          );
          if (typeof addBasketItem !== 'undefined') {
            addBasketItem.quantity = dto.addedQuantity ?? 0;
            currentBasket.basketItems?.push(addBasketItem);
          }
        } else {
          target[0].quantity += dto.addedQuantity ?? 0;
        }
      }
      currentBasket = calcBasketAccount(currentBasket);
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
        const target = currentBasket.basketItems?.filter(
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
      currentBasket = calcBasketAccount(currentBasket);
      return response;
    },
  ),
  http.delete('/api/basket-items/:catalogItemId', async ({ params }) => {
    const { catalogItemId } = params;
    currentBasket.basketItems = currentBasket.basketItems?.filter(
      (item) => item.catalogItemId !== Number(catalogItemId),
    );
    currentBasket = calcBasketAccount(currentBasket);
    return new HttpResponse(null, { status: HttpStatusCode.NoContent });
  }),
];
