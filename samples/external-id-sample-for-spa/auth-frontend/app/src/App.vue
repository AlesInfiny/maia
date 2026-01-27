<!-- eslint-disable no-alert -->
<script setup lang="ts">
import { onMounted } from 'vue'
import { storeToRefs } from 'pinia'
import { authenticationService } from '@/services/authentication/authentication-service'
import { fetchServerTime } from '@/services/server-time/server-time-service'
import { useCustomErrorHandler } from '@/shared/error-handler/custom-error-handler'
import { fetchUser } from './services/user/user-service'
import { useLogger } from './composables/use-logger'
import { useServerTimeStore } from './stores/server-time/server-time'
import { useUserStore } from './stores/user/user'
import { BrowserAuthError } from '@azure/msal-browser'

const userStore = useUserStore()
const { getUserId } = storeToRefs(userStore)
const serverTimeStore = useServerTimeStore()
const { getServerTime } = storeToRefs(serverTimeStore)
const handleErrorAsync = useCustomErrorHandler()
const { signIn, signOut, isAuthenticated } = authenticationService()
const logger = useLogger()

const signInButtonClicked = async () => {
  try {
    await signIn()
  } catch (error) {
    // ポップアップ画面をユーザーが×ボタンで閉じると、 BrowserAuthError が発生します。
    if (error instanceof BrowserAuthError) {
      // 認証途中でポップアップを閉じることはよくあるユースケースなので、ユーザーには特に通知しません。
      await handleErrorAsync(error, () => {
        logger.info('ユーザーが認証処理を中断しました。')
      })
    } else {
      await handleErrorAsync(error, () => {
        window.alert('Microsoft Entra External Id での認証に失敗しました。')
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

const signOutButtonClicked = async () => {
  try {
    await signOut()
  } catch (error) {
    // ポップアップ画面をユーザーが×ボタンで閉じると、 BrowserAuthError が発生します。
    if (error instanceof BrowserAuthError) {
      // 認証途中でポップアップを閉じることはよくあるユースケースなので、ユーザーには特に通知しません。
      await handleErrorAsync(error, () => {
        logger.info('ユーザーが認証処理を中断しました。')
      })
    } else {
      await handleErrorAsync(error, () => {
        window.alert('Microsoft Entra External Id での認証に失敗しました。')
      })
    }
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
  <header><h1>Microsoft Entra External Id 認証サンプル</h1></header>
  <div>
    <span>現在時刻: {{ getServerTime }}</span>
    <button type="submit" @click="updateServerTime()">更新</button>
  </div>
  <div>
    <button v-if="!isAuthenticated()" type="submit" @click="signInButtonClicked">ログイン</button>
    <span v-if="isAuthenticated()">
      ユーザーID: {{ getUserId }}
      <button type="submit" @click="signOutButtonClicked">ログアウト</button>
    </span>
  </div>
</template>
