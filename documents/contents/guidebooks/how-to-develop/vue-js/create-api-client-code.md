---
title: Vue.js 開発手順
description: Vue.js を用いた フロントエンドアプリケーションの 開発手順を説明します。
---

# OpenAPI 仕様書からのクライアントコード生成 {#top}

サーバー側で公開される Web API は、 OpenAPI 仕様書を自動生成しています（詳細は [web プロジェクトの設定](../java/sub-project-settings/web-project-settings.md) を参照）。 Vue.js アプリケーションでは、 OpenAPI Generator を使用して、この OpenAPI 仕様書からクライアントコードを生成します。

## 事前準備 {#preparation}

[OpenAPI 仕様書の出力設定](../java/sub-project-settings/web-project-settings.md#open-api-specification-output-configuration) に示す手順に従って生成した OpenAPI 仕様書をローカルに保存します。ここでは、ファイル名を「api-specification.json」とします。

### JDK のインストール {#install-jdk}

OpenAPI Generator を使用するためには、 Java 11 以降のランタイムと、システム環境変数 JAVA_HOME の設定が必要です。 Oracle JDK や Eclipse Temurin など、適当な JDK をインストールし、 JAVA_HOME を設定してください。

## Axios {#axios}

### Axios のインストール {#install-axios}

```terminal
npm install axios
```

## OpenAPI Generator {#open-api-generator}

### OpenAPI Generator のインストール {#install-open-api-generator}

OpenAPI Generator をインストールします。ターミナルで以下のコマンドを入力します。

<!-- cspell:disable -->

```terminal
npm install -D @openapitools/openapi-generator-cli
```

<!-- cspell:enable -->

### OpenAPI Generator の設定 {#settings-open-api-generator}

package.json の scripts セクションにタスクを追加します。

<!-- cspell:disable -->

```json title="package.json"
{
  "scripts": {
    "generate-client": "openapi-generator-cli batch ./openapisettings.json --clean"
  }
}
```

`--clean` オプションを指定することで、クライアントコードを生成する前に、以前の生成ファイルを削除できます。
これにより、 API 仕様書の変更によって不要になったクライアントコードは自動的に削除されます。

ワークスペースの直下に、設定ファイルを作成します。

```json title="openapisettings.json"
{
  "inputSpec": "./../../dressca-backend/api-docs/web-consumer/api-specification.json",
  "generatorName": "typescript-axios",
  "outputDir": "./src/generated/api-client",
  "additionalProperties": {
    "withSeparateModelsAndApi": "true",
    "modelPackage": "models",
    "apiPackage": "api",
    "supportsES6": "true"
  }
}
```

<!-- cspell:enable -->

設定ファイルの内容について説明します。

| キー                         | 設定値                                                      | 意味                                                |
| ---------------------------- | ----------------------------------------------------------- | --------------------------------------------------- |
| `"inputSpec"`                | `"./../../dressca-backend/api-docs/api-specification.json"` | 入力の API 仕様書を指定します。                     |
| `"generatorName"`            | `"typescript-axios"`                                        | 使用するジェネレーターを指定します。                |
| `"outputDir"`                | `"./src/generated/api-client"`                              | 生成されたコードの出力先を設定します。              |
| `"additionalProperties"`     | -                                                           | 使用するジェネレーターごとに固有の値[^2]をキー・バリュー形式で設定します。|
| `"withSeparateModelsAndApi"` | `"true"`                                                    | model と API を別クラス・別フォルダーに配置します。 |
| `"modelPackage"`             | `"models"`                                                  | クラスのパッケージ名を「models」に設定します。      |
| `"apiPackage"`               | `"api"`                                                     | API クラスのパッケージ名を「api」に設定します。     |
| `"supportsES6"`              | `"true"`                                                    | ES6 に準拠したコードを生成します。                  |

## クライアントコードの生成 {#create-client-code}

ターミナルで以下のコマンドを実行します。

```terminal
npm run generate-client
```

`"outputDir"` に定義した出力先へ、クライアントコードが生成されます。

## クライアントコードの設定 {#set-client-code}

`./src/api-client/index.ts` というファイルを作成し、以下のように設定します。

```typescript title="index.ts"
import axios from 'axios'
import * as apiClient from '@/generated/api-client'

function createConfig(): apiClient.Configuration {
  const config = new apiClient.Configuration({
  })
  return config
}

const axiosInstance = axios.create({})

const defaultApi = new apiClient.DefaultApi(createConfig(), '', axiosInstance)

export { defaultApi }
```

- `apiClient.Configuration` : api-client の共通の Configuration があればここに定義します。プロパティの詳細は [こちら :material-open-in-new:](https://github.com/OpenAPITools/openapi-generator/blob/master/modules/openapi-generator/src/main/resources/typescript-axios/configuration.mustache){ target=_blank }を参照してください。
- `axios.create` : axios インスタンスを生成し、共通の設定をカスタマイズします。詳しくは [公式ドキュメント :material-open-in-new:](https://github.com/axios/axios#request-config){ target=_blank }を参照してください。

このファイルでは、 api-client や axios 共通の設定をします。

1. `src/generated/api-client/api` に自動生成された API を `import` します。
1. 上記の例の `DefaultApi` と同様に `apiClient.XxxApi(createConfig(), '', axiosInstance)` コンストラクターでインスタンスを生成します。
1. 生成したインスタンスを `export` します。

??? info "BaseAPI のコンストラクター"
    - `BaseAPI(configuration?: Configuration, basePath: string, axios: AxiosInstance)`

    `BaseAPI` は OpenAPI Generator で自動生成されるコードの `base.ts` に含まれるクラスです。
    各 API が継承している `BaseAPI` コンストラクターの引数に api-client の共通設定、ベースパス[^1]、 axios インスタンスを設定することで、 API に関するグローバルな設定を適用します。

    OpenAPI Generator で生成されたクライアントコードはデフォルトで OpenAPI 仕様書の URL が設定されます。
    開発環境やモックで API サーバーなしでアプリを起動するためには、アプリレベルでエンドポイントを設定する必要があります。
    Vite では `/api` のような相対パスに対して異なるエンドポイントの設定ができ、これを有効にするためには、 `BaseAPI` コンストラクターの第 2 引数のベースパスを空文字で上書きする必要があります。

    [^1]: ベースパスは `https://www.example.com` のようなリンク先の基準となる URL です。

    ```typescript title="base.ts"
      https://github.com/AlesInfiny/maia/blob/main/samples/web-csr/dressca-frontend/consumer/src/generated/api-client/base.ts#L50-L59
    ```

[^2]: ジェネレーターに `"typescript-axios"` を使用する場合に設定可能な値は [こちら :material-open-in-new:](https://openapi-generator.tech/docs/generators/typescript-axios){ target=_blank }を参照ください。
