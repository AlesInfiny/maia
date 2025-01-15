export function assetHelper() {
  const getAssetUrl = (assetCode: string): string => {
    if (assetCode === '') {
      return `${import.meta.env.VITE_NO_ASSET_URL}`;
    }

    return `${import.meta.env.VITE_ASSET_URL}${assetCode}`;
  };

  function getFirstItem(assetCodes: string[] | undefined): string {
    if (
      typeof assetCodes === 'undefined' ||
      assetCodes == null ||
      assetCodes.length === 0
    ) {
      return '';
    }

    return assetCodes[0];
  }

  const getFirstAssetUrl = (assetCodes: string[] | undefined): string => {
    const firstItem = getFirstItem(assetCodes);
    return getAssetUrl(firstItem);
  };

  return {
    getFirstAssetUrl,
    getAssetUrl,
  };
}
