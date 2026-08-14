package com.dressca.applicationmodules;

import com.tngtech.archunit.core.domain.JavaClass;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.modulith.core.ApplicationModules;
import org.springframework.modulith.docs.Documenter;

/**
 * モジュール構造の検証とドキュメント生成を行うテストです。
 */
class ModularityTests {

  /**
   * 境界づけられたコンテキストではない技術基盤のパッケージです。モジュール検出の対象外とします。
   */
  private static final String INFRASTRUCTURE_PACKAGE = "com.dressca.applicationmodules.shared..";

  static final ApplicationModules modules =
      ApplicationModules.of(ModularityTests.class.getPackageName(),
          JavaClass.Predicates.resideInAPackage(INFRASTRUCTURE_PACKAGE));

  @Test
  @DisplayName("コンテキスト間の依存関係が正しいことを検証する")
  void verifiesModularStructure() {
    modules.verify();
  }

  @Test
  @DisplayName("モジュール構造のドキュメントを生成する")
  void writesDocumentationSnippets() {
    new Documenter(modules).writeDocumentation();
  }
}
