---
title: MSAL.js で提供される秘密情報のキャッシュ保存先
description: Azure AD B2C による認証・認可において、トークンのキャッシュ保存先を指定する方法について解説します。
---

# MSAL.js で提供される秘密情報のキャッシュ保存先 {#top}

本サンプルのフロントエンドで認証・認可機能を実現するために Microsoft が提供するライブラリである [MSAL.js :material-open-in-new:](https://www.npmjs.com/package/@azure/msal-browser){ target=_blank } を利用しています。

MSAL.js で提供されている  `loginPopup()` や `loginRedirect()` といった認証用のメソッドを利用すると、将来の使用に備えて以下の秘密情報を永続的なアーティファクトとしてキャッシュします。

- ID トークン
- アクセストークン
- リフレッシュトークン
- アカウント情報（ `homeAccountId` 、 `localAccountId` 、 `ID トークンのクレーム` ）

## キャッシュストレージの保存先設定 {#setting-cache-storage}

MSAL のインスタンス化に使用する構成オブジェクトをもとに、キャッシュストレージの保存先を設定できます。
具体的には、 `authentication-config.ts` の以下の部分で設定します。

``` ts title="authentication-config.ts" hl_lines="9"
export const msalConfig = {
  auth: {
    clientId: import.meta.env.VITE_ADB2C_APP_CLIENT_ID,
    authority: b2cPolicies.authorities.signUpSignIn.authority,
    knownAuthorities: [b2cPolicies.authorityDomain],
    redirectUri: import.meta.env.VITE_ADB2C_APP_URI,
  },
  cache: {
    cacheLocation: 'sessionStorage',
    storeAuthStateInCookie: true,
  },
  ...
}
```

キャッシュストレージの保存先として指定できる値は以下の通りです。

- `sessionStorage`
- `localStorage` （ AlesInfiny Maia OSS Edition では非推奨）
- `memoryStorage`

なお、デフォルトでは [Web Storage API :material-open-in-new:](https://developer.mozilla.org/ja/docs/Web/API/Web_Storage_API){ target=_blank } が提供する `sessionStorage` を利用します。
キャッシュストレージの保存先の違いに関しては、 [Store の永続化方式](../../app-architecture/client-side-rendering/global-function.md) で詳細を確認してください。

!!! Danger "localStorage を利用する際の危険性"

    キャッシュの保存先に `localStorage` を指定した場合、トークンやアクセス情報といった秘密情報が Local Storage に保存されます。
    すなわち、ユーザーが明示的にログアウトをしない限りキャッシュがクリアされず、 XSS 攻撃などのセキュリティ上の脅威にさらされ続ける可能性があります。
    
    よって、MSAL.js で保存される認証・認可情報のキャッシュの保存先として `localStorage` を利用しないでください。

!!! Warning "memoryStorage を利用する際の注意点"

    キャッシュの保存先に `memoryStorage` を指定した場合、インメモリに秘密情報が保持されるため、ページの更新やナビゲーションでキャッシュがクリアされます。
    これにより、セキュリティが強固になるメリットを享受できる反面、 キャッシュのクリアごとにユーザー認証が必要になり、ユーザー体験が低下する恐れがあります。

    また、MSAL.js で提供されている `loginRedirect()` や `acquireTokenRedirect()` といったリダイレクトフローが利用できず、`loginPopup()` や `acquireTokenPopup()` といったポップアップによる実装が強制されます。
    このような条件の下で、`memoryStorage` を利用するかどうかを考慮してください。

## 参照記事 {#reference}

詳細については、以下を確認してください。

- [Caching in MSAL :material-open-in-new:](https://github.com/AzureAD/microsoft-authentication-library-for-js/blob/dev/lib/msal-browser/docs/caching.md){ target=_blank }
