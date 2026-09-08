---
title: E2E テスト
description: アプリケーションのテスト方法について解説します。
---

# E2E テスト {#top}

## テストの設計 {#test-design}

### Page Object Models {#page-object-models}

- POM について、 Playwright 公式ドキュメントを参照しつつ説明します。

- playwright.config.ts がデフォルトでは src に作られるので移動するか検討
- testCase の命名がよくわからん

```text title="e2e 配下のフォルダー構成" linenums="0"
<project-name>
├─ e2e/
│  ├─ page　------------------------ URL 本位のページを表すクラスを配置します。
│  │  ├─ feature/
│  │  │  ├─ page1.ts
│  │  │  └─ page2.ts
│  │  └─ root/
│  │     └─ homePage.ts
│  ├─ testCase/  ------------------- ページを操作するテストヘルパーを配置します。
│  │  │  ├─ page1TestCase.ts
│  │  │  └─ page2TestCase.ts
│  │  └─ root/
│  │     └─ homePageTestCase.ts
│  ├─ testScenario/ ---------------- 検証するテストシナリオを配置します。
│     └ BusinessScenario.test.ts --- 検証対象の画面操作を実装します。
```

- 下記の構成を用いて、 Consumer アプリケーションにおいてカタログアイテムが注文できることを確認します。
- [Consumer アプリケーションの機能 - カタログアイテムを注文する](../../samples/dressca/index.md#order-catalog-item)

```text title="e2e 配下のフォルダー構成" linenums="0"
<project-name>
├─ e2e/
│  ├─ page
│  │  ├─ authentication/
│  │  │  └─ loginPage.ts
│  │  ├─ basket/
│  │  │  └─ basketPage.ts
│  │  ├─ catalog/
│  │  │  └─ catalogPage.ts
│  │  ├─ ordering/
│  │  │  └─ orderPage.ts
│  │  └─ root/
│  │     └─ homePage.ts
│  ├─ testCase/
│  │  │  ├─ authenticationTestCase.ts
│  │  │  ├─ basketTestCase.ts
│  │  │  ├─ catalogTestCase.ts
│  │  │  ├─ orderingTestCase.ts
│  │  │  └─ homeTestCase.ts
│  │  └─ root/
│  │     └─ homePageTestCase.ts
│  ├─ testScenario/
│      └ shoppingScenario.test.ts 
```

- こちらの方が簡単なはず
- 下記の構成を用いて、 Admin アプリケーションにおいてカタログアイテムが編集できることを確認します。
- [Admin アプリケーションの機能 - カタログアイテムを編集する](../../samples/dressca/index.md#order-catalog-item)

```text title="e2e 配下のフォルダー構成" linenums="0"
<project-name>
├─ e2e/
│  ├─ page
│  │  ├─ authentication/
│  │  │  └─ loginPage.ts
│  │  ├─ catalog/
│  │  │  ├─ itemsPage.ts
│  │  │  └─ itemsEditPage.ts
│  │  └─ root/
│  │     └─ homePage.ts
│  ├─ testCase/
│  │  │  ├─ catalogTestCase.ts
│  │  │  └─ homeTestCase.ts
│  │  └─ root/
│  │     └─ homePageTestCase.ts
│  ├─ testScenario/
│      └ itemsManagementScenario.test.ts 
```

- ローカルでの実行方法について解説します。

- CI/CD パイプラインでの実行方法について解説します。
