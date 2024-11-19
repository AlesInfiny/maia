<script setup lang="ts">
import { onMounted } from 'vue';
import { authenticationService } from '@/services/authentication/authentication-service';
import { fetchServerTime } from '@/services/server-time/server-time-service';
import { fetchUser } from './services/user/user-service';
import { useServerTimeStore } from './stores/server-time/server-time';
import { useUserStore } from './stores/user/user';
import { useAuthenticationStore } from './stores/authentication/authentication';

const userStore = useUserStore();
const serverTimeStore = useServerTimeStore();
const authenticationStore = useAuthenticationStore();

const signIn = async () => {
  await authenticationService.signInAzureADB2C();
  await fetchUser();
};

async function updateServerTime() {
  await fetchServerTime();
}
onMounted(async () => {
  await fetchServerTime();
  await fetchUser();
});
</script>

<template>
  <header><h1>Azure AD B2C 認証サンプル</h1></header>
  <div>
    <span>現在時刻: {{ serverTimeStore.getServerTime }}</span>
    <button type="submit" @click="updateServerTime()">更新</button>
  </div>
  <div>
    <button
      v-if="!authenticationStore.isAuthenticated"
      type="submit"
      @click="signIn()"
    >
      ログイン
    </button>
    <span v-if="authenticationStore.isAuthenticated"
      >ユーザーID: {{ userStore.getUserId }}</span
    >
  </div>
</template>
