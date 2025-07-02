/** キャンペーン */
export interface Campaign {
  campaignCode: string
  assetCode: string
}

/** セール品 */
export interface SaleItem {
  catalogItemId: number
  assetCode: string
}

export type SpecialContent = Campaign | SaleItem
