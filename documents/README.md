<!-- textlint-disable @textlint-rule/require-header-id -->
<!-- markdownlint-disable-file CMD001 -->
<!-- cSpell:ignore hoge hogehoge -->
# AlesInfiny Maia OSS Edition ドキュメントについて

## 本番環境

<https://maia.alesinfiny.org/>

## フォルダー構造

documents フォルダー配下のフォルダー、ファイルの配置は以下の通りです。

|               |                  |                       |                     |                     |                                               |
| ------------- | ---------------- | --------------------- | ------------------- | ------------------- | --------------------------------------------- |
| _materials    |                  |                       |                     |                     | ドキュメント内で利用する素材                  |
|               | images           |                       |                     |                     | 画像素材（画像を作るための元ファイル）        |
|               |                  | app-architecture      |                     |                     | contents/images フォルダーと構造をそろえる    |
|               |                  |                       | hoge.drawio         |                     | ファイル名は生成後の画像ファイルと同じにする  |
| contents      |                  |                       |                     |                     | ドキュメント本体                              |
|               | about-maia       |                       |                     |                     | 利用規約等、ライセンス関連のファイル          |
|               | app-architecture |                       |                     |                     | アプリケーションアーキテクチャ                |
|               |                  | client-side-rendering |                     |                     | CSR 編                                        |
|               |                  |                       | backend-application |                     | バックエンドアプリの構造詳細                  |
|               |                  |                       | test                |                     | テスト方針                                    |
|               |                  |                       |                     | backend-application | バックエンドアプリのテスト方針                |
|               |                  | overview              |                     |                     | 概要編                                        |
|               | assets           |                       |                     |                     | 共通資材（ロゴなど）                          |
|               |                  | images                |                     |                     |                                               |
|               | guidebooks       |                       |                     |                     | ガイドライン系ドキュメント                    |
|               |                  | how-to-develop        |                     |                     | アプリケーション開発手順                      |
|               |                  |                       | java                |                     | Java 編                                       |
|               |                  |                       | local-environment   |                     | ローカル開発環境の構築                        |
|               |                  |                       | vue-js              |                     | Vue.js 編                                     |
|               | images           |                       |                     |                     | ページ固有の画像ファイル置き場                |
|               |                  | about-maia            |                     |                     | mdファイルの配置フォルダーと構造をそろえる    |
|               |                  |                       | hoge.png            |                     | 画像ファイルは svg か png にする                  |
|               |                  |                       | animation.gif       |                     | gif アニメーションも利用可                      |
|               |                  | app-architecture      |                     |                     |                                               |
|               |                  | guidebooks            |                     |                     |                                               |
|               |                  |                       | how-to-develop      |                     |                                               |
|               |                  |                       | samples             |                     |                                               |
|               |                  |                       | terms               |                     |                                               |
|               | samples          |                       |                     |                     | サンプルアプリケーション解説                  |
|               |                  | azure-ad-b2c          |                     |                     | Azure AD B2C を利用しているサンプルの解説     |
|               |                  | downloads             |                     |                     | サンプルアプリケーションコード置き場( zip 圧縮) |
|               | stylesheets      |                       |                     |                     | 既定のスタイルシートの上書き設定              |
|               | index.md         |                       |                     |                     | トップページ                                  |
| includes      |                  |                       |                     |                     | Snippets の置き場                             |
|               | abbreviations.md |                       |                     |                     | 略語用語集                                    |
| overrides     |                  |                       |                     |                     | Mkdocs Material の拡張ファイル置き場（\*）     |
| readme-images |                  |                       |                     |                     | README.md 内の画像ファイル置き場              |
| .gitignore    |                  |                       |                     |                     | mkdocs 用の gitignore                         |
| mkdocs.yml    |                  |                       |                     |                     | mkdocs の設定ファイル                         |
| README.md     |                  |                       |                     |                     | このドキュメント                              |

\*：詳細は [Mkdocs Material の解説](https://squidfunk.github.io/mkdocs-material/customization/?h=theme#extending-the-theme) と [GitHub リポジトリ](https://github.com/squidfunk/mkdocs-material/tree/master/src/overrides) を参照。

<!-- textlint-enable @textlint-rule/require-header-id -->
