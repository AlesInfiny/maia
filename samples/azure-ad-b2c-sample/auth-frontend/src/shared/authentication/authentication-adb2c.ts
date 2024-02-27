import { AccountInfo, PublicClientApplication } from '@azure/msal-browser';
import { useUserStore } from '@/stores/user/user';
import { useHomeAccountIdStore } from '@/stores/user/home-account-id';
import { msalConfig, loginRequest } from './authentication-config';

const myMSALObj = new PublicClientApplication(msalConfig);

async function setAccount(account: AccountInfo) {
  const homeAccountIdStore = useHomeAccountIdStore();
  homeAccountIdStore.setHomeAccountId(account.homeAccountId);
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

export async function getUserId() {
  const loginElem = document.getElementById('login');
  if (loginElem) {
    try {
      const userStore = useUserStore();
      await userStore.fetchUserResponse();
      const userIdRes = userStore.getUserId;
      loginElem.innerText = userIdRes ?? 'No UserID';
    } catch (err) {
      loginElem.innerText = 'error occurred';
      throw err;
    }
  }
}

export async function getTokenPopup(request: unknown) {
  const homeAccountIdStore = useHomeAccountIdStore();
  const homeAccountId = homeAccountIdStore.getHomeAccountId;
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
