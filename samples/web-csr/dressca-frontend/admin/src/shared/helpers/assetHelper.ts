/**
 * アセット関連のユーティリティ関数群を提供するヘルパーです。
 * 画像やファイルのアセットコードから URL を生成したり、
 * 配列の最初のアセットコードを取得する機能を持ちます。
 * @returns アセット操作用の関数群
 */
export function assetHelper() {
  /**
   * アセットコードからアセット URL を生成します。
   * 空文字の場合は VITE の環境変数から代替用の NO_ASSET_URL を返します。
   * @param assetCode - アセットを識別するコード
   * @returns アセットの URL
   */
  const getAssetUrl = (assetCode: string): string => {
    if (assetCode === '') {
      return `${import.meta.env.VITE_NO_ASSET_URL}`
    }

    return `${import.meta.env.VITE_ASSET_URL}${assetCode}`
  }

  /**
   * アセットコード配列の最初の要素を取得します。
   * 配列が未定義または空の場合は空文字を返します。
   * @param assetCodes - アセットコードの配列
   * @returns 最初のアセットコード、または空文字
   */
  function getFirstItem(assetCodes: string[] | undefined): string {
    if (typeof assetCodes === 'undefined' || assetCodes == null || assetCodes.length === 0) {
      return ''
    }

    return assetCodes[0]
  }

  /**
   * アセットコード配列の最初の要素からアセット URL を生成します。
   * 配列が未定義または空の場合は NO_ASSET_URL を返します。
   * @param assetCodes - アセットコードの配列
   * @returns 最初のアセットの URL
   */
  const getFirstAssetUrl = (assetCodes: string[] | undefined): string => {
    const firstItem = getFirstItem(assetCodes)
    return getAssetUrl(firstItem)
  }

  return {
    getFirstAssetUrl,
    getAssetUrl,
  }
}
