/**
 * ユーザーの住所情報を表すインターフェースです。
 *
 * 氏名・郵便番号・都道府県・市区町村・番地など、
 * 配送や請求先情報に使用される基本的な住所データを保持します。
 */
export interface Address {
  fullName: string
  postalCode: string
  todofuken: string
  shikuchoson: string
  azanaAndOthers: string
}
