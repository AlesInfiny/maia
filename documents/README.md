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
|               |                  | files                 |                     |                     |                                               |
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

## ドキュメントの作成方法（わかっている人向けの大雑把な手順）

このリポジトリは GitHub-flow で開発します。
Feature ブランチの名前は「feature/<更新内容を表す名前>」として開発してください。
記事の作成後は main ブランチへのマージを行う Pull Request を投げてください。

## ドキュメントの作成方法（詳細版）

### ローカルの main ブランチを最新にする

Visual Studio Code を起動して、 [ファイル] メニューから [ワークスペースを開く] を選択します。
クローンしたフォルダー内にある maia.code-workspace ファイルを選択して開きます。

[`ソース管理`] メニューを開き、 [チェックアウト先] を選択します。

![チェックアウト先をクリック](readme-images/click-checkout.png)

main ブランチを選択します（コミットハッシュは図とは異なるはずです。）。

![main ブランチを選択](readme-images/select-main-branch.png)

main ブランチをチェックアウトしたら、 pull を実行して最新を取得します。

![git pull を実行](readme-images/git-pull.png)

### Feature ブランチの作成

記事を作成するための Feature ブランチをローカルに作成します。
[`ソース管理`] メニューを開き、 [ブランチ] > [分岐の作成] を選択します。

![ブランチの作成をクリック](readme-images/click-create-branch.png)

作成するブランチ名を入力します。
ブランチ名は「feature/<更新内容を表す名前>」として Enter を押下します。

![ブランチの作成](readme-images/create-branch.png)

### ドキュメントの作成

フォルダー構成に従って Markdown ファイルを作成してください。

Markdown の作成にあたっては、 Material for MkDocs の Web サイトを参照してください。
素の Markdown とは異なる表現パターンがあります。

<https://squidfunk.github.io/mkdocs-material/reference/abbreviations/>

### 動作確認

記事をある程度作成したら、 mkdocs を用いて確認します。

Visual Studio Code で [ターミナル] を開きます。
ターミナルが開いていない場合は、以下の通り新たに開いてください。

![ターミナルを開く](readme-images/open-terminal.png)

[ターミナル] ウィンドウの右上のドロップダウンを開き、お好きなものを選択してください。
以降 Command Prompt を選択した場合の例を示します。

![コマンドプロンプトを起動](readme-images/open-command-prompt.png)

ターミナルのコマンドプロンプトで、 [mkdocs.yml] ファイルのあるフォルダーに移動してください。
フォルダー移動後、以下のコマンドを実行してください。

```plain
mkdocs serve
```

うまく実行できると、以下のようなメッセージが出て、 Web サーバーが起動します。

```plain
INFO     -  Building documentation...
INFO     -  Cleaning site directory
INFO     -  Documentation built in 1.04 seconds
INFO     -  [10:07:52] Serving on http://127.0.0.1:8000/
INFO     -  [10:07:53] Browser connected: http://127.0.0.1:8000/
```

最後に出力されている URL を任意のブラウザーで開いてください。
作成した記事を参照し、正しく表示できていることを確認します。

警告がある場合は、そのまま Markdown を修正して保存します。
保存するとブラウザー側はホットリロードが行われ、即座に編集結果が反映されます。
また mkdocs.yml ファイルを編集した場合も、ファイルを保存した瞬間にホットリロードが走ります。

Markdown ファイルを追加したら、ほとんどの場合 mkdocs.yml の nav セクションを修正する必要があります。

### 体裁の修正

#### markdownlint

markdownlint の拡張機能をインストールしていると、 [問題] ウィンドウに以下のような Markdown の体裁エラーを指摘するコメントが出ます。

![markdownlint の警告メッセージ](readme-images/markdownlint-result.png)

本リポジトリでは Markdown の体裁にエラーがあると、 Web サイトの発行が行えないようになっています。
警告が出ている場合は、メッセージを読んで必ず解消しましょう。

#### cSpell

cSpell の拡張機能をインストールしていると、 [問題] ウィンドウに未定義の単語の存在を知らせるコメントが出ます。
この拡張機能は、英単語のスペルミスを検出するために導入するものであり、コメントの多くはスペルミスに起因しています。
必ず対応するようにしてください。

