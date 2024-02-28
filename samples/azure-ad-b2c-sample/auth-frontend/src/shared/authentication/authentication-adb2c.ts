import { AccountInfo, PublicClientApplication } from '@azure/msal-browser';
import { useAuthenticationStore } from '@/stores/authentication/authentication';
import { msalConfig, loginRequest } from './authentication-config';

const myMSALObj = new PublicClientApplication(msalConfig);

async function setAccount(account: AccountInfo) {
  const authenticationStore = useAuthenticationStore();
  authenticationStore.setHomeAccountId(account.homeAccountId);
}

export async function signIn() {
  await myMSALObj.initialize();
  const loginRes = await myMSALObj.loginPopup(loginRequest);
  if (loginRes !== null && loginRes.account) {
    setAccount(loginRes.account);
  } else {
    setAccount();
  }
}

export async function getTokenPopup(request: unknown) {
  const authenticationStore = useAuthenticationStore();
  const homeAccountId = authenticationStore.getHomeAccountId;
  request.account = myMSALObj.getAccountByHomeId(homeAccountId);

  return myMSALObj
    .acquireTokenSilent(request)
    .then((response) => {
      if (!response.accessToken || response.accessToken === '') {
        throw new InteractionRequiredAuthError();
      }
      return response;
    })
    .catch((error) => {
      console.log(
        'Silent token acquisition fails. Acquiring token using popup. \n',
        error,
      );

      if (error instanceof InteractionRequiredAuthError) {
        return myMSALObj
          .acquireTokenPopup(request)
          .then((response) => {
            return response;
          })
          .catch((error) => {
            console.log(error);
            throw error;
          });
      } else {
        console.log(error);
        throw error;
      }
    });
}
