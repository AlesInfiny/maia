<script setup lang="ts">
import { ShoppingCartIcon } from '@heroicons/vue/24/solid'
import { routeNames } from '@/business-common/router/route-names'
import { router } from '@/business-common/router'
import { useEventBus } from '@vueuse/core'
import NotificationToast from '@/business-common/components/NotificationToast.vue'
import { unauthorizedErrorEventKey } from '@/system-common/events'
import { AuthenticationMenu } from '@/security/public-api'

/**
 * ログアウトしたとき、ログイン画面へ遷移します。
 */
const onLoggedOut = () => {
  router.push({ name: routeNames.login })
}

const unauthorizedErrorEventBus = useEventBus(unauthorizedErrorEventKey)

unauthorizedErrorEventBus.on(() => {
  // 現在の画面情報をクエリパラメーターに保持してログイン画面にリダイレクトします。
  // コンポーネント外に引き渡すので、 直接 import した router を使用します。
  router.push({
    name: routeNames.login,
    query: { redirect: router.currentRoute.value.fullPath },
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
            <router-link class="text-2xl" :to="{ name: routeNames.displayItem }">
              Dressca
            </router-link>
          </div>
          <div class="flex gap-5 sm:gap-5 lg:gap-12">
            <router-link :to="{ name: routeNames.basket }">
              <ShoppingCartIcon class="h-8 w-8 text-amber-600" />
            </router-link>
            <AuthenticationMenu
              :login-location="{ name: routeNames.login }"
              @logged-out="onLoggedOut"
            />
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