複合語や技術用語は、辞書登録しないと誤検知されることがあります。
その場合はワークスペースの辞書に、単語を登録するようにしてください。
[cspell.json] ファイルの `words` に単語を登録できます。

コード内や設定ファイル内の文字など、単語登録することが望ましくないと考える場合は、以下の記事を参照して、各ページで抑制してください。
cSpell が実行されないようにするのではなく、そのページ内で使用する抑制しても良い単語を、ページの先頭に記述する方式で抑制しましょう。
このページの上部にも設定してあります。

<https://github.com/streetsidesoftware/cspell/tree/main/packages/cspell#enable--disable-checking-sections-of-code>

#### textlint

vscode-textlint の拡張機能をインストールしていると、 [問題] ウィンドウに校正に関するコメントが出ます。
この拡張機能は、技術ドキュメントを書く際の冗長な表現を排除したり、表記ゆれの検出したりする自動校正ツールです。
多くの場合、文章の見直しによってエラーを回避できます。
必ず対応するようにしてください。

一部の機能については、誤検知をしてしまう場合や、ルールの適用が不適切なこともあります。
その場合は、文書内に以下のようなコメントを記述することで、一部の解析ルールを無視するよう設定できます。

```markdown
<!-- textlint-disable @textlint-rule/require-header-id -->

# 解析ルールが無効になる場所

<!-- textlint-enabled @textlint-rule/require-header-id -->
```

詳細は以下を参照してください。

<https://github.com/textlint/textlint-filter-rule-comments#usage>

### 用語集の整理

cSpell の拡張機能をインストールしていると、 [問題] ウィンドウに上記と同じようなスペルミスを指摘するコメントが出ます。
ドキュメントに関連する部分で警告が出る場合は対策を講じてください。
対応方法は以下の通りです。

1. 一般的な用語 / 技術用語である場合

    ワークスペース ( maris.code-workspace ) の用語集に単語を追加してください。

1. 特定のページでのみ使用する特殊な用語や略語の場合

    以下を参照して、ページ内に `cSpell:ignore` を追加して、警告を除外する用語を追加してください。
    設定はページ最上部に記述します。

    <https://github.com/streetsidesoftware/cspell/tree/main/packages/cspell#ignore>

### 各ページの title/description の設定

以下のように、各 markdown ファイルの上部に `title` と `description` を設定しています。

``` text
---
title: CSR 編
description: クライアントサイドレンダリングを行う Web アプリケーションの アーキテクチャについて解説します。
---
```

ここで設定した `title` と `description` を元にソーシャルカードが生成されるため、以下の文字数を守って設定します。
( `description` を省略すると、 mkdocs.yml の `site_description` の値が設定されます。)

- カード上に表示できる文字数
    - `title`：全角 9 文字×3 行まで
    - `description`: 半角 59 文字(全角だと 30 文字目が半分途切れる)× 2 行まで

※スペースを空けないと 1 単語とみなされ、同じ行に詰め込もうとして文章が途切れるので適宜スペースで区切ってください。

- 例：

    - `title: Azure AD B2C を利用したユーザー認証` の場合
    ![ソーシャルカード失敗例](readme-images/social-card-example-error.png)
    - `title: Azure AD B2C を 利用した ユーザー認証` の場合
    ![ソーシャルカード](readme-images/social-card-example.png)

文字が途切れていないか等を適宜確認してください。
ソーシャルカードはローカルでは生成せず、 CI 上でのドキュメントビルド時に生成されるよう設定しています。
生成されたソーシャルカードは以下の手順で確認してください。

1. GitHub Actions の「ドキュメントのビルド(CI 用)」を開き、対象のブランチを選択してワークフローを実行します。

1. 実行したワークフローの Summary の Artifacts から　documents をダウンロードします。

1. ダウンロードした documents.zip 内の docs.zip を解凍し、`assets/images/social` に生成されたソーシャルカードを確認します。

### 修正内容のコミット

記事の作成と体裁の修正が完了したら、 Feature ブランチにコミットします。
[`ソース管理`] メニューを開き、上部の [メッセージ] にコミットメッセージを入力します。
また [✓] アイコンを押下して、ローカルリポジトリにコミットします。

