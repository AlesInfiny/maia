package com.dressca.web.consumer;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;

/**
 * 利用者アプリケーションのアーキテクチャを検証するテストです。
 * プレゼンテーション層がアプリケーションモジュールの内部構造に依存していないことを確認します。
 */
@AnalyzeClasses(packages = "com.dressca.web.consumer")
class ArchitectureTest {

  @ArchTest
  static void プレゼンテーション層はアプリケーションモジュールの内部パッケージに依存してはいけない(JavaClasses classes) {
    noClasses().should().dependOnClassesThat()
        .resideInAPackage("com.dressca.applicationmodules..internal..").check(classes);
  }
}
