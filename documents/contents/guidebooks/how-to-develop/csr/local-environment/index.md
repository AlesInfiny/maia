---
title: 開発環境構築 （ CSR 編）
description: AlesInfiny Maia OSS Edition の CSR アプリケーション開発で 最低限必要な環境の構築方法を解説します。
---

# ローカル開発環境の構築 {#top}

AlesInfiny Maia OSS Edition （以降、 AlesInfiny Maia）のアプリケーション開発で最低限必要な環境の構築方法を解説します。

## システム要件 {#system-requirements}

AlesInfiny Maia のアプリケーションを開発するコンピューターが満たすべき要件について解説します。
ここに記載のない環境でも開発できることがありますが、動作確認は行われていません。

### OS 要件 {#os-requirements}

- Windows 11

原則としてサポートされている最新の Windows クライアント OS を対象とします。

### ハードウェア要件 {#hardware-requirements}

- 8 GB 以上の RAM が必要です。 16 GB 以上を推奨します。
- 20 GB 以上の空き容量を持つ SSD が必要です。

### ネットワーク要件 {#network-requirements}

- インターネット接続が必要です。

### ソフトウェア要件 {#software-requirements}

AlesInfiny Maia のアプリケーション開発には、 Visual Studio Code（以下 VS Code） の利用を推奨します。
VS Code では、 Web API アプリケーションやバッチアプリケーションなどの Java アプリケーションと、 Vue.js を含む SPA アプリケーションの両方を開発できます。
Java アプリケーションの開発のみを行う場合には Eclipse や IntelliJ IDEA といった IDE も利用可能です。
また、 IntelliJ IDEA で Vue.js の開発をする場合は有償版（IntelliJ IDEA Ultimate）の利用が推奨されます。

| 開発するアプリケーション | VS Code | Eclipse | IntelliJ IDEA |
| ------------------------ | :-----: | :-----: | :-----------: |
| Vue.js アプリケーション  |   〇    |    ×    |       △       |
| Java アプリケーション    |   〇    |   〇    |      〇       |

## ローカル開発環境の構築手順 {#setup-development-environment}

本節では開発に最低限必要なソフトウェアのインストール方法について解説します。
AlesInfiny Maia の各ドキュメントは、本節に記載されている環境が整っていることを前提に記載されています。

!!! info "Windows Defender ファイアウォールの警告が表示される場合"
    後述の各種ソフトウェアをインストールして起動した際、以下のような警告の出ることがあります。

    > このアプリのいくつかが Windows Defender ファイアウォールでブロックされています。
    
    この警告が出た場合には、ご利用の環境に合わせて通信の許可設定を行ってください。

### Visual Studio Code のインストール {#install-vscode}

1. [こちらのサイト :material-open-in-new:](https://code.visualstudio.com/){ target=_blank } から、コンピューターの環境にあった Visual Studio Code(以下 VS Code) のインストーラーをダウンロードします。

1. ダウンロードしたインストーラーを実行します。

1. オプション設定は、ご利用の環境に応じて設定してインストールしてください。

1. インストールが完了したら VS Code を起動します。

1. 以下の拡張機能をインストールします。

    - [Japanese Language Pack for Visual Studio Code :material-open-in-new:](https://marketplace.visualstudio.com/items?itemName=MS-CEINTL.vscode-language-pack-ja){ target=_blank }

        VS Code のユーザーインターフェースを日本語にローカライズする機能を提供します。

    - [EditorConfig for VS Code :material-open-in-new:](https://marketplace.visualstudio.com/items?itemName=EditorConfig.EditorConfig){ target=_blank }

        アプリケーションのコーディングスタイルを維持する機能を提供します。

    バックエンドアプリケーションやフロントエンドアプリケーションの開発に利用する VS Code の拡張機能は、以下を確認してください。

    - [バックエンドアプリケーション開発の事前準備](../java/preparation.md#install-extensions)
    - [フロントエンドアプリケーション開発の事前準備](../vue-js/preparation.md#install-extensions)

### Git for Windows のインストール {#install-git-for-windows}

!!! warning "Git for Windows インストールの前に"
    [VS Code のインストール](#install-vscode) を完了させておくことを推奨します。
    Git for Windows のインストール中に、既定のエディターを選択する必要があります。
    VS Code をインストールしている場合、 VS Code を既定のエディターとして設定できます。

1. [こちらのサイト :material-open-in-new:](https://git-scm.com/){ target=_blank } から、コンピューターの環境にあった Git for Windows のインストーラーをダウンロードします。

1. ダウンロードしたインストーラーを実行します。

1. オプション設定は、ご利用の環境に応じて設定してインストールしてください。

1. 以下のコマンドが実行できればインストールは完了です。

    ```shell title="Git for Windows のバージョン確認"
    git --version
    ```

### JDK のインストール {#install-jdk}

JDK の取得先は実行環境に合わせて選択します。

- [Azure：Microsoft Build for OpenJDK :material-open-in-new:](https://www.microsoft.com/openjdk/){ target=_blank }

- [AWS：Amazon Corretto :material-open-in-new:](https://aws.amazon.com/jp/corretto/){ target=_blank }

- オンプレミス/その他クラウドサービス（下記のいずれか）

    - [Temurin ( Adoptium ) :material-open-in-new:](https://adoptium.net/){ target=_blank }
    - [Oracle JDK :material-open-in-new:](https://www.oracle.com/java/){ target=_blank }

本ドキュメントは Temurin を前提に記載しています。

1. 上記のサイトから選択した JDK を取得します。

1. msi 形式のインストーラーを取得した場合はインストーラーを実行します。 zip 形式で取得した場合には任意のパスに解凍して配置します。

1. 環境変数 JAVA_HOME にインストールした JDK のパスを設定します。
JDK のインストール時のカスタムセットアップで設定済みであれば不要です。

### Node.js のインストール {#install-node}

1. [こちらのサイト :material-open-in-new:](https://nodejs.org/ja/){ target=_blank } からインストーラーを取得します。 Node.js のインストーラーは、原則最新の LTS 版を利用してください。

1. インストーラーを実行します。カスタムセットアップにて、 npm のインストールと PATH の追加をするよう設定することを推奨します ( 既定値のままインストールすると npm のインストールと PATH の設定が行われます ) 。

1. 以下のコマンドが実行できればインストールは完了です。

    ```shell title="Node.js と npm のバージョン確認"
    node --version
    npm --version
    ```
