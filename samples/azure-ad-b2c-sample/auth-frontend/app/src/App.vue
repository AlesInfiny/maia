<script setup lang="ts">
import { onMounted } from 'vue';
import { storeToRefs } from 'pinia';
import { authenticationService } from '@/services/authentication/authentication-service';
import { fetchServerTime } from '@/services/server-time/server-time-service';
import { fetchUser } from './services/user/user-service';
import { useServerTimeStore } from './stores/server-time/server-time';
import { useUserStore } from './stores/user/user';
import { useAuthenticationStore } from './stores/authentication/authentication';

const userStore = useUserStore();
const { getUserId } = storeToRefs(userStore);
const serverTimeStore = useServerTimeStore();
const { getServerTime } = storeToRefs(serverTimeStore);
const authenticationStore = useAuthenticationStore();
const { isAuthenticated } = storeToRefs(authenticationStore);

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
    <span>現在時刻: {{ getServerTime }}</span>
    <button type="submit" @click="updateServerTime()">更新</button>
  </div>
  <div>
    <button v-if="!isAuthenticated" type="submit" @click="signIn()">
      ログイン
    </button>
    <span v-if="isAuthenticated">ユーザーID: {{ getUserId }}</span>
  </div>
</template>