コミットは意味のある単位であれば、何回行ってもかまいません。

### Feature ブランチのアップロード

コミットが完了したら、 Feature ブランチをプッシュして、リモートリポジトリにアップロードします。
[`ソース管理`] メニューを開き、 [プッシュ] を選択します。

![プッシュを選択](readme-images/click-push.png)

### プルリクエストの作成

GitHub でプッシュした Feature ブランチを main ブランチにマージするプルリクエストを作成してください。
プルリクエストを作成すると、 Markdown と YAML の Lint と、ビルド可否のチェックが行われます。

Lint の結果、警告がある場合は GitHub Actions が失敗します。

![linter の警告による GitHub Actions の失敗](readme-images/linter-error.png)

mkdocs のビルドがエラーとなった場合も GitHub Actions は失敗します。
どちらの場合も GitHub Actions のログからエラー情報を確認できます。
右側の詳細ログからエラーとなっている箇所を開くことで、ログメッセージを確認できます。

![mkdocs の実行エラー](readme-images/mkdocs-error.png)

プルリクエストをマージすると、継続的デプロイメントが走り、自動的にステージング環境の Web サイトが更新されます。

### 修正内容の確認

Web サイトの更新が完了したら、以下にアクセスして問題なく修正が反映されていることを確認します。

<https://app-oss-maia-staging.azurewebsites.net/>

問題がある場合は、再度 Feature ブランチを作成するところから再実施してください。
修正に時間がかかる場合は、以前のリリース物を再リリースし、切り戻すようにしてください。

## 画像の作成方法

### 画像の作成ルール

図を作成する場合は \*.drawio で作成してください。
\*.drawio ファイルは、 _materials/images フォルダーの配下に作成してください。
画像ファイルは svg 形式または png 形式で出力し、出力した画像ファイルを contents/images フォルダーの配下に配置してください。

スクリーンショットなど、元となる \*.drawio ファイルが存在しない場合は、 _materials/images フォルダーの配下にファイルを配置する必要ありません。

_materials/images フォルダー、 contents/images フォルダーの配下は、画像を配置するマークダウンの置き場所にあわせてフォルダーを掘り、その中に各ファイルを配置してください。

画像ファイル作成にあたり、以下のルールを守って作成してください。

| 項目               | 設定値/選択する値                                    |
| ------------------ | ---------------------------------------------------- |
| フォントサイズ     | 18pt                                                 |
| フォントファミリー | [カスタム] > [Google Fonts] > 「Noto Sans JP」を設定 |

![フォントファミリーの設定例](readme-images/drawio-font-family.png)

### ライトモード/ダークモードに関する設定

本ドキュメントでは、ライトモード/ダークモードの切り替えができるように設定されています。
各モードへ対応するために、ライトモード向け / ダークモード向けの 2 通りの画像を作成します。
また、モードによる切り替わりが行われるよう、マークダウンを実装します。

ライトモード用 / ダークモード用の画像作成やマークダウンの実装方法は以下の通りです。

1. 画像原本の \*.drawio ファイルについて、ライトモード用、ダークモード用の 2 通りのファイルを作成します。  
   以下の命名規則に従い、どちらのモード用のファイルかを \*.drawio ファイル名末尾の文字列で表現します。

    | 対象モード   | \*.drawio ファイル名末尾の文字列 |
    | ------------ | -------------------------------- |
    | ライトモード | -light.drawio                    |
    | ダークモード | -dark.drawio                     |

1. コンテンツとして配置する png ファイルも、ライトモード用、ダークモード用の 2 通り用意します。  
   ファイル名の命名規則については \*.drawio ファイルと同様の規則を適用します。

