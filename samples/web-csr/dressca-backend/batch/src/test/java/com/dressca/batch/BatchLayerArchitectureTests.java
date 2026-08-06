package com.dressca.batch;

import static com.tngtech.archunit.base.DescribedPredicate.not;
import static com.tngtech.archunit.core.domain.JavaClass.Predicates.resideInAPackage;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.core.importer.ImportOption;
import org.junit.jupiter.api.Test;

/**
 * バッチ層のアーキテクチャを検証するテストです。
 *
 * <p>Spring Modulith の {@code ApplicationModules#verify()} は domain-modules 内部の
 * モジュール間依存しか検証できないため、 Gradle モジュールをまたぐ依存はここで検証します。</p>
 */
class BatchLayerArchitectureTests {

  /** 内部実装が配置されたパッケージです。 */
  private static final String DOMAIN_MODULE_INTERNALS = "com.dressca.domainmodules..internal..";

  /** バッチ層からの依存を例外的に許可する、ドメインのリポジトリーのパッケージです。 */
  private static final String ALLOWED_DOMAIN_REPOSITORIES =
      "com.dressca.domainmodules..internal.domain.repository";

  private static final JavaClasses BATCH_CLASSES =
      new ClassFileImporter().withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
          .importPackages("com.dressca.batch");

  @Test
  void doesNotDependOnDomainModuleInternalsExceptRepositories() {
    noClasses().should()
        .dependOnClassesThat(resideInAPackage(DOMAIN_MODULE_INTERNALS)
            .and(not(resideInAPackage(ALLOWED_DOMAIN_REPOSITORIES))))
        .because("バッチ層はドメインモジュールの内部実装を参照できません。"
            + "アプリケーションサービスなどの公開 API か、例外的に許可されたドメインのリポジトリーを利用してください。")
        .check(BATCH_CLASSES);
  }
}
