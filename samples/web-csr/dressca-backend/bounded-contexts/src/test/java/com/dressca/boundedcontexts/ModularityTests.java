package com.dressca.boundedcontexts;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.modulith.core.ApplicationModules;
import org.springframework.modulith.docs.Documenter;

/**
 * モジュール構造の検証とドキュメント生成を行うテストです。
 */
class ModularityTests {

  static final ApplicationModules modules =
      ApplicationModules.of(ModularityTests.class.getPackageName());

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