1. マークダウンの実装は以下のように「#only-light」または「#only-dark」を付加して配置します。  
   詳細は以下の公式ドキュメントを参照してください。  
   <https://squidfunk.github.io/mkdocs-material/reference/images/#light-and-dark-mode>

    - 「align=right」は画像の配置に関する属性であり、画面デザインにあわせて任意で付加します。  
    詳細は以下の公式ドキュメントを参照してください。  
    <https://squidfunk.github.io/mkdocs-material/reference/images/#image-alignment>

    - 「loading=lazy」は画像の遅延読込に関する属性であり、必須で付加します。  
    詳細は以下の公式ドキュメントを参照してください。  
    <https://squidfunk.github.io/mkdocs-material/reference/images/#image-lazy-loading>

    ```md
    ![mono-repo の第 2 階層構造例](../../images/guidebooks/git/mono-repo-structure-2nd-subsystem-light.png#only-light){ align=right loading=lazy }
    ![mono-repo の第 2 階層構造例](../../images/guidebooks/git/mono-repo-structure-2nd-subsystem-dark.png#only-dark){ align=right loading=lazy }
    ```

## ドキュメント執筆環境の構築方法

### Visual Studio Code のインストール

以下のサイトから最新版の Visual Studio Code をインストールします。

<https://code.visualstudio.com/download>

### リポジトリのクローン

ローカルマシン内の適当なフォルダーで、このリポジトリをクローンします。
詳細な手順は以下を参照してください。

<https://docs.github.com/ja/github/creating-cloning-and-archiving-repositories/cloning-a-repository-from-github/cloning-a-repository>

### ユーザー名、メールアドレスの設定

クローンした Git リポジトリに対して、自分のユーザー名、メールアドレスを設定します。
コマンドプロンプトでクローンしたリポジトリのあるフォルダーに移動して、現在設定されているユーザー名、メールアドレスを確認します。

```plane
c:\hogehoge\maia>git config user.name
XXXXXXXXXXX

c:\hogehoge\maia>git config user.email
YYYYY@hoge.com
```

グローバルの設定が生きている場合は、以下を参照し、メールアドレスとユーザー名の上書き設定を推奨します。
メールアドレスは `GitHub が提供する no-reply メールアドレス` を設定することをおすすめします。
また GitHub 上の設定変更もあわせて行うことをおすすめします。

<https://docs.github.com/ja/github/setting-up-and-managing-your-github-user-account/managing-email-preferences/setting-your-commit-email-address>

### Visual Studio Code 拡張機能のインストール

Visual Studio Code の [ファイル] メニューから [ワークスペースを開く] を選択します。
クローンしたフォルダー内にある maia.code-workspace ファイルを選択して開きます。

はじめてワークスペースを開いたとき、以下のようなダイアログが表示されるので、 [インストール] ボタンを押下します。

![このリポジトリにお勧めの拡張機能をインストールしますか](readme-images/install-vscode-extensions.png)

このダイアログ経由でインストールしなかった場合は、 [拡張機能] メニューから [推奨] のグループを開いて、以下の拡張機能をインストールします。

- Code Spell Checker
- Draw.io integration
- Markdown All in One
- markdownlint
- vscode-textlint

![拡張機能メニュー](readme-images/recommend-vscode-extensions.png)

また必要に応じて以下の拡張機能をインストールします。

- Japanese Language Pack for Visual Studio Code
- Table Formatter

### Node.js のインストール

以下のサイトから安定版の Node.js インストーラーをダウンロードし、インストールしてください。

<https://nodejs.org/ja/>

### Node モジュールのインストール

本リポジトリでは textlint を使用して、校正を自動化します。
以下のコマンドを実行して、 textlint の実行に必要な Node モジュールをインストールしてください。
モジュールの更新も同じコマンドで実行できます。

```plane
npm install
```

### Pythonのインストール

以下のサイトから、 Windows 用の最新版 Python をインストールします。
インストール時に [Add Python 3.x to PATH] へチェックを入れてからインストールしてください。

<https://www.python.org/downloads/>

コマンドプロンプトを管理者権限で起動します。
以下のコマンドを実行して pip を更新します。

```plain
pip install --upgrade pip
```

### Python モジュールのインストール

本リポジトリでは、 yamllint を使用して、 YAML ファイルの Lint を自動化します。
また、 Mkdocs を用いて、マークダウンから Web サイトを生成します。
以下のコマンドを実行して、必要なモジュールを一括でインストールします。
モジュールの更新も同じコマンドで実行できます。

```plain
pip install -r requirements.txt
```

<!-- textlint-enable @textlint-rule/require-header-id -->
