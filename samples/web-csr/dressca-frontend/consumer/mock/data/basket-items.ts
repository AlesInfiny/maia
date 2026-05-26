import type { BasketItemApiModel, GetBasketItemsResponse } from '@/generated/api-client'
import { deletedItemId } from './catalog-items'

export const basket: GetBasketItemsResponse = {
  buyerId: '018f25d7-0000-7000-8000-000000000501',
  account: {
    consumptionTaxRate: 0.1,
    consumptionTax: 0.0,
    deliveryCharge: 500,
    totalItemsPrice: 0,
    totalPrice: 550,
  },
  basketItems: [],
  deletedItemIds: [],
}

export const basketItems: BasketItemApiModel[] = [
  {
    catalogItemId: '018f25d7-0000-7000-8000-000000000301',
    quantity: 0,
    unitPrice: 1980,
    catalogItem: {
      id: '018f25d7-0000-7000-8000-000000000301',
      name: 'クルーネック Tシャツ - ブラック',
      productCode: 'C000000001',
      assetCodes: [],
    },
    subTotal: 0,
  },
  {
    catalogItemId: '018f25d7-0000-7000-8000-000000000302',
    quantity: 0,
    unitPrice: 4800,
    catalogItem: {
      id: '018f25d7-0000-7000-8000-000000000302',
      name: '裏起毛 スキニーデニム',
      productCode: 'C000000002',
      assetCodes: ['4aed07c4ed5d45a5b97f11acedfbb601'],
    },
    subTotal: 0,
  },
  {
    catalogItemId: '018f25d7-0000-7000-8000-000000000303',
    quantity: 0,
    unitPrice: 49800,
    catalogItem: {
      id: '018f25d7-0000-7000-8000-000000000303',
      name: 'ウールコート',
      productCode: 'C000000003',
      assetCodes: ['082b37439ecc44919626ba00fc60ee85'],
    },
    subTotal: 0,
  },
  {
    catalogItemId: '018f25d7-0000-7000-8000-000000000304',
    quantity: 0,
    unitPrice: 2800,
    catalogItem: {
      id: '018f25d7-0000-7000-8000-000000000304',
      name: '無地 ボタンダウンシャツ',
      productCode: 'C000000004',
      assetCodes: ['f5f89954281747fa878129c29e1e0f83'],
    },
    subTotal: 0,
  },
  {
    catalogItemId: '018f25d7-0000-7000-8000-000000000305',
    quantity: 0,
    unitPrice: 18800,
    catalogItem: {
      id: '018f25d7-0000-7000-8000-000000000305',
      name: 'レザーハンドバッグ',
      productCode: 'B000000001',
      assetCodes: ['a8291ef2e8e14869a7048e272915f33c'],
    },
    subTotal: 0,
  },
  {
    catalogItemId: '018f25d7-0000-7000-8000-000000000306',
    quantity: 0,
    unitPrice: 38000,
    catalogItem: {
      id: '018f25d7-0000-7000-8000-000000000306',
      name: 'ショルダーバッグ',
      productCode: 'B000000002',
      assetCodes: ['66237018c769478a90037bd877f5fba1'],
    },
    subTotal: 0,
  },
  {
    catalogItemId: '018f25d7-0000-7000-8000-000000000307',
    quantity: 0,
    unitPrice: 24800,
    catalogItem: {
      id: '018f25d7-0000-7000-8000-000000000307',
      name: 'トートバッグ ポーチ付き',
      productCode: 'B000000003',
      assetCodes: ['d136d4c81b86478990984dcafbf08244'],
    },
    subTotal: 0,
  },
  {
    catalogItemId: '018f25d7-0000-7000-8000-000000000308',
    quantity: 0,
    unitPrice: 2800,
    catalogItem: {
      id: '018f25d7-0000-7000-8000-000000000308',
      name: 'ショルダーバッグ',
      productCode: 'B000000004',
      assetCodes: ['47183f32f6584d7fb661f9216e11318b'],
    },
    subTotal: 0,
  },
  {
    catalogItemId: '018f25d7-0000-7000-8000-000000000309',
    quantity: 0,
    unitPrice: 258000,
    catalogItem: {
      id: '018f25d7-0000-7000-8000-000000000309',
      name: 'レザー チェーンショルダーバッグ',
      productCode: 'B000000005',
      assetCodes: ['cf151206efd344e1b86854f4aa49fdef'],
    },
    subTotal: 0,
  },
  {
    catalogItemId: '018f25d7-0000-7000-8000-00000000030a',
    quantity: 0,
    unitPrice: 12800,
    catalogItem: {
      id: '018f25d7-0000-7000-8000-00000000030a',
      name: 'ランニングシューズ - ブルー',
      productCode: 'S000000001',
      assetCodes: ['ab2e78eb7fe3408aadbf1e17a9945a8c'],
    },
    subTotal: 0,
  },
  {
    catalogItemId: '018f25d7-0000-7000-8000-00000000030b',
    quantity: 0,
    unitPrice: 23800,
    catalogItem: {
      id: '018f25d7-0000-7000-8000-00000000030b',
      name: 'メダリオン ストレートチップ ドレスシューズ',
      productCode: 'S000000002',
      assetCodes: ['0e557e96bc054f10bc91c27405a83e85'],
    },
    subTotal: 0,
  },
  {
    catalogItemId: deletedItemId,
    quantity: 0,
    unitPrice: 1980,
    catalogItem: {
      id: deletedItemId,
      name: '削除済みアイテム',
      productCode: 'C999999999',
      assetCodes: ['05c0415d57b342e79a10cac9b9cb25a8'],
    },
    subTotal: 0,
  },
]
