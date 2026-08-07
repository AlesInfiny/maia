package com.dressca.domainmodules.assetsmanagement.dto;

import lombok.Data;
import lombok.NonNull;
import org.springframework.core.io.Resource;
import com.dressca.domainmodules.assetsmanagement.model.Asset;

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
