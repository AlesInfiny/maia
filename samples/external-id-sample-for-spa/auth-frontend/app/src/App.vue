<!-- eslint-disable no-alert -->
<!-- eslint-disable no-console -->
<!--  このサンプルコードでは、ログ出力先としてコンソール、ユーザーへの通知先としてブラウザの標準ダイアログを使用するので、ファイル全体に対して ESLint の設定を無効化しておきます。
実際のアプリケーションでは、適切なログ出力先や、通知先のコンポーネントを使用してください。-->
<script setup lang="ts">
import { onMounted } from 'vue'
import { storeToRefs } from 'pinia'
import { authenticationService } from '@/services/authentication/authentication-service'
import { fetchServerTime } from '@/services/server-time/server-time-service'
import { useCustomErrorHandler } from '@/shared/error-handler/custom-error-handler'
import { BrowserAuthError } from '@azure/msal-browser'
import { fetchUser } from './services/user/user-service'
import { useServerTimeStore } from './stores/server-time/server-time'
import { useUserStore } from './stores/user/user'
import { useAuthenticationStore } from './stores/authentication/authentication'

const userStore = useUserStore()
const { getUserId } = storeToRefs(userStore)
const serverTimeStore = useServerTimeStore()
const { getServerTime } = storeToRefs(serverTimeStore)
const authenticationStore = useAuthenticationStore()
const { isAuthenticated } = storeToRefs(authenticationStore)
const customErrorHandler = useCustomErrorHandler()

const signIn = async () => {
  try {
    await authenticationService.signInEntraExternalId()
  } catch (error) {
    // ポップアップ画面をユーザーが×ボタンで閉じると、 BrowserAuthError が発生します。
    if (error instanceof BrowserAuthError) {
      // 認証途中でポップアップを閉じることはよくあるユースケースなので、ユーザーには特に通知しません。
      customErrorHandler.handle(error, () => {
        console.info('ユーザーが認証処理を中断しました。')
        authenticationStore.updateAuthenticated(false)
      })
    } else {
      customErrorHandler.handle(error, () => {
        window.alert('Microsoft Entra External Id での認証に失敗しました。')
      })
    }
  }

  try {
    await fetchUser()
  } catch (error) {
    customErrorHandler.handle(error, () => {
      window.alert('ユーザー情報の取得に失敗しました。')
    })
  }
}

const signOut = async () => {
  try {
    await authenticationService.signOutEntraExternalId()
  } catch (error) {
    // ポップアップ画面をユーザーが×ボタンで閉じると、 BrowserAuthError が発生します。
    if (error instanceof BrowserAuthError) {
      // 認証途中でポップアップを閉じることはよくあるユースケースなので、ユーザーには特に通知しません。
      customErrorHandler.handle(error, () => {
        console.info('ユーザーが認証処理を中断しました。')
      })
    } else {
      customErrorHandler.handle(error, () => {
        window.alert('Microsoft Entra External Id での認証に失敗しました。')
      })
    }
  }
}

async function updateServerTime() {
  try {
    await fetchServerTime()
  } catch (error) {
    customErrorHandler.handle(error, () => {
      window.alert('サーバー時刻の更新に失敗しました。')
    })
  }
}

onMounted(async () => {
  try {
    await fetchServerTime()
  } catch (error) {
    customErrorHandler.handle(error, () => {
      window.alert('サーバー時刻の取得に失敗しました。')
    })
  }
  try {
    await fetchUser()
  } catch (error) {
    customErrorHandler.handle(error, () => {
      window.alert('ユーザー情報の取得に失敗しました。')
    })
  }
})
</script>

<template>
  <header><h1>Microsoft Entra External Id 認証サンプル</h1></header>
  <div>
    <span>現在時刻: {{ getServerTime }}</span>
    <button type="submit" @click="updateServerTime()">更新</button>
  </div>
  <div>
    <button v-if="!isAuthenticated" type="submit" @click="signIn()">ログイン</button>
    <span v-if="isAuthenticated">
      ユーザーID: {{ getUserId }}
      <button type="submit" @click="signOut()">ログアウト</button>
    </span>
  </div>
</template>
