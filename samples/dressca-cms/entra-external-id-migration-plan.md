# dressca-cms 認証方式の Microsoft Entra External ID (CIAM) 移行プラン {#entra-external-id-migration-plan}

## Context {#context}

dressca-cms は Spring Boot 4.0.7 (Spring Security 7) + Thymeleaf の SSR 型 Web アプリです。現在は独自フォーム認証を実装しています。

- `LoginController.java`: email/password から `UsernamePasswordAuthenticationToken` を生成し HTTP セッションへ保存
- `web/src/main/java/com/dressca/cms/web/security/SecurityConfig.java` の `DaoAuthenticationProvider` + BCrypt
- `authentication` モジュールが MyBatis で `APPLICATION_USERS` テーブルを検索

これを Microsoft Entra External ID (CIAM) の OIDC **Authorization Code Flow** に移行する。

### 確定済みの方針 {#confirmed-policy}

1. **Spring Security OAuth2 Client**(`spring-boot-starter-oauth2-client` の `oauth2Login()`)を採用。 External ID の公式サンプルに Java 版はなく、 Microsoft の Spring 向けガイダンスも実体は Spring Security OAuth2 Client 。サインインのみの本アプリに MSAL4J は不要(将来 API 呼び出しのトークン管理が必要になった時点で再検討)
2. **DB 認証資産は削除**して Entra に一本化。表示名/メールは ID トークン(`OidcUser`)のクレームから取得
3. **ログアウト実装はスコープ外**(現状も未実装のまま)

クライアント登録 ID は `entra` とする(コールバック URI: `/login/oauth2/code/entra`)。

---

## 前提作業: Entra External ID テナント側の手動準備 {#manual-preparation}

コード変更の前提として Microsoft Entra 管理センターで行う。

1. 外部テナントを作成し、テナント ID・サブドメインを控える
2. **アプリ登録**(Web): リダイレクト URI `http://localhost:8083/login/oauth2/code/entra`(本番用 `https://<host>/login/oauth2/code/entra` も追加)
3. **クライアントシークレット**発行(値は一度しか表示されない。最長 2 年で失効するためローテーション運用も決めておく)
4. **API のアクセス許可**: Graph 委任の `openid` / `profile` / `email` を追加し**管理者の同意を付与**(External ID テナントはユーザー同意が既定で無効)
5. **トークン構成**: optional claims で ID トークンに `email` を追加
6. **ユーザーフロー**作成: 収集属性とアプリケーションクレームの両方で Display Name / Email Address を有効化し、アプリ登録を関連付け
7. **疎通確認**: OIDC メタデータ URL を開く。 URL は `https://{subdomain}.ciamlogin.com/{tenant-id}/v2.0/.well-known/openid-configuration`。
   **レスポンスの `issuer` 値をそのまま `issuer-uri` に使う**。サブドメイン名でなくテナント GUID 形式で返ることが多く、不一致だと issuer 検証で失敗する。

## 1. ビルド設定 {#build-settings}

|               ファイル               |                                                                           変更                                                                           |
| ------------------------------------ | -------------------------------------------------------------------------------------------------------------------------------------------------------- |
| `dependencies.gradle`                | `supportDependencies` に `spring_boot_starter_oauth2_client : "org.springframework.boot:spring-boot-starter-oauth2-client"` を追加                       |
| `web/build.gradle`                   | 上記を `implementation` 追加。`implementation project(':authentication')` を削除。`spring_boot_starter_security_test` は残す(oidcLogin() テストで使用可) |
| `settings.gradle`                    | `include 'announcement', 'system-common', 'web'` に変更                                                                                                  |
| `config/checkstyle/suppressions.xml` | `authentication` モジュールの generated コード向け suppress 行を削除(死設定の掃除)                                                                       |

## 2. SecurityConfig の書き換え {#rewrite-security-config}

`web/src/main/java/com/dressca/cms/web/security/SecurityConfig.java` を全面書き換える。
`PasswordEncoder` / `AuthenticationManager` / `DaoAuthenticationProvider` Bean を削除する。
`UserDetailsServiceImpl` 依存、`formLogin`、`exceptionHandling` も削除する。

```java
@Bean
public SecurityFilterChain configure(HttpSecurity http) throws Exception {
  http.headers(headers -> headers.frameOptions(frameOptions -> frameOptions.deny())
      .contentSecurityPolicy(csp -> csp.policyDirectives("frame-ancestors 'none';")))
      .authorizeHttpRequests(authorize -> authorize
          .requestMatchers("/bootstrap/**", "/css/**", "/scss/**", "/images/**")
          .permitAll().anyRequest().authenticated())
      .oauth2Login(Customizer.withDefaults());
  return http.build();
}
```

