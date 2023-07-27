<template>
  <v-container fill-height>
    <v-layout pa-0 ma-0 row wrap>
    <div style="margin-bottom:20px">
        <span id="login">{{message}}</span>
    </div>
    </v-layout>
  </v-container>
</template>

<script lang="ts">
import { mdiChevronRight } from '@mdi/js'
import { defineComponent, ref, useRouter } from '@nuxtjs/composition-api'
import { data } from 'browserslist'
import authPopup from '~/auth/authADB2C'

export default defineComponent({
  name: 'Communities',
  setup() {
    const title = ''
    const copyright = 'Copyright © 2022 BIPROGY Inc. All rights reserved.'
    const message = 'API呼び出し中 . . . .'

    const hamburgerActive = ref<boolean>(false)
    const hamburgerClick = () => {
      hamburgerActive.value = !hamburgerActive.value
    }

    return {
      title,
      copyright,
      hamburgerClick,
      hamburgerActive,
      message
    }
  },
  mounted() {
    this.callGetLoginMessage()
    },
  methods: {
    refs(): any {
      return this.$refs
    },
    async callGetLoginMessage() {
      let errorMessage;
      try {
        await new Promise(resolve => setTimeout(resolve, 3000))
        await authPopup.getLoginMessage()
      } catch(err) {
        errorMessage = 'API呼び出しに失敗しました';
        console.log('callAPIError:' + err)
        alert(errorMessage)
      }
    }
  }
  })
</script>
<style lang="scss" scoped>
.form-community-name {
  padding-top: 8px !important;
  padding-bottom: 8px !important;
  font-size: 20px !important;
  font-weight: 500 !important;
}
.form-nft-group-name {
  padding-top: 8px !important;
  padding-bottom: 36px !important;

  font-size: 24px !important;
  font-weight: 700 !important;
}
.form-dt {
  padding: 0px !important;
  font-size: 18px !important;
}

</style>