<script setup lang="ts">
/**
 * ログイン／ログアウトを切り替えるハンバーガーメニューです。
 */
import { storeToRefs } from 'pinia'
import { useAuthenticationStore } from '@/stores/authentication/authentication'
import { Bars3Icon } from '@heroicons/vue/24/solid'
import { logout as logoutByService } from '@/services/authentication/authentication-service'
import { useRouter } from 'vue-router'
import { ref, watch } from 'vue'
import { onClickOutside } from '@vueuse/core'

const authenticationStore = useAuthenticationStore()
const { authenticationState, userName, userRoles } = storeToRefs(authenticationStore)

const router = useRouter()

const menuRef = ref<HTMLElement | null>(null)

/**
 * ログインメニューの開閉状態です。
 */
const showLoginMenu = ref(false)

/**
 * アプリケーションからログアウトします。
 */
const logout = () => {
  logoutByService()
  showLoginMenu.value = false
  router.push({ name: 'authentication/login' })
}

onClickOutside(menuRef, () => {
  showLoginMenu.value = false
})

watch(
  () => router.currentRoute.value.fullPath,
  () => {
    showLoginMenu.value = false
  },
)
</script>
<template>
  <div
    ref="menuRef"
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
          class="h-14 w-14 cursor-pointer rounded-sm px-2 py-2 text-white hover:bg-blue-800"
          @click="showLoginMenu = !showLoginMenu"
        ></Bars3Icon>
        <div
          v-if="showLoginMenu"
          class="absolute right-0 z-10 mt-2 w-48 origin-top-right rounded-md bg-white py-1 shadow-lg ring-1 ring-black/5 focus:outline-hidden"
          role="menu"
          aria-orientation="vertical"
          aria-labelledby="menu-button"
          tabindex="-1"
        >
          <div v-if="!authenticationState">
            <router-link
              id="login"
              to="/authentication/login"
              class="block cursor-pointer px-4 py-2 text-sm text-gray-700"
              role="menuitem"
              tabindex="-1"
              @click="showLoginMenu = false"
              >ログイン</router-link
            >
          </div>
          <div v-if="authenticationState">
            <button
              id="logout"
              type="button"
              class="block cursor-pointer px-4 py-2 text-sm text-gray-700"
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
</template>
