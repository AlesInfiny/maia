<template>
  <v-row justify="center" align="center">
    <v-col cols="12" sm="8" md="6"> </v-col>
  </v-row>
</template>

<script lang="ts">
import { defineComponent, useRouter, ref } from '@nuxtjs/composition-api'

export default defineComponent({
  name: 'TopPage',
  components: {},
  setup() {
    const accessPath = '/sampledemo/market'
    const router = useRouter()
    const sampledemoFlag = ref<boolean>(false)

    if(accessPath == '/sampledemo/market') {
      sampledemoFlag.value = !sampledemoFlag.value
    }
    router.push(accessPath)
  },
})
</script>