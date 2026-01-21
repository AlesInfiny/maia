<!-- textlint-disable @textlint-rule/require-header-id -->
<!-- markdownlint-disable-file CMD001 -->
<!-- cspell:ignore Validatable signupsignin b2clogin -->

# Azure Active Directory B2C による認証サンプル

## このサンプルについて

Azure Active Directory B2C （以降、 Azure AD B2C ）によるユーザー認証の簡単な実装サンプルを提供します。

あわせて本ドキュメントでは、以下について説明します。

- 本サンプルの動作確認手順
- AlesInfiny Maia OSS Edition のサンプルアプリケーションである Dressca への組み込み手順

## 前提

本サンプルを動作させるためには、以下が必要です。

- Azure サブスクリプション
- サブスクリプション内、またはサブスクリプション内のリソース グループ内で共同作成者以上のロールが割り当てられている Azure アカウント

Azure サブスクリプションを持っていない場合、 [無料アカウントを作成](https://azure.microsoft.com/ja-jp/free) できます。

## 動作環境

本サンプルは以下の環境で動作確認を行っています。

- Java 21
- Node.js v24.13.0
- Visual Studio Code 1.108.1

## サンプルの構成

本サンプルは、クライアントブラウザー上で動作するフロントエンドアプリケーション (SPA) と、 SPA が呼び出すバックエンドアプリケーション (Web API) によって構成されています。
フォルダー構成は以下のとおりです。

```text
ルートフォルダー
├ auth-backend ....... バックエンドアプリケーションが配置されたフォルダー
├ auth-frontend ...... フロントエンドアプリケーションが配置されたフォルダー
└ README.md .......... このファイル
```

バックエンドアプリケーションは Spring Boot 、フロントエンドアプリケーションは Vue.js (TypeScript) で作成されています。
また、 AlesInfiny Maia のサンプルアプリケーション Dressca をベースとしており、フォルダー構造、参照する OSS 、名前空間等は Dressca に準拠しています。

## バックエンドアプリケーションの構成

バックエンドアプリケーションを構成するファイルやフォルダーのうち、認証機能に関係があるものを以下に示します。

```text
auth-backend
├ build.gradle .......................................... バックエンドアプリケーション全体で利用するライブラリの依存関係を記載する設定ファイル
├ dependencies.gradle ................................... ライブラリのバージョン管理を行う設定ファイル
└ web
　 ├ src\main
　 |  ├ java\com\dressca\web 
　 |  |  ├ controller
　 |  |  |  ├ dto
　 |  |  |  |  ├ time
　 |  |  |  |  |  └ ServerTimeResponse.java ............. 認証を必要としない現在時刻を取得する Web API の戻り値の型
　 |  |  |  |  ├ auth
　 |  |  |  |  └  └ UserResponse.java ................... 認証を必要とする ユーザー ID を取得する Web API の戻り値の型
　 |  |  |  ├ ServerTimeController.java ................. 認証を必要としない Web API を配置するコントローラー
　 |  |  |  └ UserController.java ....................... 認証を必要とする Web API を配置するコントローラー
　 |  |  ├ controllerAdvice
　 |  |  |  └ ExceptionHandlerControllerAdvice.java ..... 未認証の場合の例外ハンドラを実装するコントローラーアドバイス
　 |  |  ├ security
　 |  |  |  ├ UserIdThreadContextFilter.java ............ JWT Token のユーザー情報を Thread Context に格納するフィルター
　 |  |  |  └ WebSecurityConfiguration.java ............. 認証が必要な Web API を設定し、リクエストヘッダーから認証情報を取得するためのフィルター
　 |  |  └ WebApplication.java .......................... アプリケーションの起動クラス
　 |  └ resources
　 |     └ application.properties ....................... Azure AD B2C への接続情報を記載する設定ファイル
　 └ build.gradle ....................................... web 層で利用するライブラリの依存関係を記載する設定ファイル
```

## フロントエンドアプリケーションの構成

フロントエンドアプリケーションを構成するファイルやフォルダーのうち、認証機能に関係があるものを以下に示します。

```text
auth-frontend
└ app
  ├ .env.dev .............................. Azure AD B2C への接続情報を記載する設定ファイル
  ├ env.d.ts .............................. 環境変数の型定義をする TypeScript ファイル
  └ src
  　 ├ App.vue ............................ 画面。本サンプルでは画面は App.vue のみ。
  　 ├ api-client
  　 │ ├ __tests__
  　 │ │  └ api-client.spec.ts ............ Web API 呼び出しに関するテストを記述する TypeScript ファイル
  　 │ └ index.ts ......................... Web API 呼び出し時の共通処理を記述する TypeScript ファイル
  　 ├ generated .......................... 自動生成された Axios のコードが配置されるフォルダー
  　 ├ services
  　 │  ├ authentication
  　 │  │ ├ authentication-service.ts ..... 認証（サインイン、トークン取得）を行うサービス
  　 │  │ └ authentication-config.ts ...... 上のコードが使用する設定ファイル
  　 │  ├ server-time
  　 │  │ └ server-time-service.ts ........ 認証の必要がない処理を行うサービス
  　 │  └ user
  　 │    └ user-service.ts ............... 認証の必要がある処理を行うサービス
  　 └ stores
  　 　 ├ authentication
  　 　 │ └ authentication.ts ............. 認証の状態を保持するストア
  　 　 ├ server-time
  　 　 │ └ server-time.ts ................ 認証の必要がない Web API 呼び出しの結果を保持するストア
  　 　 └ user
  　 　 　 └ user.ts ...................... 認証が必要な Web API 呼び出しの結果を保持するストア
```

## サンプルのシナリオ

本サンプルは、ユーザー認証が必要な Web API に対し、 Azure AD B2C を利用してその機能を提供します。
本サンプルでは、ユーザー認証が必要な Web API と、認証が不要な Web API の両方を実装しています。
これにより、認証を必要とする Web API を選択して保護できます。
本サンプルのシナリオは以下の通りです。

1. サンプルを起動すると、ブラウザーに SPA のトップ画面が表示されます。
1. 現在時刻を取得する Web API が認証機能なしで呼び出され、トップ画面に表示されます。
1. トップ画面の「 `ログイン` 」をクリックすると、 Azure AD B2C の `サインイン` 画面がポップアップで表示されます。
1. `サインイン` または `サインアップ` が成功すると、ポップアップが閉じます。
1. 成功した認証情報に基づき、ユーザー固有の ID （JWT における sub の値）を取得する Web API が呼び出され、トップ画面に結果が表示されます。
1. トップ画面の「`更新`」をクリックすると、現在時刻を再度取得します。本 Web API は、引き続き認証機能なしで呼び出されます。

※本サンプルでは `サインイン` と `サインアップ` のシナリオのみ提供しており、 `サインアウト` は存在しません。

## サンプルで実現している認証フロー

本サンプルでは、 Microsoft 認証ライブラリ（ MSAL ）の使用によって、 [OAuth 2.0 承認コードフロー with PKCE](https://auth0.com/docs/get-started/authentication-and-authorization-flow/authorization-code-flow-with-pkce) を実現しています。

なお、以下の処理はフロントエンドの [MSAL.js](https://www.npmjs.com/package/@azure/msal-browser) (JavaScript 用 Microsoft Authentication Library) によって行われます。

- code_verifier の生成・送信
- code_challenge の生成・送信

## 前提となる OSS ライブラリ

本サンプルでは、バックエンド、フロントエンドアプリケーションそれぞれで OSS を使用しています。

- バックエンドアプリケーション
    - [spring-cloud-azure-starter](https://central.sonatype.com/artifact/com.azure.spring/spring-cloud-azure-starter)
    - [spring-cloud-azure-starter-active-directory-b2c](https://central.sonatype.com/artifact/com.azure.spring/spring-cloud-azure-starter-active-directory-b2c)
    - [spring-cloud-azure-dependencies](https://central.sonatype.com/artifact/com.azure.spring/spring-cloud-azure-dependencies)
- フロントエンドアプリケーション
    - [MSAL.js](https://www.npmjs.com/package/@azure/msal-browser)

その他の使用 OSS は、 AlesInfiny Maia のサンプルアプリケーションに準じます。

## サンプルの動作方法

本サンプルをローカルマシンで動作させるには、事前に Azure AD B2C のテナントを作成し、アプリケーションを登録する作業が必要です。

### Azure AD B2C テナントの作成

1. [Microsoft のチュートリアル「 Azure AD B2C テナントを作成する」](https://learn.microsoft.com/ja-jp/azure/active-directory-b2c/tutorial-create-tenant#create-an-azure-ad-b2c-tenant) に従って、 [Azure ポータル](https://portal.azure.com/) にサインインし、 Azure AD B2C テナントを作成します。
   - 「`初期ドメイン名`」をメモします。
1. [Microsoft のチュートリアル「 B2C テナント ディレクトリを選択する」](https://learn.microsoft.com/ja-jp/azure/active-directory-b2c/tutorial-create-tenant#select-your-b2c-tenant-directory) に従って、 B2C テナントディレクトリに切り替えます。
1. [Microsoft のチュートリアル「 Azure AD B2C をお気に入りとして追加する (省略可能)」](https://learn.microsoft.com/ja-jp/azure/active-directory-b2c/tutorial-create-tenant#add-azure-ad-b2c-as-a-favorite-optional) に従って、 Azure ポータル上で「 Azure サービス」から「 Azure AD B2C 」を選択しお気に入りに登録します。

### Azure AD B2C テナントを利用するアプリの登録（バックエンドアプリケーション）

1. [Microsoft のチュートリアル「 Azure Active Directory B2C テナントに Web API アプリケーションを追加する」](https://learn.microsoft.com/ja-jp/azure/active-directory-b2c/add-web-api-application?tabs=app-reg-ga) に従い、バックエンドアプリケーションを Azure AD B2C に登録します。
   - 登録したアプリの名前を、ここでは「 `SampleWebAPI` 」とします。
   - 登録したアプリの `クライアント ID` （アプリケーション ID ）をメモします。
1. [Microsoft のチュートリアル「スコープを構成する」](https://learn.microsoft.com/ja-jp/azure/active-directory-b2c/add-web-api-application?tabs=app-reg-ga#configure-scopes) に従って、アプリにスコープを追加します。
   - チュートリアルの手順では読み取りと書き込み 2 つのスコープを作成していますが、本サンプルのシナリオでは作成するスコープは 1 つで良いです。
   - 追加したスコープの名前を、ここでは「 `api.read` 」とします。
1. Azure ポータルのお気に入りから「 Azure AD B2C 」を選択します。
1. 「アプリの登録」ブレードを選択し、「すべてのアプリケーション」から「 SampleWebAPI 」を選択します。
1. 「概要」ブレードに表示された「 `アプリケーション ID の URI` 」をメモします。

### Azure AD B2C テナントを利用するアプリの登録（フロントエンドアプリケーション）

1. [Microsoft のチュートリアル「 SPA アプリケーションの登録」](https://learn.microsoft.com/ja-jp/azure/active-directory-b2c/tutorial-register-spa#register-the-spa-application) に従って、フロントエンドアプリケーションを Azure AD B2C に登録します。
   - 登録したアプリの名前を、ここでは「 `SampleSPA` 」とします。
   - 登録したアプリの `クライアント ID` （アプリケーション ID ）をメモします。
   - 「暗黙的フロー」に関する設定は無視してください。
1. Azure ポータルのお気に入りから「 Azure AD B2C 」を選択します。
1. 「アプリの登録」ブレードを選択し、「すべてのアプリケーション」から「 SampleSPA 」を選択します。
1. 「認証」ブレードを選択し、「シングルページアプリケーション」の「リダイレクト URI」に `http://localhost` を追加します。
1. [Microsoft のチュートリアル「[アクセス許可の付与]」](https://learn.microsoft.com/ja-jp/azure/active-directory-b2c/add-web-api-application?tabs=app-reg-ga#grant-permissions) に従い、 SampleSPA に、前の手順で追加した SampleWebAPI のスコープ「 api.read 」へのアクセス許可を付与します。

### ユーザーフローの作成

1. [Microsoft のチュートリアル「Azure Active Directory B2C でサインアップおよびサインイン フローを設定する」](https://learn.microsoft.com/ja-jp/azure/active-directory-b2c/add-sign-up-and-sign-in-policy?pivots=b2c-user-flow) に従って、`サインアップとサインイン`ユーザーフローを作成します。
   - ここでは追加した`サインアップとサインイン`ユーザーフローの名前を「 `signupsignin1` 」とします（ユーザーフローの名前には自動的に『`B2C_1_`』プレフィックスが付与されます）。

### 設定情報の記入

#### バックエンドアプリケーションの設定

1. VS Code で `auth-backend\web\src\main\resources\application.properties` を開きます。
1. 以下のように設定情報を記入します（以下の例では Azure AD B2C の設定以外は省略しています）。

    ```properties
    spring.cloud.azure.active-directory.b2c.enabled=true
    spring.cloud.azure.active-directory.b2c.base-uri=http://[初期ドメイン名].b2clogin.com/[初期ドメイン名].onmicrosoft.com/
    spring.cloud.azure.active-directory.b2c.credential.client-id=[SampleWebAPI のクライアント ID]
    spring.cloud.azure.active-directory.b2c.profile.tenant-id=[SampleWebAPI のテナント ID]
    spring.cloud.azure.active-directory.b2c.user-flows.sign-up-or-sign-in=B2C_1_[追加した「サインアップとサインインのユーザーフローの名前」。本サンプルの既定では signupsignin1]
    cors.allowed.origins=[フロントエンドアプリケーションのベースとなるURL。本サンプルの既定では http://localhost:5173]
    ```

#### フロントエンドアプリケーションの設定

1. `auth-frontend\.env.dev` を開きます。
1. 以下のように設定情報を記入します（以下の例では Azure AD B2C の設定以外は省略しています）。

    ```properties
    VITE_ADB2C_USER_FLOW_SIGN_IN=[B2C_1_[『サインアップとサインイン』のユーザフロー名]。 本サンプルでは B2C_1_signupsignin1]
    VITE_ADB2C_SIGN_IN_URI=https://[初期ドメイン名].b2clogin.com/[初期ドメイン名].onmicrosoft.com/B2C_1_[『サインアップとサインイン』のユーザフロー名]
    VITE_ADB2C_AUTHORITY_DOMAIN=[初期ドメイン名].b2clogin.com
    VITE_ADB2C_SCOPE=[APIの公開で設定したApplication ID URI]/[Web APIに追加したスコープの名前]
    VITE_ADB2C_APP_CLIENT_ID=[クライアントアプリケーションのクライアントID]
    VITE_ADB2C_APP_URI=[クライアントアプリケーションのURI。本サンプルの既定では http://localhost:5173]
    ```

### 動作確認

1. VS Code で `auth-backend` のフォルダーへ移動し、ターミナルで以下を実行します。

    ```shell
    ./gradlew build
    ./gradlew web:bootRunDev
    ```

1. VS Code で `auth-frontend` のフォルダーの `auth-frontend.code-workspace` ファイルを開き、ターミナルで以下を実行します。

    ```shell
    npm ci
    npm run dev:app
    ```

1. ブラウザーを開き、以下のアドレスにアクセスします。
    - <http://localhost:5173>
1. 画面の「 `ログイン` 」をクリックします。 Azure AD B2C の `サインイン` 画面がポップアップで表示されます。
1. 「 Sign up now 」リンクをクリックします。
1. 使用可能なメールアドレスを入力し、「 Send verification code 」をクリックします。
1. 上の手順で入力したメールアドレス宛に Verification code が送信されるので、画面に入力して「 Verify code 」をクリックします。
1. 画面に新しいパスワード等の必要事項を入力し、「 Create 」をクリックします。
1. `サインイン` が成功し、画面に「ユーザー ID 」が表示されれば成功です。以降は入力したメールアドレスとパスワードで `サインイン` できるようになります。

Azure AD B2C に追加したユーザーは、以下の手順で削除できます。

1. Azure ポータルのお気に入りから「 Azure AD B2C 」を選択します。
1. 「ユーザー」ブレードを選択します。
1. 対象のユーザーをチェックし、画面上部から「削除」を選択します。

## アプリケーションへの認証機能の組み込み

本サンプルのコードを既存のアプリケーションへコピーすることで、 Azure AD B2C の認証機能を組み込むことができます。
なお、対象のアプリケーションは AlesInfiny Maia のクライアントサイドレンダリングアプリケーションです。

### バックエンドアプリケーション

1. [バックエンドアプリケーションの設定](#バックエンドアプリケーションの設定) を参照し、 `application.properties` を設定、ライブラリを追加します。
1. `dependencies.gradle`を開きます。
1. 以下のように OSS ライブラリの依存関係を記入します（以下の例では Azure AD B2C の設定以外は省略しています）。

    ```gradle
    ext {
      springCloudAzureVersion = "[使用するライブラリのバージョン番号を記述。サンプルでは 5.22.0]"

      supportDependencies = [
        spring_cloud_azure_starter : "com.azure.spring:spring-cloud-azure-starter",
        spring_cloud_azure_starter_ad_b2c : "com.azure.spring:spring-cloud-azure-starter-active-directory-b2c",
        spring_cloud_azure_dependencies : "com.azure.spring:spring-cloud-azure-dependencies:$springCloudAzureVersion",
      ]
    }
    ```

1. `\web\build.gradle`を開きます。
1. 以下のように OSS ライブラリの依存関係を記入します（以下の例では Azure AD B2C の設定以外は省略しています）。

    ```gradle
    dependencies {
      implementation supportDependencies.spring_cloud_azure_starter
      implementation supportDependencies.spring_cloud_azure_starter_ad_b2c
    }

    dependencyManagement {
      imports {
        mavenBom supportDependencies.spring_cloud_azure_dependencies
      }
    }
    ```

1. `\web\src\main\java\com\[プロジェクト名]\web\security` フォルダーを作成し、サンプルの以下のコードをコピーします。
   - UserIdTHreadContextFilter.java
   - WebSecurityConfiguration.java
1. 認証を必要とするコントローラークラスで、 認証が必要であることを表すアノテーションを付与します。

    ```java
    @RestController
    public class ExampleController {
      // 認証が必要な Web API に対し、PreAuthorizeアノテーションを付与
      @PreAuthorize(value = "isAuthenticated()")
      public ResponseEntity<ExampleResponse> get() throws Exception {
        // 以下に、コントローラーの詳細を実装
      }
    }
    ```

1. 未認証の場合の例外ハンドラを実装します。

    ```java
    @ControllerAdvice
    public class ExceptionHandlerControllerAdvice extends ResponseEntityExceptionHandler {
      // 未認証の場合に発生する AccessDeniedException に対し、例外ハンドラを指定
      @ExceptionHandler(AccessDeniedException.class)
      public ResponseEntity<?> accessDeniedHandleException(AccessDeniedException e, HttpServletRequest req) {
        // 以下に、例外ハンドラの詳細を実装
      }
    }
    ```

1. バックエンドアプリケーションをビルドします。

### フロントエンドアプリケーション

1. VS Code で `auth-frontend` のフォルダーの `auth-frontend.code-workspace` ファイルを開きます。
1. ターミナルで `npm install @azure/msal-browser` を実行し、フロントエンドアプリケーションに MSAL.js をインストールします。
1. `auth-frontend\.env.dev` に記述した Azure AD B2C の設定をフロントエンドアプリケーションの `.env.dev` にコピーします。
1. `env.d.ts` のインターフェースに、前の手順で `.env.dev` に追加したプロパティを追加します。

    ```typescript
    interface ImportMetaEnv {
      // 認証に関係のないプロパティは省略
      readonly VITE_ADB2C_USER_FLOW_SIGN_IN: string;
      readonly VITE_ADB2C_AUTHORITY_DOMAIN: string;
      readonly VITE_ADB2C_SCOPE: string;
      readonly VITE_ADB2C_APP_CLIENT_ID: string;
      readonly VITE_ADB2C_APP_URI: string;
    }
    ```

1. `npm run generate-client` を実行し、 Axios のクライアントコードを再生成します。
1. `src\services\authentication` フォルダーを作成し、サンプルの以下のコードをコピーします。
    - authentication-services.ts
    - authentication-config.ts
1. `src\store\authentication` フォルダーを作成し、サンプルの以下のコードをコピーします。
    - authentication.ts
1. 認証が成功したら、認証が必要な Web API リクエストヘッダーに Bearer トークンを付与する必要があります。
    AlesInfiny Maia のサンプルアプリケーション Dressca の場合、 `src\api-client\index.ts` を編集します。

    ```typescript
    import axios from "axios";
    import * as apiClient from "@/generated/api-client";
    import { authenticationService } from '@/services/authentication/authentication-service';

    // その他のコードは省略

    /** api-client の共通の Configuration があればここに定義します。 */
    function createConfig(): apiClient.Configuration {
      const config = new apiClient.Configuration({
        basePath: import.meta.env.VITE_AXIOS_BASE_ENDPOINT_ORIGIN,
      });

      return config;
    }

    async function addTokenAsync(config: apiClient.Configuration) {
      

      // 認証済みの場合、アクセストークンを取得して Configuration に設定します。
      if (await authenticationService.isAuthenticated()) {
        const token = await authenticationService.getTokenAzureADB2C();
        config.accessToken = token;
      }
    }

    export async function getExampleApi(): Promise<apiClient.ExampleApi> {
      const config = createConfig();

      // 認証が必要な API では、addTokenAsync を呼び出します。
      await addTokenAsync(config);
      const exampleApi = new apiClient.ExampleApi(config, '', axiosInstance);
      return exampleApi;
    }

    export async function getServerTimeApi(): Promise<apiClient.ServerTimeApi> {
      const config = createConfig();

      // 認証が不要な API では、addTokenAsync は呼び出しません。
      const serverTimeApi = new apiClient.ServerTimeApi(
        config,
        '',
        axiosInstance
      );
      return serverTimeApi;
    }
    ```

1. `ログイン` 画面へのリンクを含む Vue ファイルの `<script>` セクションにコードを追加します。

    ```vue
    <script setup lang="ts">
    import { authenticationService } from '@/services/authentication/authentication-service';
    import { useAuthenticationStore } from '@/stores/authentication/authentication';
    const authenticationStore = useAuthenticationStore();

    const signIn = async () => {
      await authenticationService.signInAzureADB2C();
    };
    </script>
    ```

1. `ログイン` 画面へのリンクを以下のように記述します（クリック時に `signIn` メソッドが動作すれば `button` である必要はありません）。

    ```vue
    <button v-if="!authenticationStore.isAuthenticated" @click="signIn()">ログイン</button>
    ```

1. `npm install` を実行し、その他のパッケージをインストールします。

## 参照記事

本サンプルは、以下の記事に基づき作成しました。

### フロントエンドアプリケーションの参照記事

- [Azure AD B2C を利用した SPA アプリケーションサンプル](https://github.com/Azure-Samples/ms-identity-b2c-javascript-spa/tree/main)

### バックエンドアプリケーションの参照記事

- [Spring Security における SecurityFilterChain のアーキテクチャー](https://spring.pleiades.io/spring-security/reference/servlet/architecture.html)
- [Spring Security の Spring Cloud Azure サポート](https://learn.microsoft.com/ja-jp/azure/developer/java/spring-framework/spring-security-support?tabs=SpringCloudAzure5x)
