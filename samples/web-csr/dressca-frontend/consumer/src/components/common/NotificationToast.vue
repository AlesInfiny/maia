<script setup lang="ts">
import { storeToRefs } from 'pinia';
import { useNotificationStore } from '@/stores/notification/notification';
import {
  ClipboardDocumentIcon,
  ExclamationCircleIcon,
  XMarkIcon,
} from '@heroicons/vue/24/outline';
import { ref, watch } from 'vue';
import { useEventBus } from '@vueuse/core';
import { showToast } from '@/services/notification/notificationService';
import { unhandledErrorEventKey } from '@/shared/events';

const show = ref(false);

const notificationStore = useNotificationStore();
const { id, title, message, detail, status, timeout } =
  storeToRefs(notificationStore);
const copy = (text: string) => {
  navigator.clipboard.writeText(text);
};

const close = () => {
  show.value = false;
  notificationStore.clearMessage();
};

const unhandledErrorEventBus = useEventBus(unhandledErrorEventKey);
unhandledErrorEventBus.on((payload) =>
  showToast(
    payload.message,
    payload.id,
    payload.title,
    payload.detail,
    payload.status,
    payload.timeout,
  ),
);

watch(message, (newMessage) => {
  if (newMessage !== '') {
    show.value = true;
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
      v-if="show"
      class="fixed inline-flex items-center w-5/6 inset-x-0 mx-auto mt-2 p-4 text-gray-500 bg-red-500 rounded-lg shadow"
    >
      <div
        class="inline-flex items-center justify-center flex-shrink-0 w-8 h-8 text-red-500 bg-red-100 rounded-lg"
      >
        <ExclamationCircleIcon class="w-5 h-5" />
        <span class="sr-only">Error icon</span>
      </div>
      <div
        class="w-11/12 text-base text-white justify-center flex-shrink-0 mx-auto"
      >
        <div class="ml-2 text-sm font-bold">{{ message }}</div>
        <div class="ml-4 mr-2 flex-shrink-0">
          <div v-if="status !== 0">
            <div class="mt-2 text-sm font-bold text-stone-800 underline">
              ステータスコード
            </div>
            <div class="ml-2">{{ status }}</div>
          </div>
          <div v-if="id">
            <div class="text-sm font-bold text-stone-800 underline">例外ID</div>
            <div class="ml-2">{{ id }}</div>
          </div>
          <div v-if="title">
            <div class="text-sm font-bold text-stone-800 underline">
              エラーメッセージ
            </div>
            <div class="ml-2">{{ title }}</div>
          </div>
          <div v-if="detail" class="">
            <div class="text-sm font-bold text-stone-800 underline">
              エラー詳細
            </div>
            <div
              class="flex justify-between max-h-20 overflow-y-auto items-start"
            >
              <div class="ml-2 text-xs">{{ detail }}</div>
              <button
                type="button"
                class="mr-2 h-8 w-8 bg-red-100 rounded-lg focus:ring-2 focus:ring-gray-300 hover:bg-gray-100 inline-flex items-center justify-center flex-shrink-0"
                @click="copy(detail)"
              >
                <ClipboardDocumentIcon class="w-5 h-5" />
              </button>
            </div>
          </div>
        </div>
      </div>
      <button
        type="button"
        class="h-8 w-8 bg-red-100 rounded-lg focus:ring-2 focus:ring-gray-300 hover:bg-gray-100 inline-flex items-center justify-center flex-shrink-0"
        @click="close"
      >
        <XMarkIcon class="w-5 h-5" />
      </button>
    </div>
  </transition>
</template>
