import { LogLevel } from '@azure/msal-browser';

export const b2cPolicies = {
  names: {
    signUpSignIn: process.env.USER_FLOW_SIGN_IN
      ? process.env.USER_FLOW_SIGN_IN
      : '',
    editProfile: process.env.USER_FLOW_EDIT_PROFILE
      ? process.env.USER_FLOW_SIGN_IN
      : '',
  },
  authorities: {
    signUpSignIn: {
      authority: process.env.ADB2C_SIGN_IN_URI,
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
};

export const apiConfig = {
  b2cScopes: [
    process.env.ADB2C_TASKS_SCOPE ? process.env.ADB2C_TASKS_SCOPE : '',
  ],
  getMessageAPI: process.env.API_GET_MESSAGE_URI,
};

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
