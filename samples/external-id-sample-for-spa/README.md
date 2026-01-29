<!-- textlint-disable @textlint-rule/require-header-id -->
<!-- markdownlint-disable-file CMD001 -->
<!-- cspell:ignore Validatable signupsignin -->

# Microsoft Entra External ID による認証サンプル

## このサンプルについて

本サンプルは、クライアントサイドレンダリングのシングルページアプリケーション（SPA）において、 Microsoft Entra External ID を利用したユーザー認証を実装するためのコード例を提供します。

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

### バックエンドアプリケーションの構成

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
　 |     └ application.properties ....................... Entra External ID への接続情報を記載する設定ファイル
　 └ build.gradle ....................................... web 層で利用するライブラリの依存関係を記載する設定ファイル
```

### フロントエンドアプリケーションの構成

フロントエンドアプリケーションを構成するファイルやフォルダーのうち、認証機能に関係があるものを以下に示します。

```text
auth-frontend
└ app
  ├ .env.dev .............................. Entra External ID への接続情報を記載する設定ファイル
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

本サンプルは、ユーザー認証が必要な Web API に対し、 Entra External ID を利用してその機能を提供します。
本サンプルでは、ユーザー認証が必要な Web API と、認証が不要な Web API の両方を実装しています。
これにより、認証を必要とする Web API を選択して保護できます。
本サンプルのシナリオは以下の通りです。

1. サンプルを起動すると、ブラウザーに SPA のトップ画面が表示されます。
1. 現在時刻を取得する Web API が認証情報なしで正常に呼び出され、トップ画面に表示されます。
1. ユーザー固有の ID （JWT における sub の値）を取得する Web API が認証情報なしで呼び出され、未認証によるエラーのアラートが表示されます。
1. トップ画面の「 `ログイン` 」をクリックすると、 Entra External ID の `サインイン` 画面がポップアップで表示されます。
1. `サインイン` または `サインアップ` が成功すると、ポップアップが閉じます。
1. 成功した認証情報に基づき、再度ユーザー固有の ID （JWT における sub の値）を取得する Web API が呼び出され、トップ画面に結果が表示されます。
1. トップ画面の「`更新`」をクリックすると、現在時刻を再度取得します。本 Web API は、引き続き認証機能なしで呼び出されます。
1. トップ画面の「`ログアウト`」をクリックすると、 Entra External ID の `サインアウト` 画面がポップアップで表示されます。
1. `サインアウト` が成功すると、ポップアップが閉じます。

## サンプルで実現している認証フロー

