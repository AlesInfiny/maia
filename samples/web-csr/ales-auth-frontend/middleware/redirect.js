import {
  PublicClientApplication,
  InteractionRequiredAuthError,
} from '@azure/msal-browser'
import { AuthConst } from '../auth/authConfig'

const myMSALObj = new PublicClientApplication(AuthConst.msalConfig)

export default async function ({ redirect, store, route }) {
  // sessionStorageからユーザ情報を取得
  const sessionLoginUser = sessionStorage.getItem('user')
  const loginUserObj = JSON.parse(sessionLoginUser)

  const tokenRequest = {
    scopes: [...AuthConst.apiConfig.b2cScopes],
    forceRefresh: false,
  }

  let isLogin = true
  if (sessionLoginUser) {
    tokenRequest.account = await myMSALObj.getAccountByHomeId(
      loginUserObj.accountHomeId
    )

    // accsessTokenを取得可能かどうか(≒ユーザが有効か)を確認
    // アクセストークンが取得できない場合は、ログインフラグをfalseにする
    let resGetToken
    try {
      resGetToken = await myMSALObj.acquireTokenSilent(tokenRequest)
    } catch (error) {
      if (error instanceof InteractionRequiredAuthError) {
        myMSALObj.acquireTokenPopup(tokenRequest)
        return myMSALObj.acquireTokenPopup(tokenRequest).catch(() => {
          isLogin = false
        })
      } else {
        isLogin = false
      }
    }

    if (
      !resGetToken ||
      !resGetToken.accessToken ||
      resGetToken.accessToken === ''
    ) {
      isLogin = false
    }
  }

  // 未ログインかつTOPページ以外へのアクセスは全てTOPページに遷移する
  if (
    (!sessionLoginUser || !isLogin) &&
    route.path !== '/' &&
    route.path !== '/sampledemo/market'
  ) {
    redirect('http://localhost:3000/sampledemo/market')
  }
}
