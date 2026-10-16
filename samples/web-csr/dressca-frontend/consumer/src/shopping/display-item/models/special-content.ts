/**
 * キャンペーン情報を表します。
 *
 * アイテムに紐づくキャンペーンコードやアセットコードを保持します。
 */
export interface Campaign {
  campaignCode: string
  assetCode: string
}

/**
 * セール対象アイテムの情報を表します。
 *
 * 陳列品 ID と関連アセットコードを保持します。
 */
export interface SaleItem {
  displayItemId: string
  assetCode: string
}

/**
 * 特別コンテンツ（キャンペーン情報またはセール対象アイテム）を表します。
 */
export type SpecialContent = Campaign | SaleItem

export const specialContents = [
  {
    campaignCode: 'LTOX48Q',
    assetCode: 'b52dc7f712d94ca5812dd995bf926c04',
  },
  {
    campaignCode: 'EKHQGBB',
    assetCode: '05d38fad5693422c8a27dd5b14070ec8',
  },
  {
    productCode: 14,
    assetCode: '80bc8e167ccb4543b2f9d51913073492',
  },
] as SpecialContent[]
