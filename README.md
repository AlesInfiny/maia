<!-- textlint-disable @textlint-rule/require-header-id -->
<!-- markdownlint-disable-file CMD001 -->

# AlesInfiny Maia OSS Edition

AlesInfiny Maia OSS Edition は現代の Java システム開発において標準的なアプリケーションアーキテクチャを提供します。
商用のエンタープライズシステム開発から個人開発の小規模なシステムまで、 無償で利用可能です。

## Getting Started

AlesInfiny Maia OSS Edition は、以下の Web サイトを通じて最新バージョンを公開しています。
ドキュメントの閲覧やサンプルアプリケーションのダウンロードは、 Web サイトから行ってください。

<https://maia.alesinfiny.org/>

## ローカル開発環境

ドキュメントやサンプルアプリケーションの開発には VS Code を使用します。
最新のバージョンをインストールしてください。

### ドキュメント開発環境

ドキュメント開発環境の構築手順は以下を参照してください。

- [AlesInfiny Maia OSS Edition ドキュメントについて | ドキュメント執筆環境の構築方法](/documents/README.md#ドキュメント執筆環境の構築方法)

### サンプルアプリケーション、要件別サンプル開発環境

サンプルアプリケーションや要件別サンプルの開発環境は、 VS Code を推奨します。
開発環境の構築手順は以下を参照してください。

- [AlesInfiny Maia OSS Edition | ローカル開発環境の構築](https://maia.alesinfiny.org/guidebooks/how-to-develop/local-environment/)

## サンプルアプリケーションのテスト

### Java アプリケーション

Java アプリケーションは、 VS Code のテストランナー、または gradlew コマンドでテストできます。

テストには、メソッドレベルでの動作を確認する単体テストと、 Web API レベルでの動作を確認する結合テストがあります。
いずれのテストも完全に自動化されています。
またテストフレームワークには JUnit を使用しています。

VS Code を利用してテストを実行する場合は、 VS Code のテスト拡張機能からテストを実行します。

gradlew コマンドを利用してテストを実行する場合は、ルートプロジェクトのフォルダーで以下のコマンドを実行します。

```shell
./gradlew test
```

サンプルアプリケーションは、テストの実装方式を説明する目的で構築されており、すべての仕様に対してテストが実装されているとは限りません。

### Vue.js アプリケーション

Vue.js アプリケーションは、 Vitest でテストできます。
Vue.js アプリケーションのルートディレクトリから、以下のようなコマンドを実行します。

```shell
npm run test:unit:consumer
```

`test:unit:consumer` の部分は、 package.json に定義されているスクリプト名と一致させます。
実行するテストの対象によって、スクリプト名を変更してください。

サンプルアプリケーションは、テストの実装方式を説明する目的で構築されており、すべての仕様に対してテストが実装されているとは限りません。

## ローカル開発環境での閲覧と実行

### ドキュメント

ドキュメントの開発には Python と MkDocs を使用します。
詳細は [AlesInfiny Maia OSS Edition ドキュメントについて | 動作確認](/documents/README.md#動作確認) を参照してください。

ただし、ローカル開発環境での実行では、サンプルアプリケーションのダウンロードリンクなど、一部が動作しません。

### サンプルアプリケーション(Dressca)

サンプルアプリケーション (Dressca) の実行手順は [AlesInfiny Maia OSS Edition | クイックスタート](https://maia.alesinfiny.org/#quick-start) を参照してください。

### 要件別サンプル

各要件別サンプルのフォルダーに配置されている README.md を参照してください。

## 本番配置向けドキュメントのビルドと配置

本番配置向けドキュメントのビルドは、 GitHub Actions を利用します。
「ドキュメントのビルドとリリース」のワークフローを実行してください。
正常にビルドできると、配置するモジュールが Artifacts に「documents」という名前で格納されます。
このファイルをダウンロードして展開し、 Web サーバー (IIS) に配置してください。

なお AlesInfiny Maia OSS Edition の Web サイトは、 IIS へのホストを前提としています。

## Contributing

AlesInfiny Maia OSS Edition への貢献については [Contributing to AlesInfiny Maia OSS Edition](/.github/CONTRIBUTING.md) を参照してください。

## Code of Conduct

AlesInfiny Maia OSS Edition に関わるすべての人は、 AlesInfiny Maia OSS Edition の [行動規範](https://github.com/AlesInfiny/.github/blob/main/CODE_OF_CONDUCT.md) に従うことを期待しています。
これには、コードベース、課題追跡システム、チャットルーム、メーリングリストなど、すべてのコミュニケーションが含まれます。

## License

BIPROGY 株式会社およびすべてのコントリビューターは、本リポジトリのドキュメントに [クリエイティブ・コモンズ表示 4.0 国際ライセンス](https://creativecommons.org/licenses/by/4.0/) のライセンスを付与します。
詳細は [LICENSE.md](/LICENSE) を参照してください。
また本リポジトリのソースコードに、 [Apache License, Version 2.0](https://www.apache.org/licenses/LICENSE-2.0) のライセンスを付与します。
詳細は [LICENSE-CODE.md](/LICENSE-CODE) を参照してください。

このプロジェクトでは、様々なオープンソースライセンスの下でライセンスされている 3rd パーティーのライブラリを使用しています。
詳細は [third-party-licenses](/third-party-licenses/) フォルダーを参照してください。

このプロジェクトのライセンスは、 BIPROGY 株式会社の商標を使用する権利を付与するものではありません。
詳細は [AlesInfiny Maia OSS Edition | 商標](https://maia.alesinfiny.org/about-maia/trademarks/) を参照してください。

<!-- textlint-enable @textlint-rule/require-header-id -->
