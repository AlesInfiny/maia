<script setup lang="ts">
import { ShoppingCartIcon } from '@heroicons/vue/24/solid'
import { router } from '@/router'
import { useEventBus } from '@vueuse/core'
import NotificationToast from './components/common/NotificationToast.vue'
import { unauthorizedErrorEventKey } from './shared/events'
import { authenticationService } from './services/authentication/authentication-service'

const { isAuthenticated } = authenticationService()

const unauthorizedErrorEventBus = useEventBus(unauthorizedErrorEventKey)

unauthorizedErrorEventBus.on(() => {
  // 現在の画面情報をクエリパラメーターに保持してログイン画面にリダイレクトします。
  // コンポーネント外に引き渡すので、 直接 import した router を使用します。
  router.push({
    name: 'authentication/login',
    query: {
      redirectName: router.currentRoute.value.name?.toString(),
      redirectParams: JSON.stringify(router.currentRoute.value.params),
      redirectQuery: JSON.stringify(router.currentRoute.value.query),
    },
  })
})
</script>

<template>
  <div class="z-2">
    <NotificationToast />
  </div>
  <div class="z-0 flex h-screen flex-col justify-between">
    <header>
      <nav
        aria-label="Jump links"
        class="py-5 text-lg font-medium text-gray-900 shadow-xs ring-1 ring-gray-900/5"
      >
        <div class="mx-auto flex justify-between px-4 md:px-24 lg:px-24">
          <div>
            <router-link class="text-2xl" to="/"> Dressca </router-link>
          </div>
          <div class="flex gap-5 sm:gap-5 lg:gap-12">
            <router-link to="/basket">
              <ShoppingCartIcon class="h-8 w-8 text-amber-600" />
            </router-link>
            <router-link v-if="!isAuthenticated()" to="/authentication/login">
              ログイン
            </router-link>
          </div>
        </div>
      </nav>
    </header>

    <main class="mb-auto">
      <router-view />
    </main>

    <footer class="mx-auto w-full border-t bg-black px-24 py-4 text-base text-gray-500">
      <p>&copy; 2023 - Dressca - Privacy</p>
    </footer>
  </div>
</template>
