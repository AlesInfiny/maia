package com.dressca.applicationcore.assets;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
public class AssetApplicationServiceTest {

  @Mock
  private AssetRepository repository;
  @Mock
  private AssetStore store;
  @InjectMocks
  private AssetApplicationService service;

  @Test
  @DisplayName("testGetAssetResourceInfo_01_正常系_存在するアセットコード")
  void testGetAssetResourceInfo_01_正常系_存在するアセットコード() throws AssetNotFoundException {
    // テスト用の入力データ
    String assetCode = "ExistAssetCode";

    // 期待する戻り値
    Asset asset = new Asset(assetCode, "png");
    Resource resource = new FileSystemResource("test");
    AssetResourceInfo expected = new AssetResourceInfo(asset, resource);

    // モックの設定
    when(this.repository.findByAssetCode(assetCode)).thenReturn(Optional.of(asset));
    when(this.store.getResource(asset)).thenReturn(Optional.of(resource));

    // 戻り値の検証
    assertThat(service.getAssetResourceInfo(assetCode)).isEqualTo(expected);
    // モックが想定通り呼び出されていることの確認
    verify(this.repository, times(1)).findByAssetCode(assetCode);
    verify(this.store, times(1)).getResource(asset);
  }

  @Test
  @DisplayName("testGetAssetResourceInfo_02_異常系_リポジトリに存在しないアセットコード")
  void testGetAssetResourceInfo_02_異常系_リポジトリに存在しないアセットコード() {
    // テスト用の入力データ
    String assetCode = "NotExistAssetCode";

    // モックの設定
    when(this.repository.findByAssetCode(assetCode)).thenReturn(Optional.empty());

    try {
      // 戻り値の検証
      service.getAssetResourceInfo(assetCode);
      fail();
    } catch (AssetNotFoundException e) {
      // モックが想定通り呼び出されていることの確認
      verify(this.repository, times(1)).findByAssetCode(assetCode);
    }
  }

  @Test
  @DisplayName("testGetAssetResourceInfo_03_異常系_ストアに存在しないアセットコード")
  void testGetAssetResourceInfo_03_異常系_ストアに存在しないアセットコード() {
    // テスト用の入力データ
    String assetCode = "NotExistAssetCode";

    // 期待する戻り値
    Asset asset = new Asset(assetCode, "png");

    // モックの設定
    when(this.repository.findByAssetCode(assetCode)).thenReturn(Optional.of(asset));
    when(this.store.getResource(asset)).thenReturn(Optional.empty());

    try {
      // 戻り値の検証
      service.getAssetResourceInfo(assetCode);
      fail();
    } catch (AssetNotFoundException e) {
      // モックが想定通り呼び出されていることの確認
      verify(this.repository, times(1)).findByAssetCode(assetCode);
      verify(this.store, times(1)).getResource(asset);
    }
  }
}
