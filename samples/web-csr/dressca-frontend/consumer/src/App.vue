<script setup lang="ts">
import { ShoppingCartIcon } from '@heroicons/vue/24/solid';
import { storeToRefs } from 'pinia';
import { useAuthenticationStore } from '@/stores/authentication/authentication';
import NotificationToast from './components/common/NotificationToast.vue';

const authenticationStore = useAuthenticationStore();
const { isAuthenticated } = storeToRefs(authenticationStore);
</script>

<template>
  <div class="z-2">
    <NotificationToast />
  </div>
  <div class="flex flex-col h-screen justify-between z-0">
    <header>
      <nav
        aria-label="Jump links"
        class="text-lg font-medium text-gray-900 py-5 ring-1 ring-gray-900 ring-opacity-5 shadow-sm"
      >
        <div class="mx-auto flex justify-between px-4 md:px-24 lg:px-24">
          <div>
            <router-link class="text-2xl" to="/"> Dressca </router-link>
          </div>
          <div class="flex space-x-5 sm:space-x-8 lg:space-x-12">
            <router-link to="/basket">
              <ShoppingCartIcon class="h-8 w-8 text-amber-600" />
            </router-link>
            <router-link v-if="!isAuthenticated" to="/authentication/login">
              ログイン
            </router-link>
          </div>
        </div>
      </nav>
    </header>

    <main class="mb-auto">
      <router-view />
    </main>

    <footer
      class="w-full mx-auto border-t py-4 px-24 text-base bg-black text-gray-500"
    >
      <p>&copy; 2023 - Dressca - Privacy</p>
    </footer>
  </div>
</template>
