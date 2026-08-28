package com.dressca.web.admin;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;

/**
 * 管理者アプリケーションのアーキテクチャを検証するテストです。
 * プレゼンテーション層がアプリケーションモジュールの内部構造に依存していないことを確認します。
 */
@AnalyzeClasses(packages = "com.dressca.web.admin")
class ArchitectureTest {

  @ArchTest
  static final ArchRule shouldNotDependOnApplicationModulesInternalPackages = noClasses().should()
      .dependOnClassesThat().resideInAPackage("com.dressca.applicationmodules..internal..")
      .as("プレゼンテーション層が application-modules の internal パッケージに依存していないこと")
      .because("プレゼンテーション層は application-modules の internal パッケージに依存してはいけないため");
}
