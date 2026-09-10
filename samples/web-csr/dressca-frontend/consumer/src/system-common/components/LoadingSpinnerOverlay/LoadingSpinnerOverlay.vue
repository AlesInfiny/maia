<script setup lang="ts">
/**
 * 画面全体を覆うオーバーレイを含むローディングスピナーコンポーネントです。
 * スピナーを表示する親コンポーネントで任意のスピナーアニメーションや画像を指定することで、スピナーをカスタマイズできます。
 * デフォルトでは灰色のローディングスピナーが表示されます。
 */
defineProps<{
  show: boolean
}>()

defineSlots<{
  /**
   * 任意のスピナーアニメーションや画像を指定してください。
   */
  spinner?: HTMLElement
}>()
</script>

<template>
  <div v-if="show" class="overlay">
    <div class="spinner-container">
      <slot name="spinner">
        <div class="default-spinner"></div>
      </slot>
    </div>
  </div>
</template>

<style scoped>
.overlay {
  position: fixed;
  inset: 0;
  width: 100%;
  height: 100vh;
  z-index: 50;
  overflow: hidden;
  background-color: transparent;
  display: flex;
  align-items: center;
  justify-content: center;
}

.spinner-container {
  display: flex;
  align-items: center;
  justify-content: center;
}

.default-spinner {
  height: 56px;
  width: 56px;
  border: 4px solid #b0b0b0;
  border-top-color: #e0e0e0;
  border-radius: 50%;
  animation: spin 1s linear infinite;
}

@keyframes spin {
  0% {
    transform: rotate(0deg);
  }

  100% {
    transform: rotate(360deg);
  }
}
</style>
