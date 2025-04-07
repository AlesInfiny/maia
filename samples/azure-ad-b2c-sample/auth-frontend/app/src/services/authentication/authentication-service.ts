import {
  BrowserAuthError,
  InteractionRequiredAuthError,
} from '@azure/msal-browser';
import {
  msalInstance,
  loginRequest,
  tokenRequest,
} from '@/services/authentication/authentication-config';
import { useAuthenticationStore } from '@/stores/authentication/authentication';

msalInstance.initialize();

export const authenticationService = {
  async signInAzureADB2C() {
    const authenticationStore = useAuthenticationStore();
    try {
      const response = await msalInstance.loginPopup(loginRequest);
      msalInstance.setActiveAccount(response.account);
      authenticationStore.updateAuthenticated(true);
      return true;
    } catch (error) {
      if (error instanceof BrowserAuthError) {
        authenticationStore.updateAuthenticated(false);
        return false;
      }
      throw error;
    }
  },

  async isAuthenticated(): Promise<boolean> {
    const result = msalInstance.getActiveAccount() !== null;
    const authenticationStore = useAuthenticationStore();
    authenticationStore.updateAuthenticated(result);
    return result;
  },

  async getTokenAzureADB2C() {
    const account = msalInstance.getActiveAccount();

    tokenRequest.account = account ?? undefined;
    try {
      const tokenResponse = await msalInstance.acquireTokenSilent(tokenRequest);

      if (!tokenResponse.accessToken || tokenResponse.accessToken === '') {
        throw new InteractionRequiredAuthError('accessToken is null or empty.');
      }
      return tokenResponse.accessToken;
    } catch (error) {
      if (error instanceof InteractionRequiredAuthError) {
        // ユーザーによる操作が必要な場合にスローされるエラーがスローされた場合、トークン呼び出しポップアップ画面を表示する。
        const tokenResponse =
          await msalInstance.acquireTokenPopup(tokenRequest);
        return tokenResponse.accessToken;
      }
      // eslint-disable-next-line no-console
      console.error(error);
      throw error;
    }
  },
};
