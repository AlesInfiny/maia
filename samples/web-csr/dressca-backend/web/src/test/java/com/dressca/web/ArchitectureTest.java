package com.dressca.web;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;

/**
 * 管理者アプリケーションと利用者アプリケーションの共通部品のアーキテクチャを検証するテストです。
 * プレゼンテーション層がアプリケーションモジュールの内部構造に依存していないことを確認します。
 */
@AnalyzeClasses(packages = "com.dressca.web")
class ArchitectureTest {

  @ArchTest
  static void プレゼンテーション層はアプリケーションモジュールの内部パッケージに依存してはいけない(JavaClasses classes) {
    noClasses().should().dependOnClassesThat()
        .resideInAPackage("com.dressca.applicationmodules..internal..").check(classes);
  }
}
