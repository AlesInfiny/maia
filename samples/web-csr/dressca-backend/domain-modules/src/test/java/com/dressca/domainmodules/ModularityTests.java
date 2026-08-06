package com.dressca.domainmodules;

import org.junit.jupiter.api.Test;
import org.springframework.modulith.core.ApplicationModules;
import org.springframework.modulith.docs.Documenter;

/**
 * モジュール構造の検証とドキュメント生成を行うテストです。
 *
 * <p>このテストが検証するのは domain-modules 内部のモジュール間依存のみです。
 * Gradle モジュールをまたぐ依存は各 Gradle モジュールのアーキテクチャテストで検証します。</p>
 */
class ModularityTests {

  // このテストクラスはドメインモジュールのルートパッケージに配置しているため、
  // パッケージ名を直接記述せずにテストクラスのパッケージを基点にします。
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
