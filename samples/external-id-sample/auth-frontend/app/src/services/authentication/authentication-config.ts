/* eslint-disable no-console */
import { LogLevel, PublicClientApplication, type SilentRequest } from '@azure/msal-browser'

export const b2cPolicies = {
  names: {
    signUpSignIn: import.meta.env.VITE_ADB2C_USER_FLOW_SIGN_IN,
  },
  authorities: {
    signUpSignIn: {
      authority: import.meta.env.VITE_ADB2C_SIGN_IN_URI,
    },
  },
  authorityDomain: import.meta.env.VITE_ADB2C_AUTHORITY_DOMAIN,
}

export const apiConfig = {
  b2cScopes: [import.meta.env.VITE_ADB2C_SCOPE],
}

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
  system: {
    loggerOptions: {
      loggerCallback: (level: LogLevel, message: string, containsPii: boolean) => {
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
            break
          default:
        }
      },
      logLevel: LogLevel.Verbose,
    },
  },
}

export const msalInstance = new PublicClientApplication(msalConfig)

export const loginRequest: SilentRequest = {
  scopes: ['openId', ...apiConfig.b2cScopes],
}

export const tokenRequest: SilentRequest = {
  scopes: [...apiConfig.b2cScopes],
}
