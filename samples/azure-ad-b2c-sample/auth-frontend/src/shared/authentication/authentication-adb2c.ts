import {
  BrowserAuthError,
  InteractionRequiredAuthError,
} from '@azure/msal-browser';
import {
  msalInstance,
  loginRequest,
  tokenRequest,
} from './authentication-config';

msalInstance.initialize();

export async function signInAzureADB2C(): Promise<AuthenticationResult> {
  const result = {} as AuthenticationResult;
  try {
    const loginResponse = await msalInstance.loginPopup(loginRequest);
    result.homeAccountId = loginResponse.account.homeAccountId;
    result.isAuthenticated = true;
    return result;
  } catch (error) {
    if (error instanceof BrowserAuthError) {
      result.isAuthenticated = false;
      return result;
    }
    throw error;
  }
}

export async function getTokenAzureADB2C(
  homeAccountId: string,
): Promise<AuthenticationResult> {
  const account = msalInstance.getAccountByHomeId(homeAccountId);
  tokenRequest.account = account ?? undefined;

  const result = {} as AuthenticationResult;

  try {
    const tokenResponse = await msalInstance.acquireTokenSilent(tokenRequest);

    if (!tokenResponse.accessToken || tokenResponse.accessToken === '') {
      throw new InteractionRequiredAuthError('accessToken is null or empty.');
    }

    result.homeAccountId = tokenResponse.account.homeAccountId;
    result.accessToken = tokenResponse.accessToken;
    result.isAuthenticated = true;
    return result;
  } catch (error) {
    if (error instanceof InteractionRequiredAuthError) {
      // ユーザーによる操作が必要な場合にスローされるエラーがスローされた場合、トークン呼び出しポップアップ画面を表示する。
      const tokenResponse = await msalInstance.acquireTokenPopup(tokenRequest);
      result.homeAccountId = tokenResponse.account.homeAccountId;
      result.accessToken = tokenResponse.accessToken;
      result.isAuthenticated = true;
      return result;
    }
    console.log(error);
    throw error;
  }
}

export interface AuthenticationResult {
  homeAccountId: string;
  accessToken: string;
  isAuthenticated: boolean;
}
