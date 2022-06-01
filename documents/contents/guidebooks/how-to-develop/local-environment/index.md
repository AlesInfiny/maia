# ローカル開発環境の構築手順

Maia OSS 版のアプリケーション開発で最低限必要な環境の構築方法を解説します。

## システム要件 ## {: #system-requirements }

Maia OSS 版のアプリケーション開発を行うコンピューターが満たすべき要件について解説します。
ここに記載のない環境でも開発できることがありますが、動作確認は行われていません。

### OS 要件 ### {: #os-requirements }

- Windows 10
- Windows 11

原則としてサポートされている最新の Windows クライアント OS を対象とします。

### ハードウェア要件 ### {: #hardware-requirements }

- 8 GB 以上の RAM が必要です。 16 GB 以上を推奨します。
- 20 GB 以上の空き容量を持つ SSD が必要です。

### ネットワーク要件 ### {: #network-requirements }

- インターネット接続が必要です。

### ソフトウェア要件 ### {: #software-requirements }

Maia OSS 版のアプリケーション開発には、
Web API アプリケーションやバッチアプリケーションなどの Java アプリケーションと、
Vue.js を含む SPA アプリケーションの両方の開発が行うことができる Visual Studio Code（以下 VS Code） を推奨します。
Java アプリケーションの開発のみを行う場合には Eclipe や IntelliJ といった IDE も利用可能です。また、IntelliJ でVue.js の開発を行う場合は有償版（IntelliJ IDEA Ultimate）の利用が推奨されます。

| 開発するアプリケーション | VS Code | Eclipse | IntelliJ IDEA |
| ----------------------- | ------- | ------- | ---|
| Vue.js アプリケーション  | 〇  | ×  | △ |
| Java アプリケーション    | 〇  | 〇  | 〇 |

## ローカル開発環境の構築手順 ## {: #setup-development-environment }

本節では開発に最低限必要なソフトウェアのインストール方法について解説します。
Maia OSS 版の各ドキュメントは、本節に記載されている環境が整っていることを前提に記載されています。

!!! info "Windows Defender ファイアウォールの警告が表示される場合"
    後述の各種ソフトウェアをインストールして起動した際に、
    「このアプリのいくつかが Windows Defender ファイアウォールでブロックされています」という警告が出た場合には、ご利用の環境に合わせて通信の許可設定を行ってください。

### Visual Studio Code のインストール ## {: #install-vscode }

1. 以下のサイトから、コンピューターの環境にあった Visual Studio Code(以下VS Code) のインストーラーをダウンロードします。

    <https://code.visualstudio.com/>

1. ダウンロードしたインストーラーを実行します。

1. オプション設定は、ご利用の環境に応じて設定してインストールしてください。

1. インストールが完了したら VS Code を起動します。

1. 以下の拡張機能をインストールします。

    [Japanese Language Pack for Visual Studio Code](https://marketplace.visualstudio.com/items?itemName=MS-CEINTL.vscode-language-pack-ja)

### Git for Windows のインストール ## {: #install-git-for-windows }

!!!warning "Git for Windows インストールの前に"
    [VS Code のインストール](#install-vscode) を完了させておくことを推奨します。
    Git for Windows のインストール中に、既定のエディターを選択する必要があります。
    VS Code をインストールしている場合、 VS Code を既定のエディターとして設定できます。

1. 以下のサイトから、コンピューターの環境にあった Git for Windows のインストーラーをダウンロードします。

    <https://git-scm.com/>

1. ダウンロードしたインストーラーを実行します。

1. オプション設定は、ご利用の環境に応じて設定してインストールしてください。

1. 以下のコマンドが実行できればインストールは完了です。

    ```ps1 title="Git for Windows のバージョン確認"
    git --version
    ```

### JDK のインストール ## {: #install-jdk }

JDKの取得先は実行環境に合わせて選択します。

- Azure：Microsoft Build for OpenJDK

    <https://www.microsoft.com/openjdk/>

- AWS：Amazon Corretto

    <https://aws.amazon.com/jp/corretto/>

- オンプレミス/その他クラウドサービス（下記のいずれか）

    - Adoptium

        <https://adoptium.net/>

    - Oracle JDK

        <https://www.oracle.com/java/>

本ドキュメントは Adoptium OpenJDK を前提に記載しています。

1. 上記のサイトから選択したJDKを取得します。

1. msi形式のインストーラを取得した場合はインストーラを実行します。zip形式で取得した場合には任意のパスに解凍して配置します。

1. 環境変数 JAVA_HOME にインストールしたJDKのパスを設定します。
JDKのインストール時のカスタムセットアップで設定済みであれば不要です。

### Node.js のインストール ## {: #install-node}

1. 以下のサイトからインストーラを取得します。

    <https://nodejs.org/ja/>

1. インストーラを実行します。カスタムセットアップにて、npm のインストールと PATH の設定を実施するよう設定することを推奨します ( 既定値のままインストールすると npm のインストールと PATH の設定が行われます )

--8<-- "includes/abbreviations.md"
