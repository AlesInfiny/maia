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
 * セール対象掲載品の情報を表します。
 *
 * 掲載品 ID と関連アセットコードを保持します。
 */
export interface SaleItem {
  displayItemId: number
  assetCode: string
}

/**
 * 特別コンテンツ（キャンペーン情報またはセール対象アイテム）を表します。
 */
export type SpecialContent = Campaign | SaleItem
