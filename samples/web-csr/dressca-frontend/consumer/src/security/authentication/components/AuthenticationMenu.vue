<script setup lang="ts">
import type { RouteLocationRaw } from 'vue-router'
import { storeToRefs } from 'pinia'
import { useAuthenticationStore } from '../stores/authentication'
import { useAuthentication } from '../composables/use-authentication'

defineProps<{
  loginLocation: RouteLocationRaw
}>()

const emit = defineEmits<{
  /** ログアウト後の画面遷移は利用する側が行います。 */
  loggedOut: []
}>()

const { isAuthenticated } = storeToRefs(useAuthenticationStore())
const { signOut } = useAuthentication()

const logout = () => {
  signOut()
  emit('loggedOut')
}
</script>

<template>
  <router-link v-if="!isAuthenticated" :to="loginLocation"> ログイン </router-link>
  <button v-else type="button" class="cursor-pointer" @click="logout">ログアウト</button>
</template>
