<template>
  <v-app>
    <h1 v-if="error.statusCode === 404">
      {{ pageNotFound }}
    </h1>
    <h1 v-else>
      {{ otherError }}
    </h1>
    <NuxtLink to="/"> Home page </NuxtLink>
  </v-app>
</template>

<script lang="ts">
import { defineComponent, useMeta } from '@nuxtjs/composition-api'

export default defineComponent({
  name: 'ErrorLayout',
  layout: 'empty',
  props: {
    error: {
      type: Object,
      default: null,
    },
  },
  setup(prop) {
    const pageNotFound = '404 Not Found'
    const otherError = 'An error occurred'

    // ページタイトルを変更
    const { title } = useMeta()
    title.value = prop.error.statusCode === 404 ? pageNotFound : otherError

    return { pageNotFound, otherError }
  },
  head: {},
})
</script>

<style scoped>
h1 {
  font-size: 20px;
}
</style>
