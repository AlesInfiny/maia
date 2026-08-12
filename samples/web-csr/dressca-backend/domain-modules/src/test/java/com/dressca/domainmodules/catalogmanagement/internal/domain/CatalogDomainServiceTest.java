package com.dressca.domainmodules.catalogmanagement.internal.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import com.dressca.domainmodules.catalogmanagement.internal.domain.repository.CatalogBrandRepository;
import com.dressca.domainmodules.catalogmanagement.internal.domain.repository.CatalogCategoryRepository;
import com.dressca.domainmodules.catalogmanagement.internal.domain.repository.CatalogRepository;
import com.dressca.domainmodules.catalogmanagement.model.CatalogBrand;
import com.dressca.domainmodules.catalogmanagement.model.CatalogCategory;
import com.dressca.domainmodules.catalogmanagement.model.CatalogItem;
import com.dressca.domainmodules.DomainModulesTestConfig;
import java.math.BigDecimal;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.context.MessageSourceAutoConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

/**
 * {@link CatalogDomainService}の動作をテストするクラスです。
 */
@ExtendWith({SpringExtension.class, MockitoExtension.class})
@Import(DomainModulesTestConfig.class)
@TestPropertySource(properties = "spring.messages.basename=domainmodules.messages")
@ImportAutoConfiguration(MessageSourceAutoConfiguration.class)
public class CatalogDomainServiceTest {
  @Mock
  private CatalogRepository catalogRepository;
  @Mock
  private CatalogBrandRepository catalogBrandRepository;
  @Mock
  private CatalogCategoryRepository catalogCategoryRepository;
  @InjectMocks
  private CatalogDomainService service;

  @Test
  void testExistCatalogBrand_正常系_指定したカタログブランドが存在する場合trueを返す() {
    // Arrange
    UUID targetId = UUID.randomUUID();
    CatalogBrand catalogBrand = this.createCatalogBrand(targetId);
    when(this.catalogBrandRepository.findById(targetId)).thenReturn(catalogBrand);

    // Act
    boolean existCatalogBrand = service.existCatalogBrand(targetId);

    // Assert
    assertThat(existCatalogBrand).isTrue();
  }

  @Test
  void testExistCatalogBrand_正常系_指定したカタログブランドが存在しない場合falseを返す() {
    // Arrange
    UUID targetId = UUID.randomUUID();
    when(this.catalogBrandRepository.findById(targetId)).thenReturn(null);

    // Act
    boolean existCatalogBrand = service.existCatalogBrand(targetId);

    // Assert
    assertThat(existCatalogBrand).isFalse();
  }

  @Test
  void testExistCatalogCategory_正常系_指定したカタログカテゴリが存在する場合trueを返す() {
    // Arrange
    UUID targetId = UUID.randomUUID();
    CatalogCategory catalogCategory = this.createCatalogCategory(targetId);
    when(this.catalogCategoryRepository.findById(targetId)).thenReturn(catalogCategory);

    // Act
    boolean existCatalogCategory = service.existCatalogCategory(targetId);

    // Assert
    assertThat(existCatalogCategory).isTrue();
  }

  @Test
  void testExistCatalogCategory_正常系_指定したカタログカテゴリが存在しない場合falseを返す() {
    // Arrange
    UUID targetId = UUID.randomUUID();
    when(this.catalogCategoryRepository.findById(targetId)).thenReturn(null);

    // Act
    boolean existCatalogCategory = service.existCatalogCategory(targetId);

    // Assert
    assertThat(existCatalogCategory).isFalse();
  }

  @Test
  void testExistCatalogItem_正常系_指定したカタログアイテムが存在する場合trueを返す() {
    // Arrange
    UUID targetId = UUID.randomUUID();
    CatalogItem catalogItem = this.createCatalogItem(targetId);
    when(this.catalogRepository.findById(targetId)).thenReturn(catalogItem);

    // Act
    boolean existCatalogItem = service.existCatalogItem(targetId);

    // Assert
    assertThat(existCatalogItem).isTrue();
  }

  @Test
  void testExistCatalogItem_正常系_指定したカタログアイテムが存在しない場合falseを返す() {
    // Arrange
    UUID targetId = UUID.randomUUID();
    when(this.catalogRepository.findById(targetId)).thenReturn(null);

    // Act
    boolean existCatalogItem = service.existCatalogItem(targetId);

    // Assert
    assertThat(existCatalogItem).isFalse();
  }

  private CatalogItem createCatalogItem(UUID id) {
    String defaultDescription = "Description.";
    String defaultName = "Name";
    BigDecimal defaultPrice = BigDecimal.valueOf(100_000_000L);
    String defaultProductCode = "C000000001";
    boolean defaultIsDeleted = false;

    CatalogItem catalogItem = new CatalogItem(id, defaultName, defaultDescription, defaultPrice,
        defaultProductCode, UUID.randomUUID(), UUID.randomUUID(), defaultIsDeleted);
    // catalogItem.setId(id);
    return catalogItem;
  }

  private CatalogBrand createCatalogBrand(UUID id) {
    CatalogBrand catalogBrand = new CatalogBrand("Name");
    catalogBrand.setId(id);
    return catalogBrand;
  }

  private CatalogCategory createCatalogCategory(UUID id) {
    CatalogCategory catalogCategory = new CatalogCategory("Name");
    catalogCategory.setId(id);
    return catalogCategory;
  }
}
