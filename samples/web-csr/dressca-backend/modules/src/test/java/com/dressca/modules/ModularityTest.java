package com.dressca.modules;

import org.junit.jupiter.api.Test;
import org.springframework.modulith.core.ApplicationModules;

/**
 * Spring Modulith を使用して、モジュール間の結合度をテストするクラスです。
 */
public class ModularityTest {
  ApplicationModules modules = ApplicationModules.of(ModulesTestApplication.class);

  @Test
  void verifyModuleDependencies() {
    modules.verify();
  }
}
