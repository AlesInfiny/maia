package com.dressca.applicationcore.assets;

import java.util.Locale;
import java.util.UUID;
import org.springframework.context.MessageSource;
import com.dressca.applicationcore.constant.ExceptionIdConstants;
import com.dressca.systemcommon.util.ApplicationContextWrapper;
import com.dressca.systemcommon.util.UuidGenerator;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

/**
 * アセットのエンティティです。
 */
@Data
@NoArgsConstructor
public class Asset {

  private UUID id;
  @NonNull
  private String assetCode;
  @NonNull
  private String assetType;

  /**
   * {@link Asset} クラスのインスタンスを初期化します。
   * 
   * @param assetCode アセットコード。
   * @param assetType アセットタイプ。
   */
  public Asset(@NonNull String assetCode, @NonNull String assetType) {
    this.id = UuidGenerator.generate();
    this.assetCode = assetCode;
    if (!AssetTypes.isSupportedAssetTypes(assetType)) {
      MessageSource messages =
          (MessageSource) ApplicationContextWrapper.getBean(MessageSource.class);
      String message = messages.getMessage(ExceptionIdConstants.E_ASSET_TYPE_NOT_SUPPORTED,
          new String[] {assetType}, Locale.getDefault());

      throw new IllegalArgumentException(message);
    }
    this.assetType = assetType;
  }

  /**
   * アセットタイプをセットします。
   * 
   * @param assetType アセットタイプ。
   */
  public void setAssetType(String assetType) {
    if (!AssetTypes.isSupportedAssetTypes(assetType)) {
      MessageSource messages =
          (MessageSource) ApplicationContextWrapper.getBean(MessageSource.class);
      String message = messages.getMessage(ExceptionIdConstants.E_ASSET_TYPE_NOT_SUPPORTED,
          new String[] {assetType}, Locale.getDefault());

      throw new IllegalArgumentException(message);
    }
    this.assetType = assetType;
  }
}
