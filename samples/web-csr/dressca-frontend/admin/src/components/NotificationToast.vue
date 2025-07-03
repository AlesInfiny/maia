<script setup lang="ts">
import { ExclamationCircleIcon, XMarkIcon } from '@heroicons/vue/24/outline'
import { watch } from 'vue'
import { useEventBus } from '@vueuse/core'
import { showToast } from '@/services/notification/notificationService'
import { unhandledErrorEventKey } from '@/shared/events'

/**
 * ユーザーにメッセージを通知するトーストです。
 */
const show = defineModel('show', {
  type: Boolean,
  required: true,
  default: false,
})
const message = defineModel('message', {
  type: String,
  required: true,
  default: '',
})
const timeout = defineModel('timeout', {
  type: Number,
  required: true,
  default: 5000,
})

const unhandledErrorEventBus = useEventBus(unhandledErrorEventKey)
unhandledErrorEventBus.on((payload) => showToast(payload.message))

const close = () => {
  show.value = false
  message.value = ''
}

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
      class="max-w-m fixed inset-x-0 mx-auto mt-2 inline-flex w-5/6 items-center rounded-lg bg-red-500 p-4 text-gray-500 shadow"
    >
      <div
        class="inline-flex h-8 w-8 flex-shrink-0 items-center justify-center rounded-lg bg-red-100 text-red-500"
      >
        <ExclamationCircleIcon class="h-5 w-5" />
        <span class="sr-only">Error icon</span>
      </div>
      <div class="ms-3 text-sm font-normal text-white">{{ message }}</div>
      <button
        type="button"
        class="-mx-1.5 -my-1.5 ms-auto inline-flex h-8 w-8 items-center justify-center rounded-lg bg-red-100 hover:bg-gray-100 focus:ring-2 focus:ring-gray-300"
        @click="close"
      >
        <XMarkIcon class="h-5 w-5" />
      </button>
    </div>
  </transition>
</template>