- `/oauth2/authorization/*` と `/login/oauth2/code/*` は専用フィルターが認証判定より前段で処理するため permitAll 不要
- クライアント登録が 1 件のみなら、未認証アクセスは自動的に Entra のサインイン画面へ直行する(中間ログインページなし)
- **元 URL への復帰は標準の `HttpSessionRequestCache`(saved request)が代替**する。
  そのため、`ReturnUrlQueryAppendingEntryPoint.java` と returnUrl 機構は削除できる。
  saved request がない場合のデフォルト遷移先 `/` は `HomeController` が処理する。
- CSRF・セッション固定対策は Spring Security デフォルトのまま

## 3. OAuth2 クライアント設定(properties) {#oauth2-client-properties}

**配置方針(重要)**: `application-common.properties` には置かない。`common` は `test`(=`common,ut`)グループにも含まれるため、`issuer-uri` を common に置くとテスト起動時に実テナントへのディスカバリーアクセスが走り CI が確実に失敗する。**issuer-uri は dev / prd にのみ定義**し、 ut は明示エンドポイントのダミーを置く。

**`web/src/main/resources/application-dev.properties`**(prd も同構成で本番テナント値)。

```properties
# Microsoft Entra External ID (CIAM)
spring.security.oauth2.client.registration.entra.provider=entra
spring.security.oauth2.client.registration.entra.client-id=<アプリケーション (クライアント) ID>
spring.security.oauth2.client.registration.entra.client-secret=${ENTRA_CLIENT_SECRET}
spring.security.oauth2.client.registration.entra.authorization-grant-type=authorization_code
spring.security.oauth2.client.registration.entra.scope=openid,profile,email
spring.security.oauth2.client.provider.entra.issuer-uri=<メタデータの issuer 値そのまま>
```

- `redirect-uri` は未指定でよい(デフォルトの `{baseUrl}/login/oauth2/code/{registrationId}` が環境に自動追従)
- シークレットは環境変数 `ENTRA_CLIENT_SECRET` で注入。ローカルは `.vscode/launch.json` が参照する `.env` へ書ける(`.env` が `.gitignore` に含まれていないため追記する)
- `scope` から `openid` を外すとプリンシパルが `OidcUser` でなくなり全 `@AuthenticationPrincipal` が null になるため、絶対に落とさない

**`web/src/main/resources/application-ut.properties`**(テストはルート `build.gradle` で `common,ut` 固定)。

```properties
# UT 用ダミー OAuth2 クライアント設定(ネットワークアクセスなしでコンテキスト起動可能にする)
spring.security.oauth2.client.registration.entra.provider=entra
spring.security.oauth2.client.registration.entra.client-id=dummy-client-id
spring.security.oauth2.client.registration.entra.client-secret=dummy-client-secret
spring.security.oauth2.client.registration.entra.authorization-grant-type=authorization_code
spring.security.oauth2.client.registration.entra.scope=openid,profile,email
spring.security.oauth2.client.provider.entra.authorization-uri=https://localhost/dummy/oauth2/v2.0/authorize
spring.security.oauth2.client.provider.entra.token-uri=https://localhost/dummy/oauth2/v2.0/token
spring.security.oauth2.client.provider.entra.jwk-set-uri=https://localhost/dummy/discovery/v2.0/keys
spring.security.oauth2.client.provider.entra.user-name-attribute=sub
```

これがないと `oauth2Login()` が `ClientRegistrationRepository` を要求し `WebApplicationTests.contextLoads()` が落ちる。ダミー issuer-uri 方式は起動時に実アクセスするため不可 — 明示エンドポイント方式のみ安全。

## 4. プリンシパル参照の置き換え(`UserDetails` → `OidcUser`) {#replace-principal-reference}

クレーム選択: 監査記録は `email` → `preferred_username` → `sub` の順でフォールバック。画面表示は `name`(`getFullName()`)→ `email` → `preferred_username`。

|                                          ファイル                                           |                                                                                                 変更                                                                                                  |
| ------------------------------------------------------------------------------------------- | ----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| `web/src/main/java/com/dressca/cms/web/controller/advice/AuthenticatedUserModelAdvice.java` | `@AuthenticationPrincipal OidcUser` に変更し、表示名フォールバックで `${userName}` を公開。null チェックは維持                                                                                        |
| `web/src/main/java/com/dressca/cms/web/controller/AnnouncementController.java`              | 3 箇所(store / update / delete)を `@AuthenticationPrincipal OidcUser` に変更。`userDetails.getUsername()` は監査用フォールバックヘルパー(private メソッド `resolveAuditUserName(OidcUser)`)経由に置換 |
| `web/src/main/java/com/dressca/cms/web/WebApplication.java`                                 | `scanBasePackages` から `"com.dressca.cms.authentication"` を削除                                                                                                                                     |
| `web/src/main/resources/templates/fragments/header.html`                                    | ログインリンクを `@{/account/login}` → `@{/oauth2/authorization/entra}` に変更。`userName == null` 分岐は残す                                                                                         |

`announcement` モジュールは `String username` を受け取るだけで Spring Security 非依存のため変更不要。

## 5. 削除対象 {#deletion-targets}

