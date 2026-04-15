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
 * カタログアイテム ID と関連アセットコードを保持します。
 */
export interface SaleItem {
  catalogItemId: number
  assetCode: string
}

/**
 * 特別コンテンツ（キャンペーン情報またはセール対象アイテム）を表します。
 */
export type SpecialContent = Campaign | SaleItem
