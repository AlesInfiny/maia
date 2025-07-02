export function currencyHelper() {
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
