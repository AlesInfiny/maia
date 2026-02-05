import { LogLevel, PublicClientApplication, type SilentRequest } from '@azure/msal-browser'
import { useLogger } from '@/composables/use-logger'

const logger = useLogger()

export const msalConfig = {
  auth: {
    clientId: import.meta.env.VITE_EXTERNAL_ID_APP_CLIENT_ID,
    authority: import.meta.env.VITE_EXTERNAL_ID_AUTHORITY_DOMAIN,
    redirectUri: import.meta.env.VITE_EXTERNAL_ID_REDIRECT_URI,
    postLogoutRedirectUri: import.meta.env.VITE_EXTERNAL_ID_LOGOUT_REDIRECT_URI,
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
            logger.error(message)
            return
          case LogLevel.Info:
            logger.info(message)
            return
          case LogLevel.Verbose:
            logger.debug(message)
            return
          case LogLevel.Warning:
            logger.warn(message)
            break
          default:
        }
      },
      logLevel: LogLevel.Verbose,
    },
  },
}

export const apiConfig = {
  scopes: import.meta.env.VITE_EXTERNAL_ID_SCOPE?.split(',') ?? [],
}

export const msalInstance = new PublicClientApplication(msalConfig)

export const loginRequest: SilentRequest = {
  scopes: ['openId', 'email', ...apiConfig.scopes],
}
export const tokenRequest: SilentRequest = {
  scopes: ['openId', 'email', ...apiConfig.scopes],
}
