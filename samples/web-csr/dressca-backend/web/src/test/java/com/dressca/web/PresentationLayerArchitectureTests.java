package com.dressca.web;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.core.importer.ImportOption;
import org.junit.jupiter.api.Test;

/**
 * プレゼンテーション層のアーキテクチャを検証するテストです。
 *
 * <p>Spring Modulith の {@code ApplicationModules#verify()} は domain-modules 内部の
 * モジュール間依存しか検証できないため、 Gradle モジュールをまたぐ依存はここで検証します。</p>
 */
class PresentationLayerArchitectureTests {

  private static final JavaClasses PRESENTATION_CLASSES =
      new ClassFileImporter().withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
          .importPackages("com.dressca.web");

  @Test
  void doesNotDependOnDomainModuleInternals() {
    noClasses().should().dependOnClassesThat()
        .resideInAPackage("com.dressca.domainmodules..internal..")
        .because("プレゼンテーション層はドメインモジュールの内部実装を参照できません。アプリケーションサービスなどの公開 API を利用してください。")
        .check(PRESENTATION_CLASSES);
  }
}
