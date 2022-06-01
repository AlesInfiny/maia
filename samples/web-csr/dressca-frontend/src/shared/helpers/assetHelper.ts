export default function () {
  const getFirstAssetUrl = (assetCodes: string[] | undefined): string => {
    const firstItem = getFirstItem(assetCodes);
    return getAssetUrl(firstItem);
  };

  const getAssetUrl = (assetCode: string): string => {
    if (assetCode === '') {
      return `${import.meta.env.VITE_NO_ASSET_URL}`;
    }

    return `${import.meta.env.VITE_ASSET_URL}${assetCode}`;
  };

  return {
    getFirstAssetUrl,
    getAssetUrl,
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
}
