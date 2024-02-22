import { LogLevel } from '@azure/msal-browser';

export const b2cPolicies = {
  names: {
    signUpSignIn: import.meta.env.USER_FLOW_SIGN_IN
      ? import.meta.env.dev.USER_FLOW_SIGN_IN
      : '',
  },
  authorities: {
    signUpSignIn: {
      authority: import.meta.env.ADB2C_SIGN_IN_URI,
    },
  },
  authorityDomain: import.meta.env.ADB2C_AUTHORITY_DOMAIN
    ? import.meta.env.ADB2C_AUTHORITY_DOMAIN
    : '',
};

export const apiConfig = {
  b2cScopes: [
    import.meta.env.ADB2C_TASKS_SCOPE ? import.meta.env.ADB2C_TASKS_SCOPE : '',
  ],
};

export const msalConfig = {
  auth: {
    clientId: import.meta.env.ADB2C_APP_CLIENT_ID
      ? import.meta.env.ADB2C_APP_CLIENT_ID
      : '',
    authority: b2cPolicies.authorities.signUpSignIn.authority,
    knownAuthorities: [b2cPolicies.authorityDomain],
    redirectUri: import.meta.env.APP_URI,
  },
  cache: {
    cacheLocation: 'sessionStorage',
    storeAuthStateInCookie: true,
  },
  system: {
    loggerOptions: {
      loggerCallback: (
        level: unknown,
        message: unknown,
        containsPii: unknown,
      ) => {
        if (containsPii) {
          return;
        }
        switch (level) {
          case LogLevel.Error:
            console.error(message);
            return;
          case LogLevel.Info:
            console.info(message);
            return;
          case LogLevel.Verbose:
            console.debug(message);
            return;
          case LogLevel.Warning:
            console.warn(message);
            return;
        }
      },
    },
  },
};
export const loginRequest = {
  scopes: ['openId', ...apiConfig.b2cScopes],
};
export const tokenRequest = {
  scopes: [...apiConfig.b2cScopes],
  forceRefresh: false,
};
