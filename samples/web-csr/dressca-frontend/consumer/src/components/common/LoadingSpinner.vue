<script setup lang="ts">
defineProps<{
  show: boolean;
}>();
</script>
<template>
  <div
    v-if="show"
    class="fixed top-0 left-0 right-0 bottom-0 w-full h-screen z-50 overflow-hidden bg-transparent flex items-center justify-center"
  >
    <div class="flex items-center justify-center">
      <div class="flex justify-center">
        <div
          class="animate-spin h-14 w-14 border-4 rounded-full border-gray-400 border-t-gray-200"
        ></div>
      </div>
    </div>
  </div>
</template>
