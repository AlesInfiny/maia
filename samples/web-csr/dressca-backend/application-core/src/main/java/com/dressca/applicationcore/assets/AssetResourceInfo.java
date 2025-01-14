package com.dressca.applicationcore.assets;

import org.springframework.core.io.Resource;
import lombok.Data;
import lombok.NonNull;

/**
 * アセット情報とそのリソースの情報です。
 */
@Data
public class AssetResourceInfo {
  @NonNull
  private Asset asset;
  @NonNull
  private Resource resource;
}
