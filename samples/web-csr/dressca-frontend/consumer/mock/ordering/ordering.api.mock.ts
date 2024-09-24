import type { Connect } from 'vite';
import type {
  OrderResponse,
  PostOrderRequest,
} from '../../src/generated/api-client';

const base = 'api';

// mock のため、注文データはidとorderDate以外固定値を返却する
const order: OrderResponse = {
  id: 0,
  buyerId: 'xxxxxxxxxxxxxxxxxxxxxxxxxx',
  orderDate: '',
  fullName: '国会　太郎',
  postalCode: '100-8924',
  todofuken: '東京都',
  shikuchoson: '千代田区',
  azanaAndOthers: '永田町1-10-1',
  orderItems: [
    {
      id: 100,
      quantity: 2,
      subTotal: 3960,
      unitPrice: 1980,
      itemOrdered: {
        id: 1,
        name: 'クルーネック Tシャツ - ブラック',
        productCode: 'C000000001',
        assetCodes: ['45c22ba3da064391baac91341067ffe9'],
      },
    },
    {
      id: 101,
      quantity: 1,
      subTotal: 2800,
      unitPrice: 2800,
      itemOrdered: {
        id: 4,
        name: '無地 ボタンダウンシャツ',
        productCode: 'C000000004',
        assetCodes: [],
      },
    },
  ],
  account: {
    consumptionTaxRate: 0.1,
    totalItemsPrice: 6760,
    deliveryCharge: 0,
    consumptionTax: 676,
    totalPrice: 7436,
  },
};

export const orderingApiMock = (middlewares: Connect.Server) => {
  middlewares.use(`/${base}/orders`, (req, res) => {
    if (req.method === 'GET') {
      const orderId = Number(req.url?.substring(1));
      order.id = orderId;
      order.orderDate = new Date().toISOString();
      res.writeHead(200, { 'Content-Type': 'application/json' });
      res.write(JSON.stringify(order));
      res.end();
      return;
    }

    let body = '';
    req.on('data', (chunk) => {
      body += chunk;
    });
    req.on('end', () => {
      if (req.method === 'POST') {
        const dto: PostOrderRequest = JSON.parse(body);
        order.fullName = dto.fullName;
        order.postalCode = dto.postalCode;
        order.todofuken = dto.todofuken;
        order.shikuchoson = dto.shikuchoson;
        order.azanaAndOthers = dto.azanaAndOthers;
        const id = Math.floor(Math.random() * 1000) + 1;
        res.writeHead(201, {
          'Content-Type': 'application/json',
          Location: `${req.headers.origin}${req.originalUrl}/${id}`,
        });
        res.end();
      }
    });
  });
};
