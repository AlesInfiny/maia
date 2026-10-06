<script setup lang="ts">
/**
 * ログイン状態に応じて、ログインのリンクかログアウトのボタンを表示するメニューです。
 */
import type { RouteLocationRaw } from 'vue-router'
import { storeToRefs } from 'pinia'
import { useAuthenticationStore } from '@/security/authentication/stores/authentication'
import { authenticationService } from '@/security/authentication/services/authentication-service'

defineProps<{
  /**
   * ログインのリンクの遷移先です。
   */
  loginLocation: RouteLocationRaw
}>()

const emit = defineEmits<{
  /**
   * ログアウトしたときに発行します。ログアウト後の画面遷移は利用する側が行います。
   */
  loggedOut: []
}>()

const { isAuthenticated } = storeToRefs(useAuthenticationStore())
const { signOut } = authenticationService()

/**
 * アプリケーションからログアウトします。
 */
const logout = () => {
  signOut()
  emit('loggedOut')
}
</script>

<template>
  <router-link v-if="!isAuthenticated" :to="loginLocation"> ログイン </router-link>
  <button v-else type="button" class="cursor-pointer" @click="logout">ログアウト</button>
</template>
