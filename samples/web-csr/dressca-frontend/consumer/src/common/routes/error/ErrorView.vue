<script setup lang="ts">
import { i18n } from '@/locales/i18n'

const { t } = i18n.global
</script>

<template>
  <div class="m-10 rounded-sm border border-red-400 bg-red-100 px-4 py-5 font-bold text-red-700">
    {{ t('errorOccurred') }}
  </div>
</template>
