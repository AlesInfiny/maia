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

/**
 * 注文のお届け先です。
 * 利用者の住所を管理する API がないため、固定の値を使用します。
 */
export const deliveryAddress: Readonly<Address> = {
  fullName: '国会　太郎',
  postalCode: '100-8924',
  todofuken: '東京都',
  shikuchoson: '千代田区',
  azanaAndOthers: '永田町1-10-1',
}
