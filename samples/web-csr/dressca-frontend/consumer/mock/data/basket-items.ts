import type { GetBasketItemsResponse, BasketItemApiModel } from '@/generated/api-client'
import { deletedItemId } from './display-items'

export const basket: GetBasketItemsResponse = {
  buyerId: 'xxxxxxxxxxxxxxxxxxxxxxxxxx',
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
    displayItemId: 1,
    quantity: 0,
    unitPrice: 1980,
    displayItemSummary: {
      id: 1,
      name: 'クルーネック Tシャツ - ブラック',
      productCode: 'C000000001',
      assetCodes: [],
    },
    subTotal: 0,
  },
  {
    displayItemId: 2,
    quantity: 0,
    unitPrice: 4800,
    displayItemSummary: {
      id: 2,
      name: '裏起毛 スキニーデニム',
      productCode: 'C000000002',
      assetCodes: ['4aed07c4ed5d45a5b97f11acedfbb601'],
    },
    subTotal: 0,
  },
  {
    displayItemId: 3,
    quantity: 0,
    unitPrice: 49800,
    displayItemSummary: {
      id: 3,
      name: 'ウールコート',
      productCode: 'C000000003',
      assetCodes: ['082b37439ecc44919626ba00fc60ee85'],
    },
    subTotal: 0,
  },
  {
    displayItemId: 4,
    quantity: 0,
    unitPrice: 2800,
    displayItemSummary: {
      id: 4,
      name: '無地 ボタンダウンシャツ',
      productCode: 'C000000004',
      assetCodes: ['f5f89954281747fa878129c29e1e0f83'],
    },
    subTotal: 0,
  },
  {
    displayItemId: 5,
    quantity: 0,
    unitPrice: 18800,
    displayItemSummary: {
      id: 5,
      name: 'レザーハンドバッグ',
      productCode: 'B000000001',
      assetCodes: ['a8291ef2e8e14869a7048e272915f33c'],
    },
    subTotal: 0,
  },
  {
    displayItemId: 6,
    quantity: 0,
    unitPrice: 38000,
    displayItemSummary: {
      id: 6,
      name: 'ショルダーバッグ',
      productCode: 'B000000002',
      assetCodes: ['66237018c769478a90037bd877f5fba1'],
    },
    subTotal: 0,
  },
  {
    displayItemId: 7,
    quantity: 0,
    unitPrice: 24800,
    displayItemSummary: {
      id: 7,
      name: 'トートバッグ ポーチ付き',
      productCode: 'B000000003',
      assetCodes: ['d136d4c81b86478990984dcafbf08244'],
    },
    subTotal: 0,
  },
  {
    displayItemId: 8,
    quantity: 0,
    unitPrice: 2800,
    displayItemSummary: {
      id: 8,
      name: 'ショルダーバッグ',
      productCode: 'B000000004',
      assetCodes: ['47183f32f6584d7fb661f9216e11318b'],
    },
    subTotal: 0,
  },
  {
    displayItemId: 9,
    quantity: 0,
    unitPrice: 258000,
    displayItemSummary: {
      id: 9,
      name: 'レザー チェーンショルダーバッグ',
      productCode: 'B000000005',
      assetCodes: ['cf151206efd344e1b86854f4aa49fdef'],
    },
    subTotal: 0,
  },
  {
    displayItemId: 10,
    quantity: 0,
    unitPrice: 12800,
    displayItemSummary: {
      id: 10,
      name: 'ランニングシューズ - ブルー',
      productCode: 'S000000001',
      assetCodes: ['ab2e78eb7fe3408aadbf1e17a9945a8c'],
    },
    subTotal: 0,
  },
  {
    displayItemId: 11,
    quantity: 0,
    unitPrice: 23800,
    displayItemSummary: {
      id: 11,
      name: 'メダリオン ストレートチップ ドレスシューズ',
      productCode: 'S000000002',
      assetCodes: ['0e557e96bc054f10bc91c27405a83e85'],
    },
    subTotal: 0,
  },
  {
    displayItemId: deletedItemId,
    quantity: 0,
    unitPrice: 1980,
    displayItemSummary: {
      id: deletedItemId,
      name: '削除済みアイテム',
      productCode: 'C999999999',
      assetCodes: ['05c0415d57b342e79a10cac9b9cb25a8'],
    },
    subTotal: 0,
  },
]
