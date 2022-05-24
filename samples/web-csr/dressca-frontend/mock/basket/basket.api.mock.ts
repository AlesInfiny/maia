const base = 'api';
import type { BasketDto } from '../../src/api-client/models/basket-dto';
import type { BasketItemDto } from '../../src/api-client/models/basket-item-dto';
import type { PostBasketItemsInputDto } from '../../src/api-client/models/post-basket-items-input-dto';
import type { PutBasketItemsInputDto } from '../../src/api-client/models/put-basket-items-input-dto';

const basket: BasketDto = {
  buyerId: 'xxxxxxxxxxxxxxxxxxxxxxxxxx',
  account: {
    consumptionTaxRate: 0.1,
    consumptionTax: 0.0,
    deliveryCharge: 500,
    totalItemsPrice: 0,
    totalPrice: 550,
  },
  basketItems: [],
};

const mockBasketItems: BasketItemDto[] = [
  {
    catalogItemId: 1,
    quantity: 0,
    unitPrice: 1980,
    catalogItem: {
      id: 1,
      name: 'クルーネック Tシャツ - ブラック',
      productCode: 'C000000001',
      assetCodes: [],
    },
    subTotal: 0,
  },
  {
    catalogItemId: 2,
    quantity: 0,
    unitPrice: 4800,
    catalogItem: {
      id: 2,
      name: '裏起毛 スキニーデニム',
      productCode: 'C000000002',
      assetCodes: ['4aed07c4ed5d45a5b97f11acedfbb601'],
    },
    subTotal: 0,
  },
  {
    catalogItemId: 3,
    quantity: 0,
    unitPrice: 49800,
    catalogItem: {
      id: 3,
      name: 'ウールコート',
      productCode: 'C000000003',
      assetCodes: ['082b37439ecc44919626ba00fc60ee85'],
    },
    subTotal: 0,
  },
  {
    catalogItemId: 4,
    quantity: 0,
    unitPrice: 2800,
    catalogItem: {
      id: 4,
      name: '無地 ボタンダウンシャツ',
      productCode: 'C000000004',
      assetCodes: ['f5f89954281747fa878129c29e1e0f83'],
    },
    subTotal: 0,
  },
  {
    catalogItemId: 5,
    quantity: 0,
    unitPrice: 18800,
    catalogItem: {
      id: 5,
      name: 'レザーハンドバッグ',
      productCode: 'B000000001',
      assetCodes: ['a8291ef2e8e14869a7048e272915f33c'],
    },
    subTotal: 0,
  },
  {
    catalogItemId: 6,
    quantity: 0,
    unitPrice: 38000,
    catalogItem: {
      id: 6,
      name: 'ショルダーバッグ',
      productCode: 'B000000002',
      assetCodes: ['66237018c769478a90037bd877f5fba1'],
    },
    subTotal: 0,
  },
  {
    catalogItemId: 7,
    quantity: 0,
    unitPrice: 24800,
    catalogItem: {
      id: 7,
      name: 'トートバッグ ポーチ付き',
      productCode: 'B000000003',
      assetCodes: ['d136d4c81b86478990984dcafbf08244'],
    },
    subTotal: 0,
  },
  {
    catalogItemId: 8,
    quantity: 0,
    unitPrice: 2800,
    catalogItem: {
      id: 8,
      name: 'ショルダーバッグ',
      productCode: 'B000000004',
      assetCodes: ['47183f32f6584d7fb661f9216e11318b'],
    },
    subTotal: 0,
  },
  {
    catalogItemId: 9,
    quantity: 0,
    unitPrice: 258000,
    catalogItem: {
      id: 9,
      name: 'レザー チェーンショルダーバッグ',
      productCode: 'B000000005',
      assetCodes: ['cf151206efd344e1b86854f4aa49fdef'],
    },
    subTotal: 0,
  },
  {
    catalogItemId: 10,
    quantity: 0,
    unitPrice: 12800,
    catalogItem: {
      id: 10,
      name: 'ランニングシューズ - ブルー',
      productCode: 'S000000001',
      assetCodes: ['ab2e78eb7fe3408aadbf1e17a9945a8c'],
    },
    subTotal: 0,
  },
  {
    catalogItemId: 11,
    quantity: 0,
    unitPrice: 23800,
    catalogItem: {
      id: 11,
      name: 'メダリオン ストレートチップ ドレスシューズ',
      productCode: 'S000000002',
      assetCodes: ['0e557e96bc054f10bc91c27405a83e85'],
    },
    subTotal: 0,
  },
];

export const basketApiMock = (middlewares) => {
  middlewares.use(`/${base}/basket-items`, (_, res) => {
    if (_.method === 'GET') {
      res.writeHead(200, { 'Content-Type': 'application/json' });
      res.write(JSON.stringify(basket));
      res.end();
      return;
    }

    if (_.method === 'DELETE') {
      const catalogItemId = Number(_.url.substring(1));
      res.writeHead(204, { 'Content-Type': 'application/json' });
      basket.basketItems = basket.basketItems?.filter(
        (item) => item.catalogItemId !== catalogItemId,
      );

      calcBasketAccount();
      res.end();
      return;
    }

    let body = '';
    _.on('data', (chunk) => (body += chunk));
    _.on('end', () => {
      if (_.method === 'POST') {
        const dto: PostBasketItemsInputDto = JSON.parse(body);
        const target = basket.basketItems?.filter(
          (item) => item.catalogItemId === dto.catalogItemId,
        );
        if (target.length === 0) {
          const addBasketItem = mockBasketItems.find(
            (item) => item.catalogItemId === dto.catalogItemId,
          );
          if (typeof addBasketItem !== 'undefined') {
            addBasketItem.quantity = dto.addedQuantity;
            basket.basketItems?.push(addBasketItem);
          }
        } else {
          target[0].quantity += dto.addedQuantity;
        }
        calcBasketAccount();
        res.writeHead(201, { 'Content-Type': 'application/json' });
        res.end();
        return;
      }

      if (_.method === 'PUT') {
        const dto: PutBasketItemsInputDto[] = JSON.parse(body);
        dto.forEach((putBasketItem) => {
          const target = basket.basketItems?.filter(
            (item) => item.catalogItemId === putBasketItem.catalogItemId,
          );
          if (target.length === 0) {
            res.writeHead(400, { 'Content-Type': 'application/json' });
            res.end();
            return;
          } else {
            target[0].quantity = putBasketItem.quantity;
          }
        });

        calcBasketAccount();
        res.writeHead(204, { 'Content-Type': 'application/json' });
        res.end();
        return;
      }
    });
  });
};

function calcBasketAccount() {
  let totalItemsPrice = 0;
  basket.basketItems?.forEach((item) => {
    item.subTotal = item.unitPrice * item.quantity;
    totalItemsPrice += item.subTotal;
  });
  basket.account.consumptionTaxRate = 0.1;
  basket.account.totalItemsPrice = totalItemsPrice;
  const deliveryCharge = totalItemsPrice >= 5000 ? 0 : 500;
  basket.account.deliveryCharge = deliveryCharge;
  const consumptionTax = Math.floor((totalItemsPrice + deliveryCharge) * 0.1);
  basket.account.consumptionTax = consumptionTax;
  basket.account.totalPrice = totalItemsPrice + consumptionTax + deliveryCharge;
}