本サンプルでは、 Microsoft 認証ライブラリ（ MSAL ）の使用によって、 [OAuth 2.0 承認コードフロー with PKCE](https://auth0.com/docs/get-started/authentication-and-authorization-flow/authorization-code-flow-with-pkce) を実現しています。

なお、以下の処理はフロントエンドの MSAL.js (JavaScript 用 Microsoft Authentication Library) によって行われます。

- code_verifier の生成・送信
- code_challenge の生成・送信

## 前提となる OSS ライブラリ

本サンプルでは、バックエンド、フロントエンドアプリケーションそれぞれで OSS を使用しています。

1. バックエンドアプリケーション
    - [spring-boot-starter-oauth2-resource-server](https://mvnrepository.com/artifact/org.springframework.boot/spring-boot-starter-security-oauth2-resource-server)

1. フロントエンドアプリケーション
    - [MSAL.js](https://www.npmjs.com/package/@azure/msal-browser)

その他の使用 OSS は、 AlesInfiny Maia のサンプルアプリケーションに準じます。

## サンプルの動作確認手順

本サンプルをローカルマシンで動作させるには、事前に Entra External ID のテナントを作成し、アプリケーションを登録する作業が必要です。

### Entra External ID テナントの作成

1. [クイック スタート: Azure サブスクリプションを使用して外部テナントを作成する](https://learn.microsoft.com/ja-jp/entra/external-id/customers/quickstart-tenant-setup) に従って、[Microsoft Entra 管理センター](https://entra.microsoft.com/) にサインインし、 Entra External ID のテナントを作成します。
    - 「`テナントサブドメイン`」（ドメイン名から `.onmicrosoft.com` を除いた部分）をメモします。

### Entra External ID テナントを利用するアプリの登録（バックエンドアプリケーション）

<!-- textlint-disable ja-technical-writing/sentence-length -->

1. [アプリケーションを Microsoft Entra ID に登録する](https://learn.microsoft.com/ja-jp/entra/identity-platform/quickstart-register-app) に従って、バックエンドアプリケーション用のアプリを Entra External ID に登録します。

    - 登録したアプリの名前を、ここでは「 `SampleWebAPI` 」とします。
  　<!-- textlint-disable @textlint-ja/no-synonyms -->
    - サポートされているアカウントの種類を、「この組織ディレクトリのみに含まれるアカウント」とします。
    <!-- textlint-enable @textlint-ja/no-synonyms -->
    - 登録したアプリの `クライアント ID` （アプリケーション ID ）をメモします。

    <!-- textlint-enable ja-technical-writing/sentence-length -->

1. [委任されたアクセス許可 (スコープ) を追加する](https://learn.microsoft.com/ja-jp/entra/identity-platform/quickstart-web-api-dotnet-protect-app?tabs=aspnet-core#add-delegated-permissions-scopes) に従って、アプリにスコープを追加します。
    - チュートリアルの手順では読み取りと書き込み 2 つのスコープを作成していますが、本サンプルのシナリオでは作成するスコープは 1 つで良いです。
    - 追加したスコープの名前を、ここでは「 `api.read` 」とします。
1. 「アプリの登録」ブレードを選択し、「すべてのアプリケーション」から「 SampleWebAPI 」を選択します。
1. 「概要」ブレードに表示された「 `アプリケーション ID の URI` 」をメモします。

### Entra External ID テナントを利用するアプリの登録（フロントエンドアプリケーション）

1. [アプリケーションを Microsoft Entra ID に登録する](https://learn.microsoft.com/ja-jp/entra/identity-platform/quickstart-register-app) に従って、フロントエンドアプリケーション用のアプリを Entra External ID に登録します。
    - 登録したアプリの名前を、ここでは「 `SampleSPA` 」とします。
    <!-- textlint-disable @textlint-ja/no-synonyms -->
    - サポートされているアカウントの種類を、「この組織ディレクトリのみに含まれるアカウント」とします。
    <!-- textlint-enable @textlint-ja/no-synonyms -->
    - 登録したアプリの `クライアント ID` （アプリケーション ID ）をメモします。
1. 「アプリの登録」ブレードを選択し、「すべてのアプリケーション」から「 SampleSPA 」を選択します。
1. 「認証」ブレードを選択し、「リダイレクト URI の追加」をクリックします。「シングルページアプリケーション」を選択し、リダイレクト URI に「 `http://localhost:5173` 」を設定します。
1. [Web API にアクセスするためのアクセス許可を追加する](https://learn.microsoft.com/ja-jp/entra/identity-platform/quickstart-configure-app-access-web-apis#add-permissions-to-access-your-web-api) に従って、 SampleSPA に、前の手順で追加した SampleWebAPI のスコープ「 `api.read` 」へのアクセス許可を付与します。
1. [管理者の同意を付与する (外部テナントのみ)](https://learn.microsoft.com/ja-jp/entra/identity-platform/quickstart-register-app#grant-admin-consent-external-tenants-only) に従って、 SampleSPA に管理者の同意を付与します。

### ユーザーフローの作成と割り当て

1. [外部テナント アプリのサインアップおよびサインイン ユーザー フローを作成する](https://learn.microsoft.com/ja-jp/entra/external-id/customers/how-to-user-flow-sign-up-sign-in-customers) に従って、ユーザーフローを作成します。
    - ここでは追加した `サインアップとサインイン` ユーザーフローの名前を「 `signupsignin1` 」とします。
1. [アプリケーションをユーザー フローに追加する](https://learn.microsoft.com/ja-jp/entra/external-id/customers/how-to-user-flow-add-application) に従って、 SampleSPA を signupsignin1 に追加します。

### 設定情報の記入

#### バックエンドアプリケーションの設定

1. VS Code で `auth-backend\web\src\main\resources\application.properties` を開きます。
1. 以下のように設定情報を記入します（以下の例では External ID の設定以外は省略しています）。

    ```properties
    spring.security.oauth2.resourceserver.jwt.issuer-uri=https://{tenant-id}.ciamlogin.com/{tenant-id}/v2.0
    spring.security.oauth2.resourceserver.jwt.audiences=client-id
    cors.allowed.origins=[フロントエンドアプリケーションのベースとなるURL。本サンプルの既定では http://localhost:5173]
    ```

#### フロントエンドアプリケーションの設定

1. `auth-frontend\.env.dev` を開きます。
1. 以下のように設定情報を記入します（以下の例では Entra External ID の設定以外は省略しています）。

```properties
VITE_EXTERNAL_ID_AUTHORITY_DOMAIN=https://[テナントサブドメイン].ciamlogin.com/
VITE_EXTERNAL_ID_SCOPE=[SampleWebAPI のアプリケーション ID の URI]/[Web APIに追加したスコープの名前]
VITE_EXTERNAL_ID_APP_CLIENT_ID=[SampleSPA のクライアント ID]
VITE_EXTERNAL_ID_APP_URI=[フロントエンドアプリケーションのベースとなるURL。サンプルの既定では http://localhost:5173]
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
1. 画面の「 `ログイン` 」をクリックします。 Entra External ID のサインイン画面がポップアップで表示されます。
1. 「アカウントをお持ちでない場合、作成できます」リンクをクリックします。
1. 使用可能なメールアドレスを入力し、「次へ」をクリックします。
1. 上の手順で入力したメールアドレス宛にアカウント確認コードが送信されるので、画面に入力して「次へ」をクリックします。
1. 画面に新しいパスワード等の必要事項を入力し、「次へ」をクリックします。
1. サインインが成功し、画面上に「ユーザー ID 」が表示されれば成功です。以降は入力したメールアドレスとパスワードでサインインできるようになります。
1. 画面上の「 `ログアウト` 」をクリックします。 Entra External ID のサインアウト画面がポップアップで表示されます。
1. サインアウトするアカウントをクリックします。
1. サインアウトに成功すると、画面上から「ユーザー ID 」 の表示が消え、「 `ログイン` 」が表示されます。

Entra External ID に追加したユーザーは、以下の手順で削除できます。

1. Microsoft Entra 管理センターの「ユーザー」ブレードを選択します。
1. 対象のユーザーをチェックし、画面上部から「削除」を選択します。

### テストの実行

バックエンドアプリケーションには、認証が必要な Web API および認証不要な Web API の両方について、結合テストが実装されています。
以下の Gradle コマンドを実行してください。

```shell
./gradlew web:test
```

上記コマンドを実行すると、主に以下の観点でテストが実行されます。

1. 認証不要な Web API
    - Bearer トークンを付与しないリクエストで、正常にレスポンスが返ること

1. 認証が必要な Web API
    - Bearer トークンを付与しない場合に、認証エラー（401 / 403）となること
    - 有効な Bearer トークンを付与した場合に、正常にレスポンスが返ること

以下が表示され、テストが成功していることを確認します。

```shell
BUILD SUCCESSFUL in 2s
```

## Dressca アプリケーションへの認証機能の組み込み手順

本サンプルのコード例を既存のアプリケーションへコピーすることで、 Entra External ID の認証機能を組み込むことができます。
本章ではそのコード例を AlesInfiny Maia OSS Edition のサンプルアプリケーションである Dressca アプリケーションに組み込む方法を、具体的な手順として説明します。

### バックエンドアプリケーション

以下、 Dressca アプリケーション (Consumer) のバックエンドアプリケーションに認証機能を適用する手順を示します。

1. [バックエンドアプリケーションの設定](#バックエンドアプリケーションの設定) を参照し、 `web-consumer\src\main\resources\application.properties` を設定、ライブラリを追加します。
1. `dependencies.gradle`を開きます。
1. 以下のように OSS ライブラリの依存関係を記入します（以下の例では Entra External ID の設定以外は省略しています）。

    ```gradle
    ext {
      supportDependencies = [
        spring_boot_starter_security_oauth2_resource_server : "org.springframework.boot:spring-boot-starter-security-oauth2-resource-server",
      ]
    }
    ```

1. `web-consumer\build.gradle`を開きます。
1. 以下のように OSS ライブラリの依存関係を記入します（以下の例では Entra External ID の設定以外は省略しています）。

    ```gradle
    dependencies {
      implementation supportDependencies.spring_boot_starter_security_oauth2_resource_server
    }
    ```

1. 認証を必要とするコントローラークラスで、 認証が必要であることを表すアノテーションを付与します。
   以下は、 `web-consumer\src\main\java\...\controller\OrderController.java` の `getById()` メソッドに認証が必要なアノテーションを付与する例です。

    ```java
    import org.springframework.security.access.prepost.PreAuthorize;
    import org.springframework.web.bind.annotation.CrossOrigin;
    import io.swagger.v3.oas.annotations.security.SecurityRequirement;

    @RestController
    @Tag(name = "Orders", description = "注文の情報にアクセスする API です。")
    @AllArgsConstructor
    @RequestMapping("/api/orders")
    public class OrderController {
      ...
      /**
      * 注文情報を取得します。
      * 
      * @param orderId 注文 ID 。
      * @return 注文情報。
      */
      @Operation(
          summary = "注文情報を取得します。",
          description = "注文情報を取得します。",
          security = {
              @SecurityRequirement(name = "Bearer")})
      @ApiResponses(
          value = {
              @ApiResponse(
                  responseCode = "200",
                  description = "成功。",
                  content = @Content(
                      mediaType = "application/json",
                      schema = @Schema(implementation = OrderResponse.class))),
              @ApiResponse(
                  responseCode = "404",
                  description = "注文 ID が存在しません。",
                  content = @Content(
                      mediaType = "application/problem+json",
                      schema = @Schema(implementation = ProblemDetail.class)))
          })
      @GetMapping("{orderId}")
      @PreAuthorize(value = "isAuthenticated()")
      public ResponseEntity<?> getById(@PathVariable("orderId") long orderId, HttpServletRequest req) {
        // その他のコードは省略
        ...
      }
    }
    ```

    付与するアノテーションの役割は以下の通りです。

    |             アノテーション              | 付与対象 |                                                       役割                                                        |
    | --------------------------------------- | -------- | ----------------------------------------------------------------------------------------------------------------- |
    | `@PreAuthorize("isAuthenticated()")`    | メソッド | リクエストが**認証済みユーザーによるものか**を Spring Security で判定します。未認証の場合はアクセスを拒否します。 |
    | `@SecurityRequirement(name = "Bearer")` | メソッド | OpenAPI 仕様書上で、**Bearer トークン（JWT）が必要な API**であることを明示します。                                |

1. 以下のように `web-consumer\src\main\java\...\security\WebSecurityConfig.java` に認証に関する処理を追加します。

    ```java
      import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;

      @Configuration(proxyBeanMethods = false)
      @EnableWebSecurity
    + @EnableMethodSecurity
      public class WebSecurityConfig {
        
        // その他のコードは省略
        ...

        @Bean
        public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
          http
              .headers(headers -> headers.frameOptions(frameOptions -> frameOptions.sameOrigin()))
              .securityMatcher("/api/**")
              .csrf(csrf -> csrf.ignoringRequestMatchers("/api/**"))
              .cors(cors -> cors.configurationSource(request -> {
                CorsConfiguration conf = new CorsConfiguration();
                conf.setAllowCredentials(true);
                conf.setAllowedOrigins(Arrays.asList(allowedOrigins));
                conf.setAllowedMethods(List.of("GET", "HEAD", "POST", "PUT", "DELETE", "OPTIONS"));
                conf.setAllowedHeaders(List.of("*"))
                conf.addExposedHeader("Location");
                return conf;
              }))
    +        .oauth2ResourceServer(oauth2 -> oauth2.jwt(Customizer.withDefaults()));
          return http.build();
        }
      }
    ```

    追加した処理の役割は以下の通りです。

    - `@EnableMethodSecurity`
      コントローラーのメソッドに付与する `@PreAuthorize` のアノテーションを有効化します。
      これにより、例えば `@PreAuthorize("isAuthenticated()")` を付与した API は認証済みユーザーのみ実行可能になります。

    - `.oauth2ResourceServer(oauth2 -> oauth2.jwt(Customizer.withDefaults()))`
      本アプリケーションを OAuth 2.0 Resource Server として動作させ、 HTTP リクエストのヘッダーに含まれる Bearer トークンを検証します。

1. 未認証の場合の例外ハンドラを実装します。
   `src\main\java\...\controller\advice\ExceptionHandlerControllerAdvice.java` に対して以下の ExceptionHandler を設定します。

    ```java
    import org.springframework.security.access.AccessDeniedException;

    @ControllerAdvice
    public class ExceptionHandlerControllerAdvice extends ResponseEntityExceptionHandler {
      // 未認証の場合に発生する AccessDeniedException に対し、例外ハンドラを指定
      @ExceptionHandler(AccessDeniedException.class)
      public ResponseEntity<?> accessDeniedHandleException(AccessDeniedException e, HttpServletRequest req) {
        // 以下に、例外ハンドラの詳細を実装
      }

      // その他のコードは省略
      ...
    }
    ```

1. バックエンドアプリケーションをビルドします。

### フロントエンドアプリケーション

以下、 Dressca アプリケーション (Consumer) のフロントエンドアプリケーションに認証機能を適用する手順を示します。

1. VS Code で `auth-frontend` のフォルダーの `auth-frontend.code-workspace` ファイルを開きます。
1. ターミナルで`cd ../consumer` 、 `npm install @azure/msal-browser` を順に実行し、フロントエンドアプリケーションに MSAL.js をインストールします。
1. `auth-frontend\.env.dev` に記述した Entra External ID の設定をフロントエンドアプリケーションの `.env.dev` にコピーします。
1. `env.d.ts` のインターフェースに、前の手順で `.env.dev` に追加したプロパティを追加します。

    ```typescript
      interface ImportMetaEnv {
        readonly VITE_NO_ASSET_URL: string
        readonly VITE_ASSET_URL: string
        readonly VITE_AXIOS_BASE_ENDPOINT_ORIGIN: string
        readonly VITE_PROXY_ENDPOINT_ORIGIN: string
    +   readonly VITE_EXTERNAL_ID_AUTHORITY_DOMAIN: string
    +   readonly VITE_EXTERNAL_ID_SCOPE: string
    +   readonly VITE_EXTERNAL_ID_APP_CLIENT_ID: string
    +   readonly VITE_EXTERNAL_ID_REDIRECT_URI: string
    +   readonly VITE_EXTERNAL_ID_LOGOUT_REDIRECT_URI: string
      }
    ```

1. `npm run generate-client` を実行し、 Axios のクライアントコードを再生成します。
1. `src\services\authentication` フォルダーを作成し、本サンプルの以下のコードをコピー・差し替えします。
    - authentication-services.ts
    - authentication-config.ts
1. `src\store\authentication` フォルダーの `authentication.ts` を本サンプルのコードに差し替えます。
1. 認証が成功したら、認証が必要な Web API リクエストヘッダーに Bearer トークンを付与する必要があります。
   本例では、 OrderApi アクセス時に Bearer トークンを付与する例を示します。
   `src\api-client\index.ts` を以下のように編集します。

    ```typescript
    // その他のコードは省略
    async function addToken(config: apiClient.Configuration) {
      // 認証済みの場合、アクセストークンを取得して Configuration に設定します。
      if (await authenticationService.isAuthenticated()) {
        const token = await authenticationService.getTokenEntraExternalId();
        config.accessToken = token;
      }
    }

    async function ordersApi() {
      const config = createConfig()
      // 認証が必要な API では、addToken を呼び出します。
      await addToken(config)
      const ordersApi = new apiClient.OrdersApi(config, '', axiosInstance)
      return ordersApi
    }
    ```

1. `src\App.vue` に対して、 `<script>` セクションに以下のコードを追加します。

    ```typescript
    const { signIn, signOut, isAuthenticated } = authenticationService()

    const signInButtonClicked = async () => {
      try {
        await signIn()
      } catch (error) {
        // ポップアップ画面をユーザーが×ボタンで閉じると、 BrowserAuthError が発生します。
        if (error instanceof BrowserAuthError) {
          // 認証途中でポップアップを閉じることはよくあるユースケースなので、ユーザーには特に通知しません。
          customErrorHandler.handle(error, () => {
            console.info('ユーザーが認証処理を中断しました。')
          })
        } else {
          customErrorHandler.handle(error, () => {
            window.alert('Microsoft Entra External Id での認証に失敗しました。')
          })
        }
      }
    }

    const signOutButtonClicked = async () => {
      try {
        await signOut()
      } catch (error) {
        // ポップアップ画面をユーザーが×ボタンで閉じると、 BrowserAuthError が発生します。
        if (error instanceof BrowserAuthError) {
          // 認証途中でポップアップを閉じることはよくあるユースケースなので、ユーザーには特に通知しません。
          customErrorHandler.handle(error, () => {
            console.info('ユーザーが認証処理を中断しました。')
          })
        } else {
          customErrorHandler.handle(error, () => {
            window.alert('Microsoft Entra External Id での認証に失敗しました。')
          })
        }
      }
    }
    ```

1. `src\App.vue` に対して、 `<template>` セクションのボタンを以下のように差し替えます。

   ```html
    <header>
      <nav
        aria-label="Jump links"
        class="py-5 text-lg font-medium text-gray-900 shadow-xs ring-1 ring-gray-900/5"
      >
        <div class="mx-auto flex justify-between px-4 md:px-24 lg:px-24">
          <div>
            <router-link class="text-2xl" to="/"> Dressca </router-link>
          </div>
          <div class="flex gap-5 sm:gap-5 lg:gap-12">
            <router-link to="/basket">
              <ShoppingCartIcon class="h-8 w-8 text-amber-600" />
            </router-link>
            <button v-if="!isAuthenticated()" @click="signInButtonClicked">ログイン</button>
            <button v-if="isAuthenticated()" @click="signOutButtonClicked">ログアウト</button>
          </div>
        </div>
      </nav>
    </header>
   ```
  
1. `src\views\authentication\LoginView.vue` は Entra External ID の LoginPopup ウィンドウに切り替わるため削除します。
1. `src\shared\authentication\authentication-guard.ts` はログインページではなく Entra External ID の LoginPopUp を表示させるように変更します。

    ```typescript
    if (to.meta.requiresAuth && !authenticationStore.isAuthenticated) {
      try {
        await authenticationService().signIn()
      } catch {
        return false
      }
    }
    ```

1. `src\router\index.ts` から、 `authenticationRoutes` を削除します。

1. BrowserAuthError が発生した場合は、エラーページに遷移させないように `src\shared\error-handler\custom-error-handler.ts` に以下を追加します。

    ```typescript
    export function useCustomErrorHandler(): handleErrorAsyncFunction {
    const { t } = i18n.global
    const handleErrorAsync = async (
      error: unknown,
      callback: MaybeAsyncFunction<void>,
      handlingHttpError: MaybeAsyncUnaryFunction<HttpError, void> | null = null,
      handlingUnauthorizedError: MaybeAsyncFunction<void> | null = null,
      handlingNetworkError: MaybeAsyncFunction<void> | null = null,
      handlingServerError: MaybeAsyncFunction<void> | null = null,
    ) => {
      const logger = useLogger()
      const unhandledErrorEventBus = useEventBus(unhandledErrorEventKey)
      const unauthorizedErrorEventBus = useEventBus(unauthorizedErrorEventKey)
      // ハンドリングできるエラーの場合はコールバックを実行します。
      if (error instanceof BrowserAuthError) {
        await callback()
        return
      }
      if (error instanceof CustomErrorBase) {
        logger.error(JSON.stringify(error.toJSON()))
        await callback()
        if (error instanceof HttpError) {
          // 業務処理で発生した HttpError を処理します。
          if (handlingHttpError) {
            await handlingHttpError(error)
          }
          // その他のコードは省略
          ...
        }
      }
    }
    ```

## 参照記事

本サンプルは、以下の記事に基づき作成しました。

### フロントエンドアプリケーションの参照記事

- [Microsoft Entra External ID を利用した SPA アプリケーションサンプル](https://learn.microsoft.com/ja-jp/entra/identity-platform/quickstart-single-page-app-sign-in?tabs=javascript-workforce%2Cjavascript-external&pivots=external)

### バックエンドアプリケーションの参照記事

- [Spring Security における SecurityFilterChain のアーキテクチャー](https://spring.pleiades.io/spring-security/reference/servlet/architecture.html)
- [Spring Security の OAuth2.0 サポート](https://spring.pleiades.io/spring-security/reference/servlet/oauth2/)
