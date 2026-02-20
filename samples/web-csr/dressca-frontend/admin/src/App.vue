<script setup lang="ts">
import NotificationToast from '@/components/NotificationToast.vue'
import LoginMenu from '@/components/LoginMenu.vue'
import { storeToRefs } from 'pinia'
import { router as importedRouter } from '@/router'
import { ref } from 'vue'
import { useNotificationStore } from '@/stores/notification/notification'
import { useEventBus } from '@vueuse/core'
import { showToast as showToastByService } from '@/services/notification/notificationService'
import { unauthorizedErrorEventKey } from './shared/events'

const notificationStore = useNotificationStore()
const { message, timeout } = storeToRefs(notificationStore)

/**
 * トーストの開閉状態です。
 */
const showToast = ref(false)

const unauthorizedErrorEventBus = useEventBus(unauthorizedErrorEventKey)

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
  })
  showToastByService(payload.details)
})
</script>
<template>
  <div class="fixed z-20">
    <NotificationToast
      v-model:show="showToast"
      v-model:message="message"
      v-model:timeout="timeout"
    />
  </div>
  <nav class="bg-gray-800">
    <div class="mx-auto max-w-7xl px-2 sm:px-6 lg:px-8">
      <div class="relative flex h-16 items-center justify-between">
        <div class="flex flex-1 items-center justify-center sm:items-stretch sm:justify-start">
          <router-link
            to="/"
            class="flex shrink-0 items-center rounded-md px-3 text-xl font-medium text-white hover:bg-blue-800"
            >Dressca 管理</router-link
          >
          <div class="hidden sm:ml-6 sm:block">
            <div class="flex gap-4">
              <router-link
                to="/catalog/items"
                class="rounded-md px-3 py-2 text-base font-medium text-white hover:bg-blue-800"
                >カタログアイテム管理</router-link
              >
            </div>
          </div>
        </div>
        <LoginMenu />
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
