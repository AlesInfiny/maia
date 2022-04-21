package com.dressca.infrastructure.store;

import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.Optional;

import com.dressca.applicationcore.assets.Asset;
import com.dressca.applicationcore.assets.AssetStore;

import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.Setter;

/**
 * 静的ファイルを取り扱うアセットのストアの実装です。
 */
@Component
@RequiredArgsConstructor
public class StaticFileAssetStore implements AssetStore {

  @Setter
  private String basePath = ".";

  @Override
  public Optional<Resource> getResource(Asset asset) {
    if (asset == null) {
      throw new IllegalArgumentException("assetが設定されていません");
    }

    FileSystemResource resource = new FileSystemResource(getFilePath(asset));
    if (resource.exists()) {
      return Optional.of(resource);
    } else {
      return Optional.empty();
    }
  }

  private Path getFilePath(Asset asset) {
    String imagePath = "images";
    return FileSystems.getDefault().getPath(basePath, imagePath, asset.getAssetCode());
  }
  
}
