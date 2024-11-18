---
title: Vue.js 開発手順
description: Vue.js を用いた クライアントサイドアプリケーションの 開発手順を説明します。
---

# Open API 仕様書からのクライアントコード生成 {#top}

サーバー側で公開される Web API は、 Open API 仕様書を自動生成しています（詳細は [サブプロジェクトの個別設定](../java/sub-project-settings.md) を参照）。 Vue.js アプリケーションでは、 Open API Generator を使用して、この Open API 仕様書からクライアントコードを生成します。

## 事前準備 {#preparation}

[Open API 仕様書の出力設定](../java/sub-project-settings.md#open-api-specification-output-configuration) に示す手順に従って生成した Open API 仕様書をローカルに保存します。ここでは、ファイル名を「api-specification.json」とします。

### JDK のインストール {#install-jdk}

Open API Generator を使用するためには、 Java 8 以降のランタイムと、システム環境変数 JAVA_HOME の設定が必要です。 Oracle JDK や Eclipse Temurin など、適当な JDK をインストールし、 JAVA_HOME を設定してください。

## Axios {#axios}

### Axios のインストール {#install-axios}

```terminal
npm install axios
```

## Open API Generator {#open-api-generator}

### Open API Generator のインストール {#install-open-api-generator}

Open API Generator をインストールします。ターミナルで以下のコマンドを入力します。

<!-- cspell:disable -->

```terminal
npm install -D @openapitools/openapi-generator-cli
```

<!-- cspell:enable -->

### Open API Generator の設定 {#settings-open-api-generator}

package.json の scripts セクションにタスクを追加します。

??? note "タスクを登録する際の注意点"
    `package.json`にタスクを登録する際には、実行環境の OS に依存するコマンドの使用を避けるように注意する必要があります。
    例えば、フォルダーを削除したい場合に`rmdir`コマンドを使用すると Windows 環境に依存してしまい、`rm`コマンドを使用すると UNIX 環境に依存してしまいます。
    ここで開発環境が Windows であり、 CI 環境が UNIX の場合には、開発環境では実行できていたコマンドが CI 環境では実行できないといった問題が発生します。
    そのため、 タスク中で OS コマンドを使用したい場合には、可能であれば [Node.jsのAPI :material-open-in-new:](https://nodejs.org/api/documentation.html){ target=_blank } で代替するほうがベターです。

<!-- cspell:disable -->

```json title="package.json"
{
  "scripts": {
    "generate-client": "run-s openapi-client:clean openapi-client:generate --print-label",
    "openapi-client:clean": "node -e \"fs.promises.rm('./src/generated/api-client', {recursive: true, force: true})\"",
    "openapi-client:generate": "openapi-generator-cli generate -g typescript-axios -i ./../../dressca-backend/api-docs/api-specification.json --additional-properties=withSeparateModelsAndApi=true,modelPackage=models,apiPackage=api,supportsES6=true -o ./src/generated/api-client"
  }
}
```

<!-- cspell:enable -->

openapi-generator-cli の generate コマンドのオプションについて説明します。

ジェネレーターとして typescript-axios を指定します。

``` terminal
-g typescript-axios
```

入力の API 仕様書として `./../../dressca-backend/api-docs/api-specification.json` というファイルを指定します。

``` terminal
-i ./../../dressca-backend/api-docs/api-specification.json 
```

以下のプロパティを追加します。

- `withSeparateModelsAndApi=true` ：model と API を別クラス・別フォルダーに配置する
- `modelPackage=models：model` ：クラスのパッケージ名を「models」に設定する
- `apiPackage=api` ：API クラスのパッケージ名を「api」に設定する
- `supportsES6=true` ：ES6 に準拠したコードを生成する

``` terminal
--additional-properties=withSeparateModelsAndApi=true,modelPackage=models,apiPackage=api,supportsES6=true
```

生成されたコードの出力先を `./src/generated/api-client` に設定します。

``` terminal
-o ./src/generated/api-client
```

## クライアントコードの生成 {#create-client-code}

ターミナルで以下のコマンドを実行します。

```terminal
npm run generate-client
```

オプション ` -o ` に定義した出力先へ、クライアントコードが生成されます。

!!! info "クライアントコードの削除と再生成"
    openapi-generator-cli の generate コマンドでは、 Open API 仕様書の変更によって不要になった既存のクライアントコードは自動で削除されません。
    そのため、既存のクライアントコードを一度削除してからクライアントコードを生成するように設定しています。

## クライアントコードの設定 {#set-client-code}

`./src/api-client/index.ts` というファイルを作成し、以下のように設定します。

```typescript title="index.ts"
import axios from 'axios';
import * as apiClient from '@/generated/api-client';

function createConfig(): apiClient.Configuration {
  const config = new apiClient.Configuration({
  });
  return config;
}

const axiosInstance = axios.create({});

const defaultApi = new apiClient.DefaultApi(createConfig(), '', axiosInstance);

export { defaultApi };
```

- `apiClient.Configuration` : api-client の共通の Configuration があればここに定義します。プロパティの詳細は [こちら :material-open-in-new:](https://github.com/OpenAPITools/openapi-generator/blob/master/modules/openapi-generator/src/main/resources/typescript-axios/configuration.mustache){ target=_blank }を参照してください。
- `axios.create` : axios インスタンスを生成し、共通の設定をカスタマイズします。詳しくは [公式ドキュメント :material-open-in-new:](https://github.com/axios/axios#request-config){ target=_blank }を参照してください。

このファイルでは、 api-client や axios 共通の設定をします。

1. `src/generated/api-client/api` に自動生成された API を `import` します。
1. 上記の例の `DefaultApi` と同様に `apiClient.XxxApi(createConfig(), '', axiosInstance)` コンストラクターでインスタンスを生成します。
1. 生成したインスタンスを `export` します。

??? info "BaseAPI のコンストラクター"
    - `BaseAPI(configuration?: Configuration, basePath?: string, axios?: AxiosInstance)`
  
    `BaseAPI` は OpenAPI Generator で自動生成されるコードの `base.ts` に含まれるクラスです。
    各 API が継承している `BaseAPI` コンストラクターの引数に api-client の共通設定、ベースパス[^1]、 axios インスタンスを設定することで、 API に関するグローバルな設定を適用します。
    
    OpenAPI Generator で生成されたクライアントコードはデフォルトで Open API 仕様書の URL が設定されます。
    開発環境やモックで API サーバーなしでアプリを起動するためには、アプリレベルでエンドポイントを設定する必要があります。
    Vite では `/api` のような相対パスに対して異なるエンドポイントの設定ができ、これを有効にするためには、 `BaseAPI` コンストラクターの第 2 引数のベースパスを空文字で上書きする必要があります。

    [^1]: ベースパスは `https://www.example.com` のようなリンク先の基準となる URL です。

    ```typescript title="base.ts"
    export class BaseAPI {
      protected configuration: Configuration | undefined;

      constructor(configuration?: Configuration, protected basePath: string = BASE_PATH, protected axios: AxiosInstance = globalAxios) {
        if (configuration) {
            this.configuration = configuration;
            this.basePath = configuration.basePath || this.basePath;
        }
      }
    };
    ```
