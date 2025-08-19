---
title: Azure AD B2C を 利用した ユーザー認証
description: Azure AD B2C による認証を利用するためのサンプルと、 その使い方を解説します。
---

# MSAL.js で提供される秘密情報のキャッシュ保存先 {#top}

本サンプルのフロントエンドでは、認証・認可機能を実現するために Microsoft が提供するライブラリである [MSAL.js :material-open-in-new:](https://www.npmjs.com/package/@azure/msal-browser){ target=_blank } を利用しています。

MSAL.js で提供されている  `loginPopup()` や `loginRedirect()` といった認証用のメソッドを利用すると、将来の使用に備えて以下の秘密情報を永続的なアーティファクトとしてキャッシュします。

- ID トークン
- アクセストークン
- リフレッシュトークン
- アカウント情報（ `homeAccountId` 、 `localAccountId` 、 `ID トークンのクレーム` ）

## キャッシュストレージの保存先設定 {#setting-cache-storage}

MSAL のインスタンス化に使用する構成オブジェクトをもとに、キャッシュストレージの保存先を設定できます。
本サンプルでは、 `src/services/authentication/authentication-config.ts` の以下の部分で設定します。

```typescript title="authentication-config.ts" hl_lines="9"
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

| 設定値 | 保存先 |
| ---------------- | --------------------------------------------- |
| `sessionStorage` | [Web Storage API :material-open-in-new:](https://developer.mozilla.org/ja/docs/Web/API/Web_Storage_API){ target=_blank } が提供する Session Storage に保存する |
| `localStorage`   | [Web Storage API :material-open-in-new:](https://developer.mozilla.org/ja/docs/Web/API/Web_Storage_API){ target=_blank } が提供する Local Storage に保存する |
| `memoryStorage`  | ブラウザーのインメモリに保存する |

キャッシュストレージの保存先の違いによる影響に関しては、 [Store の永続化方式](../../app-architecture/client-side-rendering/global-function/store-design-policy.md) で詳細を確認してください。

!!! Danger "Local Storage を利用する際の危険性"

    キャッシュの保存先に `localStorage` を指定した場合、トークンやアクセス情報といった秘密情報が Local Storage に保存されます。
    これにより、別タブ遷移やリロードでもログイン状態が保持されるためユーザー体験が向上しますが、ユーザーが明示的にログアウトをしない限りキャッシュがクリアされず、秘密情報が XSS 攻撃などのセキュリティ上の脅威にさらされ続ける危険性があります。
    
    上記の危険性から、秘密情報を Local Storage に保存することは望ましくありません。
    よって、 MSAL.js で提供される秘密情報の保存設定は `localStorage` を使用する代わりに、 `sessionStorage` もしくは `memoryStorage` を使用してください。

!!! Warning "Memory Storage を利用する際の注意点"

    キャッシュの保存先に `memoryStorage` を指定した場合、秘密情報がインメモリに保持されるため、ページの更新やナビゲーションでキャッシュがクリアされます。
    これにより、セキュリティが強固になるメリットを享受できる反面、 キャッシュのクリアごとにユーザー認証が必要になり、ユーザー体験が低下する恐れがあります。

    また、MSAL.js で提供されている `loginRedirect()` や `acquireTokenRedirect()` といったリダイレクトフローが利用できず、`loginPopup()` や `acquireTokenPopup()` といったポップアップによる実装が強制されます。
    このような条件の下で、`memoryStorage` を利用するかどうかを検討してください。

## 参照記事 {#reference}

詳細については、以下を確認してください。

- [Caching in MSAL :material-open-in-new:](https://github.com/AzureAD/microsoft-authentication-library-for-js/blob/dev/lib/msal-browser/docs/caching.md){ target=_blank }
