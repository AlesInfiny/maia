package com.dressca.boundedcontexts.assetsmanagement.dto;

import com.dressca.boundedcontexts.assetsmanagement.entity.Asset;
import lombok.Data;
import lombok.NonNull;
import org.springframework.core.io.Resource;

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