- **`authentication/` モジュール全体**
  - UserDetailsServiceImpl
  - UserDetailsImpl
  - UserRepository
  - MyBatisUserRepository
  - Mapper/Entity
  - schema-authentication.sql
  - data-authentication.sql
  <!-- textlint-disable @textlint-ja/no-synonyms -->
  - i18n/authentication/messages.properties
  <!-- textlint-enable @textlint-ja/no-synonyms -->
  - mybatisGeneratorConfig.xml
  - application-*.properties
  - UserDetailsServiceImplTest
  - build.gradle
- `web/src/main/java/com/dressca/cms/web/controller/LoginController.java`
- `web/src/main/java/com/dressca/cms/web/models/base/LoginViewModel.java`
- `web/src/main/java/com/dressca/cms/web/security/ReturnUrlQueryAppendingEntryPoint.java`
- `web/src/main/resources/templates/authentication/`(login.html)
- `web/src/main/resources/i18n/authentication/`(login_ja.properties 。`_en` 版は存在しない)

参照破壊チェック済み: `com.dressca.cms.authentication` への参照は SecurityConfig と WebApplication のみ。`authentication.login.*` のメッセージキー参照は削除対象ファイルに閉じる。メッセージ定義は `I18nConfig` の glob スキャンのため設定変更不要。`schema-*.sql`/`data-*.sql` も glob 読み込みのためモジュール削除で自動的に消える。 DB の FK 参照なし。 PostgreSQL/H2 ドライバーは announcement モジュールも宣言済みで prd 接続に影響なし。

## 6. テスト {#tests}

- 既存テストは `web/src/test/java/com/dressca/cms/web/WebApplicationTests.java`(contextLoads)のみ — §3 の ut 設定で起動が通る
- 追加(推奨): `SecurityMockMvcRequestPostProcessors.oidcLogin()` で OidcUser ルートを実テナントなしで検証する。
  `AnnouncementController` / `AuthenticatedUserModelAdvice` を対象にする。
  未認証アクセスは 302 → `/oauth2/authorization/entra` を期待値にする。

## 実装順序と検証 {#implementation-order-and-verification}

実装順: ビルド設定 → 削除 → SecurityConfig → プリンシパル置換 → properties → テスト。

1. `./gradlew build` — コンパイル・テスト・checkstyle/spotbugs 通過(新 SecurityConfig も既存同様の Javadoc スタイルを維持)
2. 環境変数へテナント値を設定し `./gradlew :web:bootRunDev` で起動する(起動時に OIDC ディスカバリーへ HTTPS アクセスが発生 — 到達不能だと起動失敗。プロキシ環境では JVM プロキシ設定が必要)
3. `http://localhost:8083/top` → Entra サインイン画面へリダイレクト → 認証 → `/top` 復帰、ヘッダーに表示名/メール表示
4. 未認証で `/announcements` 等の深いリンク → サインイン後に元 URL へ復帰(RequestCache)
5. お知らせ登録/更新/削除 → 履歴の操作ユーザーにメールアドレスが記録されること
6. 初回ログインで `OidcUser.getClaims()` を確認し `email` / `name` クレームが発行されていること(欠落時はテナント側設定 §前提 4〜6 を見直し)

## 落とし穴(要注意順) {#pitfalls}

1. **issuer-uri 不一致**: 実メタデータの `issuer`(テナント GUID 形式のことが多い)をそのまま設定する
2. **テスト起動失敗**: OAuth2 設定を common に置かない。 ut はダミー明示エンドポイント方式のみ
3. **userinfo 呼び出し**: `profile`/`email` スコープがあると `OidcUserService` が Graph の userinfo を呼ぶ。
   CIAM で HTTP Unauthorized になることがある。発生したら `OidcUserService` Bean で `setRetrieveUserInfo(request -> false)` を設定する。
   必要クレームは ID トークンに載る。
4. **email クレーム欠落**: optional claim + ユーザーフローのアプリケーションクレーム + 管理者同意の 3 点セットが必要
5. **client 認証方式**: トークン取得で `invalid_client` が出たら `client-authentication-method=client_secret_post` を明示
6. **本番 DB の後始末**: prd は `spring.sql.init.mode=never` のため `application_users` テーブルの DROP は手動の運用作業として別途予定
7. **actuator**: `/actuator/health` も Entra へ 302 になる(現行も /account/login へ 302 なので劣化ではない)。インフラストラクチャのヘルスチェックが要るなら `requestMatchers("/actuator/health").permitAll()` を検討
8. **マルチインスタンス**: state / saved request は HttpSession 保存のため、複数台構成ではスティッキーセッションかセッション外部化が必要

## 関連資料 {#references}

- [Spring Boot Starter for Microsoft Entra developer's guide](https://learn.microsoft.com/en-us/azure/developer/java/spring-framework/spring-boot-starter-for-entra-developer-guide)
- [Microsoft Entra External ID サンプル一覧(Java 版なし)](https://learn.microsoft.com/en-us/entra/external-id/customers/samples-ciam-all)
