package com.dressca.batch;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;

/**
 * バッチアプリケーションのアーキテクチャを検証するテストです。
 * プレゼンテーション層がアプリケーションモジュールの内部構造に依存していないことを確認します。
 */
@AnalyzeClasses(packages = "com.dressca.batch")
class ArchitectureTest {

  @ArchTest
  static final ArchRule shouldNotDependOnApplicationModulesInternalPackages = noClasses().should()
      .dependOnClassesThat().resideInAPackage("com.dressca.applicationmodules..internal..")
      .as("バッチアプリケーションが application-modules の internal パッケージに依存していないこと")
      .because("バッチアプリケーションは application-modules の internal パッケージに依存してはいけないため");
}
