<script setup lang="ts">
import { ShoppingCartIcon } from '@heroicons/vue/24/solid';
import { useAuthenticationStore } from '@/stores/authentication/authentication';
import { useUserStore } from './stores/user/user';

const authenticationStore = useAuthenticationStore();
const userStore = useUserStore();
const isAuthenticated = () => {
  return authenticationStore.isAuthenticated;
};
const signIn = async () => {
  await authenticationStore.signIn();
  if (authenticationStore.isAuthenticated) {
    await userStore.fetchUserResponse();
  }
};

const getUserId = () => {
  return userStore.getUserId;
};
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
            <span class="text-2xl">Dressca（Azure AD B2C 認証サンプル）</span>
          </div>
          <div class="flex space-x-5 sm:space-x-8 lg:space-x-12">
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
