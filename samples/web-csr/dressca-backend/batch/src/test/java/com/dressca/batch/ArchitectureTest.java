package com.dressca.batch;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;

/**
 * バッチアプリケーションのアーキテクチャを検証するテストです。
 * バッチ層がアプリケーションモジュールの内部構造に依存していないことを確認します。
 */
@AnalyzeClasses(packages = "com.dressca.batch")
class ArchitectureTest {

  @ArchTest
  static void バッチ層はアプリケーションモジュールの内部パッケージに依存してはいけない(JavaClasses classes) {
    noClasses().should().dependOnClassesThat()
        .resideInAPackage("com.dressca.applicationmodules..internal..").check(classes);
  }
}
