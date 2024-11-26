<script setup lang="ts">
import { storeToRefs } from 'pinia';
import { useNotificationStore } from '@/stores/notification/notification';
import { ExclamationCircleIcon, XMarkIcon } from '@heroicons/vue/24/outline';
import { ref, watch } from 'vue';

/**
 * ユーザーにメッセージを通知するトーストです。
 */

const state = ref({
  show: false,
});

const notificationStore = useNotificationStore();
const { message, timeout } = storeToRefs(notificationStore);

const close = () => {
  state.value.show = false;
  notificationStore.clearMessage();
};

watch(message, (newMessage) => {
  if (newMessage !== '') {
    state.value.show = true;
    setTimeout(() => {
      close();
    }, timeout.value);
  }
});
</script>

<template>
  <transition
    enter-from-class="translate-y-[-150%] opacity-0"
    leave-to-class="translate-y-[-150%] opacity-0"
    enter-active-class="transition duration-300"
    leave-active-class="transition duration-300"
  >
    <div
      v-if="state.show"
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
