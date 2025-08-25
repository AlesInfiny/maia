<script setup lang="ts">
import { storeToRefs } from 'pinia'
import { useNotificationStore } from '@/stores/notification/notification'
import { ClipboardDocumentIcon, ExclamationCircleIcon, XMarkIcon } from '@heroicons/vue/24/outline'
import { ref, watch } from 'vue'
import { useEventBus } from '@vueuse/core'
import { showToast } from '@/services/notification/notificationService'
import { unhandledErrorEventKey } from '@/shared/events'

const show = ref(false)

const notificationStore = useNotificationStore()
const { id, title, message, detail, status, timeout } = storeToRefs(notificationStore)
const copy = async (text: string) => {
  await navigator.clipboard.writeText(text)
}

const close = () => {
  show.value = false
  notificationStore.clearMessage()
}

const unhandledErrorEventBus = useEventBus(unhandledErrorEventKey)
unhandledErrorEventBus.on((payload) =>
  showToast(
    payload.message,
    payload.id,
    payload.title,
    payload.detail,
    payload.status,
    payload.timeout,
  ),
)

watch(message, (newMessage) => {
  if (newMessage !== '') {
    show.value = true
    setTimeout(() => {
      close()
    }, timeout.value)
  }
})
</script>

<template>
  <transition
    enter-from-class="translate-y-[-150%] opacity-0"
    leave-to-class="translate-y-[-150%] opacity-0"
    enter-active-class="transition duration-300"
    leave-active-class="transition duration-300"
  >
    <div
      v-if="show"
      class="fixed inset-x-0 mx-auto mt-2 inline-flex w-5/6 items-center rounded-lg bg-red-500 p-4 text-gray-500 shadow-sm"
    >
      <div
        class="inline-flex h-8 w-8 shrink-0 items-center justify-center rounded-lg bg-red-100 text-red-500"
      >
        <ExclamationCircleIcon class="h-5 w-5" />
        <span class="sr-only">Error icon</span>
      </div>
      <div class="mx-auto w-11/12 shrink-0 justify-center text-base text-white">
        <div class="ml-2 text-sm font-bold">{{ message }}</div>
        <div class="mr-2 ml-4 shrink-0">
          <div v-if="status !== 0">
            <div class="mt-2 text-sm font-bold text-stone-800 underline">ステータスコード</div>
            <div class="ml-2">{{ status }}</div>
          </div>
          <div v-if="id">
            <div class="text-sm font-bold text-stone-800 underline">例外ID</div>
            <div class="ml-2">{{ id }}</div>
          </div>
          <div v-if="title">
            <div class="text-sm font-bold text-stone-800 underline">エラーメッセージ</div>
            <div class="ml-2">{{ title }}</div>
          </div>
          <div v-if="detail" class="">
            <div class="text-sm font-bold text-stone-800 underline">エラー詳細</div>
            <div class="flex max-h-20 items-start justify-between overflow-y-auto">
              <div class="ml-2 text-xs">{{ detail }}</div>
              <button
                type="button"
                class="mr-2 inline-flex h-8 w-8 shrink-0 items-center justify-center rounded-lg bg-red-100 hover:bg-gray-100 focus:ring-2 focus:ring-gray-300"
                @click="copy(detail)"
              >
                <ClipboardDocumentIcon class="h-5 w-5" />
              </button>
            </div>
          </div>
        </div>
      </div>
      <button
        type="button"
        class="inline-flex h-8 w-8 shrink-0 items-center justify-center rounded-lg bg-red-100 hover:bg-gray-100 focus:ring-2 focus:ring-gray-300"
        @click="close"
      >
        <XMarkIcon class="h-5 w-5" />
      </button>
    </div>
  </transition>
</template>
