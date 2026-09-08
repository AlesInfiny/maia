/**
 * 通貨関連のユーティリティ関数を提供するヘルパーです。
 * 主に金額を日本円形式に変換する処理を扱います。
 * @returns 通貨フォーマット用の関数群
 */
export function currencyHelper() {
  /**
   * 数値を日本円（JPY）形式の文字列に変換します。
   * 値が `undefined` の場合は `"-"` を返します。
   * @param price - 金額を表す数値（未定義の場合はハイフンを返す）
   * @returns 日本円形式にフォーマットされた文字列
   * @example
   * toCurrencyJPY(123456) // "￥123,456"
   * toCurrencyJPY(undefined) // "-"
   */
  const toCurrencyJPY = (price: number | undefined): string => {
    if (typeof price === 'undefined') {
      return '-'
    }
    return price.toLocaleString('ja-JP', {
      style: 'currency',
      currency: 'JPY',
    })
  }
  return {
    toCurrencyJPY,
  }
}
