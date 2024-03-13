<script setup lang="ts">
import { useAuthenticationStore } from '@/stores/authentication/authentication';
import { useUserStore } from './stores/user/user';
import { useTimeStore } from './stores/time/time';
import { onMounted } from 'vue';

const authenticationStore = useAuthenticationStore();
const userStore = useUserStore();
const timeStore = useTimeStore();

const isAuthenticated = () => {
  return authenticationStore.isAuthenticated;
};
const signIn = async () => {
  await authenticationStore.signIn();
  if (authenticationStore.isAuthenticated) {
    await userStore.fetchUserResponse();
  }
};

const getTime = () => {
  return timeStore.getTime;
};

const getUserId = () => {
  return userStore.getUserId;
};

onMounted(async () => {
  await timeStore.fetchTimeResponse();
});
</script>

<template>
  <div class="flex flex-col h-screen justify-between">
    <header>
      <nav
        aria-label="Jump links"
        class="text-lg font-medium text-gray-900 py-5 ring-1 ring-gray-900 ring-opacity-5 shadow-sm"
      >
        <div class="mx-auto flex justify-between px-4 md:px-24 lg:px-24">
          <div>
            <span class="text-2xl">Azure AD B2C 認証サンプル</span>
          </div>
          <div class="flex space-x-5 sm:space-x-8 lg:space-x-12">
            <span> 現在時刻: {{ getTime() }}</span>
            <br />
            <button v-if="!isAuthenticated()" @click="signIn()">
              ログイン
            </button>
            <span v-if="isAuthenticated()">
              ユーザーID: {{ getUserId() }}
            </span>
          </div>
        </div>
      </nav>
    </header>

    <footer
      class="w-full mx-auto border-t py-4 px-24 text-base bg-black text-gray-500"
    >
      <p>&copy; 2023 - Dressca - Privacy</p>
    </footer>
  </div>
</template>
