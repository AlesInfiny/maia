import {
  PublicClientApplication,
  InteractionRequiredAuthError,
  AccountInfo,
} from '@azure/msal-browser'
import axios from 'axios'
import { useUserStore } from '../store/user'
import { AuthConst } from './authConfig'

const myMSALObj = new PublicClientApplication(AuthConst.msalConfig)

/**
 * ログイン中ユーザのアカウント情報をstoreにセットする
 *
 * @param account アカウント情報
 */
function setAccount(account: AccountInfo) {
  const userStore = useUserStore()

  // ログイン中のユーザ情報をstoreにセットする
  userStore.setAccountHomeId(account.homeAccountId)
}

/**
 * ログイン中ユーザのアカウント情報を選択する
 *
 * @param account アカウント情報
 */
function selectAccount() {
  const currentAccounts: AccountInfo[] = myMSALObj.getAllAccounts()
  if (currentAccounts.length > 1) {
    const accounts = currentAccounts.filter(
      (account) =>
        account.idTokenClaims &&
        account.idTokenClaims.iss &&
        account.homeAccountId
          .toUpperCase()
          .includes(AuthConst.b2cPolicies.names.signUpSignIn.toUpperCase()) &&
        account.idTokenClaims.iss
          .toUpperCase()
          .includes(AuthConst.b2cPolicies.authorityDomain.toUpperCase()) &&
        account.idTokenClaims.aud === AuthConst.msalConfig.auth.clientId
    )
    if (accounts.length > 1) {
      // localAccountId identifies the entity for which the token asserts information.
      if (
        accounts.every(
          (account) => account.localAccountId === accounts[0].localAccountId
        )
      ) {
        // All accounts belong to the same user
        setAccount(accounts[0])
      } else {
        // Multiple users detected. Logout all to be safe.
        signOut()
      }
    } else if (accounts.length === 1) {
      setAccount(accounts[0])
    }
  } else if (currentAccounts.length === 1) {
    setAccount(currentAccounts[0])
  }
}

/**
 * ログイン画面を表示しログインを実行する
 *
 */
async function signIn() {
  const loginRes = await myMSALObj.loginPopup(AuthConst.loginRequest)

  if (loginRes !== null && loginRes.account) {
    setAccount(loginRes.account)
  } else {
    selectAccount()
  }
}

/**
 * ログアウトを実行する
 *
 */
function signOut() {
  const logoutRequest = {
    postLogoutRedirectUri: AuthConst.msalConfig.auth.redirectUri,
    mainWindowRedirectUri: AuthConst.msalConfig.auth.redirectUri,
  }
  myMSALObj.logoutPopup(logoutRequest)
}

/**
 * アクセストークンを取得する
 *
 * @pram request トークン取得時にセットするリクエスト
 *
 */
function getTokenPopup(request: any) {
  const userStore = useUserStore()
  const accountHomeId = userStore.getAccountHomeId()
  request.account = myMSALObj.getAccountByHomeId(accountHomeId)

  return myMSALObj
    .acquireTokenSilent(request)
    .then((response) => {
      if (!response.accessToken || response.accessToken === '') {
        throw new InteractionRequiredAuthError()
      }
      return response
    })
    .catch((error) => {
      console.log(
        'Silent token acquisition fails. Acquiring token using popup. \n',
        error
      )

      if (error instanceof InteractionRequiredAuthError) {
        return myMSALObj
          .acquireTokenPopup(request)
          .then((response) => {
            return response
          })
          .catch((error) => {
            console.log(error)
            throw error
          })
      } else {
        console.log(error)
        throw error
      }
    })
}

/**
 * メッセージ取得APIを呼び出す
 *
 */
async function getLoginMessage() {
  const loginElem = document.getElementById('login')
  if (loginElem) {
    try {
      const resGetToken = await getTokenPopup(AuthConst.tokenRequest)

      const bearer = `Bearer ${resGetToken.accessToken}`
      const getMessageURL = AuthConst.apiConfig.getMessageAPI

      if (getMessageURL) {
        const getMessageRes = await axios.get(getMessageURL, {
          headers: {
            Authorization: bearer,
          },
        })

        loginElem.innerText = getMessageRes.data
      }
    } catch (err) {
      loginElem.innerText = ''

      throw err
    }
  }
}

/**
 * ユーザ編集画面を表示し、ユーザ情報を更新する
 *
 */
async function editProfile() {
  const userStore = useUserStore()
  const aaduserHomeId = userStore.getAccountHomeId()

  const loginUser = myMSALObj.getAccountByHomeId(aaduserHomeId)

  const editProfileRequest = {
    scopes: ['openid', ...AuthConst.apiConfig.b2cScopes],
    authority: AuthConst.b2cPolicies.authorities.editProfile.authority,
    loginHint: loginUser?.username,
  }

  try {
    await myMSALObj.loginPopup(editProfileRequest)
  } catch (error: unknown) {
    if (error instanceof Error) {
      if (
        // 編集画面でキャンセルを押下した場合と×ボタンを押下した場合を考慮する
        error.message?.indexOf('AADB2C90091') !== -1 ||
        error.message?.indexOf('cancel') !== -1
      ) {
        return
      }
    }
    throw error
  }
}

/**
 * ユーザ削除を実行する
 *
 */
async function deleteUser() {
  const resGetToken = await getTokenPopup(AuthConst.tokenRequest)

  const bearer = `Bearer ${resGetToken.accessToken}`
  const deleteURL = AuthConst.apiConfig.deleteUserAPI

  if (deleteURL) {
    try {
      await axios.delete(deleteURL, {
        headers: {
          Authorization: bearer,
        },
      })

      // ユーザ削除実行後にログアウトする
      signOut()
    } catch (error) {
      console.log(error)
      return error
    }
  }
}

// リロード時に読み込む
window.addEventListener('DOMContentLoaded', () => {
  selectAccount()
})

export default {
  signIn,
  signOut,
  getLoginMessage,
  editProfile,
  deleteUser,
  getTokenPopup,
}
