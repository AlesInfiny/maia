package com.dressca.boundedcontexts;

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
  void verifiesModularStructure() {
    modules.verify();
  }

  @Test
  void writesDocumentationSnippets() {
    new Documenter(modules).writeDocumentation();
  }
}
