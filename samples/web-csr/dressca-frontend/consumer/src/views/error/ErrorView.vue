<script setup lang="ts">
import { i18n } from '@/locales/i18n'

const { t } = i18n.global
</script>

<template>
  <div class="m-10 bg-red-100 border border-red-400 text-red-700 font-bold px-4 py-5 rounded">
    {{ t('errorOccurred') }}
  </div>
</template>
