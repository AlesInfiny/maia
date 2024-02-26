import { useHomeAccountIdStore } from '@/stores/login/home-account-id';
import { AccountInfo, PublicClientApplication } from '@azure/msal-browser';
import { useUserStore } from '@/stores/login/user-id';
import {
  msalConfig,
  loginRequest,
  tokenRequest,
} from './authentication-config';

const myMSALObj = new PublicClientApplication(msalConfig);

async function setAccount(account: AccountInfo) {
  const homeAccountIdStore = useHomeAccountIdStore();
  homeAccountIdStore.setHomeAccountId(account.homeAccountId);
}

export async function signIn() {
  await myMSALObj.initialize();
  const loginRes = await myMSALObj.loginPopup(loginRequest);
  if (loginRes !== null && loginRes.account) {
    setAccount(loginRes.account.homeAccountId);
  } else {
    setAccount();
  }
}

export async function getUserId() {
  const loginElem = document.getElementById('login');
  if (loginElem) {
    try {
      const resGetToken = await getTokenPopup(tokenRequest);
      const bearer = `Bearer ${resGetToken.accessToken}`;
      const userStore = useUserStore(bearer);
      await userStore.fetchUserResponse();
      const userIdRes = userStore.response?.userId;
      loginElem.innerText = userIdRes ?? 'No UserID';
    } catch (err) {
      loginElem.innerText = 'error occurred';
      throw err;
    }
  }
}

async function getTokenPopup(request: unknown) {
  const accountHomeIdStore = useAccountHomeIdStore();
  const accountHomeId = accountHomeIdStore.getAccountHomeId();
  request.account = myMSALObj.getAccountByHomeId(accountHomeId);

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
