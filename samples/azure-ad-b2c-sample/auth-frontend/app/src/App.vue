<!-- eslint-disable no-alert -->
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
import { useLogger } from './composables/use-logger'

const userStore = useUserStore()
const { getUserId } = storeToRefs(userStore)
const serverTimeStore = useServerTimeStore()
const { getServerTime } = storeToRefs(serverTimeStore)
const authenticationStore = useAuthenticationStore()
const { isAuthenticated } = storeToRefs(authenticationStore)
const handleErrorAsync = useCustomErrorHandler()
const logger = useLogger()

const signIn = async () => {
  try {
    await authenticationService.signInAzureADB2C()
  } catch (error) {
    // ポップアップ画面をユーザーが×ボタンで閉じると、 BrowserAuthError が発生します。
    if (error instanceof BrowserAuthError) {
      // 認証途中でポップアップを閉じることはよくあるユースケースなので、ユーザーには特に通知しません。
      await handleErrorAsync(error, () => {
        logger.info('ユーザーが認証処理を中断しました。')
        authenticationStore.updateAuthenticated(false)
      })
    } else {
      await handleErrorAsync(error, () => {
        window.alert('AzureADB2C での認証に失敗しました。')
      })
    }
  }

  try {
    await fetchUser()
  } catch (error) {
    await handleErrorAsync(error, () => {
      window.alert('ユーザー情報の取得に失敗しました。')
    })
  }
}

async function updateServerTime() {
  try {
    await fetchServerTime()
  } catch (error) {
    await handleErrorAsync(error, () => {
      window.alert('サーバー時刻の更新に失敗しました。')
    })
  }
}

onMounted(async () => {
  try {
    await fetchServerTime()
  } catch (error) {
    await handleErrorAsync(error, () => {
      window.alert('サーバー時刻の取得に失敗しました。')
    })
  }
  try {
    await fetchUser()
  } catch (error) {
    await handleErrorAsync(error, () => {
      window.alert('ユーザー情報の取得に失敗しました。')
    })
  }
})
</script>

<template>
  <header><h1>Azure AD B2C 認証サンプル</h1></header>
  <div>
    <span>現在時刻: {{ getServerTime }}</span>
    <button type="submit" @click="updateServerTime()">更新</button>
  </div>
  <div>
    <button v-if="!isAuthenticated" type="submit" @click="signIn()">ログイン</button>
    <span v-if="isAuthenticated">ユーザーID: {{ getUserId }}</span>
  </div>
</template>
