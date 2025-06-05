<script setup lang="ts">
import NotificationToast from '@/components/NotificationToast.vue';
import { storeToRefs } from 'pinia';
import { useAuthenticationStore } from '@/stores/authentication/authentication';
import { Bars3Icon } from '@heroicons/vue/24/solid';
import { logoutAsync } from '@/services/authentication/authentication-service';
import { useRouter } from 'vue-router';
import { router as importedRouter } from '@/router';
import { ref } from 'vue';
import { useNotificationStore } from '@/stores/notification/notification';
import { useEventBus } from '@vueuse/core';
import { showToast as showToastByService } from '@/services/notification/notificationService';
import { unauthorizedErrorEventKey } from './shared/events';

const authenticationStore = useAuthenticationStore();
const { authenticationState, userName, userRoles } =
  storeToRefs(authenticationStore);

const notificationStore = useNotificationStore();
const { message, timeout } = storeToRefs(notificationStore);

const router = useRouter();

/**
 * トーストの開閉状態です。
 */
const showToast = ref(false);

/**
 * ログインメニューの開閉状態です。
 */
const showLoginMenu = ref(false);

/**
 * アプリケーションからログアウトします。
 */
const logout = async () => {
  await logoutAsync();
  showLoginMenu.value = !showLoginMenu.value;
  router.push({ name: 'authentication/login' });
};

const unauthorizedErrorEventBus = useEventBus(unauthorizedErrorEventKey);

unauthorizedErrorEventBus.on((payload) => {
  // 現在の画面情報をクエリパラメーターに保持してログイン画面にリダイレクトします。
  // コンポーネント外に引き渡すので、 直接 import した router を使用します。
  importedRouter.push({
    name: 'authentication/login',
    query: {
      redirectName: importedRouter.currentRoute.value.name?.toString(),
      redirectParams: JSON.stringify(importedRouter.currentRoute.value.params),
      redirectQuery: JSON.stringify(importedRouter.currentRoute.value.query),
    },
  });
  showToastByService(payload.details);
});
</script>
<template>
  <div class="z-20 fixed">
    <NotificationToast
      v-model:show="showToast"
      v-model:message="message"
      v-model:timeout="timeout"
    />
  </div>
  <nav class="bg-gray-800">
    <div class="mx-auto max-w-7xl px-2 sm:px-6 lg:px-8">
      <div class="relative flex h-16 items-center justify-between">
        <div
          class="flex flex-1 items-center justify-center sm:items-stretch sm:justify-start"
        >
          <router-link
            to="/"
            class="flex flex-shrink-0 items-center text-xl font-medium rounded-md px-3 text-white hover:bg-blue-800"
            >Dressca 管理</router-link
          >
          <div class="hidden sm:ml-6 sm:block">
            <div class="flex space-x-4">
              <router-link
                to="/catalog/items"
                class="rounded-md px-3 py-2 text-base font-medium text-white hover:bg-blue-800"
                >カタログアイテム管理</router-link
              >
            </div>
          </div>
        </div>
        <div
          class="absolute inset-y-0 right-0 flex items-center pr-2 sm:static sm:inset-auto sm:ml-6 sm:pr-0"
        >
          <div class="relative rounded-full text-white">
            <span class="absolute -inset-1.5"></span>
            {{ userRoles }}
          </div>
          <div
            class="absolute inset-y-0 right-0 flex items-center pr-2 sm:static sm:inset-auto sm:ml-6 sm:pr-0"
          >
            <div class="relative rounded-full text-white">
              <span class="absolute -inset-1.5"></span>
              {{ userName }}
            </div>
            <div class="relative ml-3">
              <Bars3Icon
                class="h-14 w-14 px-2 py-2 rounded text-white hover:bg-blue-800"
                @click="showLoginMenu = !showLoginMenu"
              ></Bars3Icon>
              <div
                v-if="showLoginMenu"
                class="absolute right-0 z-10 mt-2 w-48 origin-top-right rounded-md bg-white py-1 shadow-lg ring-1 ring-black ring-opacity-5 focus:outline-none"
                role="menu"
                aria-orientation="vertical"
                aria-labelledby="menu-button"
                tabindex="-1"
              >
                <div v-if="!authenticationState">
                  <router-link
                    id="login"
                    to="/authentication/login"
                    class="block px-4 py-2 text-sm text-gray-700"
                    role="menuitem"
                    tabindex="-1"
                    @click="showLoginMenu = !showLoginMenu"
                    >ログイン</router-link
                  >
                </div>
                <div v-if="authenticationState">
                  <button
                    id="logout"
                    type="button"
                    class="block px-4 py-2 text-sm text-gray-700"
                    role="menuitem"
                    tabindex="-1"
                    @click="logout"
                  >
                    ログアウト
                  </button>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </nav>
  <main class="mb-16 min-h-screen">
    <router-view />
  </main>

  <footer class="flex flex-col bg-gray-900 px-24 py-4 text-base text-white">
    <p>&copy; 2024 - Dressca - Privacy</p>
  </footer>
</template>
