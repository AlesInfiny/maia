import { InteractionRequiredAuthError } from '@azure/msal-browser';
import {
  msalInstance,
  loginRequest,
  tokenRequest,
} from './authentication-config';

msalInstance.initialize();

export async function signInAzureADB2C(): Promise<AuthenticationResult> {
  const result = new AuthenticationResult();
  try {
    const loginResponse = await msalInstance.loginPopup(loginRequest);
    result.homeAccountId = loginResponse.account.homeAccountId;
    result.idToken = loginResponse.idToken;
    result.isAuthenticated = true;
    return result;
  } catch (error) {
    result.isAuthenticated = false;
    return result;
  }
}

export async function getTokenAzureADB2C(
  homeAccountId: string,
): Promise<AuthenticationResult> {
  const account = msalInstance.getAccountByHomeId(homeAccountId);
  tokenRequest.account = account;

  const result = new AuthenticationResult();

  try {
    const tokenResponse = await msalInstance.acquireTokenSilent(tokenRequest);

    if (!tokenResponse.accessToken || tokenResponse.accessToken === '') {
      throw new InteractionRequiredAuthError('accessToken is null or empty.');
    }

    result.homeAccountId = tokenResponse.account.homeAccountId;
    result.accessToken = tokenResponse.accessToken;
    result.idToken = tokenResponse.idToken;
    result.isAuthenticated = true;
    return result;
  } catch (error) {
    if (error instanceof InteractionRequiredAuthError) {
      // ユーザーによる操作が必要な場合にスローされるエラーがスローされた場合、トークン呼び出しポップアップ画面を表示する。
      const tokenResponse = await msalInstance.acquireTokenPopup(tokenRequest);
      result.homeAccountId = tokenResponse.account.homeAccountId;
      result.accessToken = tokenResponse.accessToken;
      result.idToken = tokenResponse.idToken;
      result.isAuthenticated = true;
      return result;
    }
    console.log(error);
    throw error;
  }
}

export class AuthenticationResult {
  homeAccountId: string;
  accessToken: string;
  idToken: string;
  isAuthenticated: boolean;
}
