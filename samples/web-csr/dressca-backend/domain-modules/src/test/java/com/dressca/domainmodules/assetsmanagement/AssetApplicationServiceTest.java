package com.dressca.domainmodules.assetsmanagement;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.dressca.domainmodules.assetsmanagement.dto.AssetResourceInfo;
import com.dressca.domainmodules.assetsmanagement.exception.AssetNotFoundException;
import com.dressca.domainmodules.assetsmanagement.internal.domain.repository.AssetRepository;
import com.dressca.domainmodules.assetsmanagement.internal.domain.store.AssetStore;
import com.dressca.domainmodules.assetsmanagement.model.Asset;
import com.dressca.domainmodules.DomainModulesTestConfig;
import com.dressca.systemcommon.log.AbstractStructuredLogger;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.context.MessageSourceAutoConfiguration;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Import;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

/**
 * {@link AssetApplicationService}の動作をテストするクラスです。
 */
@Import(DomainModulesTestConfig.class)
@ExtendWith({SpringExtension.class, MockitoExtension.class})
@TestPropertySource(properties = "spring.messages.basename=domainmodules.messages")
@ImportAutoConfiguration(MessageSourceAutoConfiguration.class)
public class AssetApplicationServiceTest {

  @Mock
  private AssetRepository repository;
  @Mock
  private AssetStore store;
  @Autowired
  private MessageSource messages;
  @Mock
  private AbstractStructuredLogger apLog;

  private AssetApplicationService service;

  @BeforeEach
  void setUp() {
    service = new AssetApplicationService(repository, store, messages, apLog);
  }

  @Test
  void testGetAssetResourceInfo_正常系_存在するアセットコード() throws AssetNotFoundException {
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
  void testGetAssetResourceInfo_異常系_リポジトリに存在しないアセットコード() {
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
  void testGetAssetResourceInfo_異常系_ストアに存在しないアセットコード() {
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
