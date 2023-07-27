import { LogLevel } from '@azure/msal-browser'

/** システム定数クラス */
export namespace AuthConst {
  /**
   * AzureADB2Cのポリシー設定
   */
  export const b2cPolicies = {
    names: {
      signUpSignIn: process.env.USER_FLOW_SIGNIN
        ? process.env.USER_FLOW_SIGNIN
        : '',
      editProfile: process.env.USER_FLOW_EDIT_PROFILE
        ? process.env.USER_FLOW_SIGNIN
        : '',
    },
    authorities: {
      signUpSignIn: {
        authority: process.env.ADB2C_SIGNIN_URI,
      },
      editProfile: {
        authority: process.env.ADB2C_EDIT_PROFILE_URI
          ? process.env.ADB2C_EDIT_PROFILE_URI
          : '',
      },
    },
    authorityDomain: process.env.ADB2C_AUTHORITY_DOMAIN
      ? process.env.ADB2C_AUTHORITY_DOMAIN
      : '',
  }

  /**
   * API設定
   */
  export const apiConfig = {
    b2cScopes: [
      process.env.ADB2C_TASKS_SCOPE ? process.env.ADB2C_TASKS_SCOPE : '',
    ],
    getMessageAPI: process.env.API_GET_MESSAGE_URI,
    deleteUserAPI: process.env.API_DELETE_USER_URI,
  }

  /**
   * msal設定
   */
  export const msalConfig = {
    auth: {
      clientId: process.env.ADB2C_APP_CLIENT_ID
        ? process.env.ADB2C_APP_CLIENT_ID
        : '',
      authority: b2cPolicies.authorities.signUpSignIn.authority,
      knownAuthorities: [b2cPolicies.authorityDomain],
      redirectUri: process.env.APP_URI,
    },
    cache: {
      cacheLocation: 'sessionStorage',
      storeAuthStateInCookie: true,
    },
    system: {
      loggerOptions: {
        loggerCallback: (level: any, message: any, containsPii: any) => {
          if (containsPii) {
            return
          }
          switch (level) {
            case LogLevel.Error:
              console.error(message)
              return
            case LogLevel.Info:
              console.info(message)
              return
            case LogLevel.Verbose:
              console.debug(message)
              return
            case LogLevel.Warning:
              console.warn(message)
          }
        },
      },
    },
  }

  /**
   * ログイン時に指定するリクエスト
   */
  export const loginRequest = {
    scopes: ['openid', ...apiConfig.b2cScopes],
  }

  /**
   * トークン取得時に指定するリクエスト
   */
  export const tokenRequest = {
    scopes: [...apiConfig.b2cScopes],
    forceRefresh: false, // trueを設定すると、トークン取得の度にサーバからトークンを取得します(falseの場合はキャッシュ)
  }
}
