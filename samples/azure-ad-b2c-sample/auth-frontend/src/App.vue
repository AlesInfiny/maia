<script setup lang="ts">
import { useAuthenticationStore } from '@/stores/authentication/authentication';
import { useUserStore } from './stores/user/user';
import { useServerTimeStore } from './stores/serverTime/serverTime';
import { onMounted } from 'vue';

const authenticationStore = useAuthenticationStore();
const userStore = useUserStore();
const serverTimeStore = useServerTimeStore();

const isAuthenticated = () => {
  return authenticationStore.isAuthenticated;
};
const signIn = async () => {
  await authenticationStore.signIn();
  if (authenticationStore.isAuthenticated) {
    await userStore.fetchUserResponse();
  }
};
const getServerTime = () => {
  return serverTimeStore.getServerTime;
};

const getUserId = () => {
  return userStore.getUserId;
};

const updateServerTime = async () => {
  await serverTimeStore.fetchServerTimeResponse();
};

onMounted(async () => {
  await serverTimeStore.fetchServerTimeResponse();
});
</script>

<template>
  <header><h1>Azure AD B2C 認証サンプル</h1></header>
  <div>
    <span>現在時刻: {{ getServerTime() }}</span>
    <button @click="updateServerTime()">更新</button>
  </div>
  <div>
    <button v-if="!isAuthenticated()" @click="signIn()">ログイン</button>
    <span v-if="isAuthenticated()">ユーザーID: {{ getUserId() }}</span>
  </div>
</template>
