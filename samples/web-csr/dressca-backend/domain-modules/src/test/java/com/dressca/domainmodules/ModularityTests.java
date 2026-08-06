package com.dressca.domainmodules;

import org.junit.jupiter.api.Test;
import org.springframework.modulith.core.ApplicationModules;

/**
 * モジュール構造の検証とドキュメント生成を行うテストです。
 */
class ModularityTests {

  static final ApplicationModules modules = ApplicationModules.of("com.dressca.domainmodules");

  @Test
  void verifiesModularStructure() {
    modules.verify();
  }
}
