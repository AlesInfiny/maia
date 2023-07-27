<template>
  <v-app>
    <div v-if="loading" id="loading">
      <v-progress-circular indeterminate color="white" :size="70" :width="7" />
    </div>
    <v-app-bar elevation="0" app color="primary">
      <button :class="{hamburgerActive}" class="hamburger" @click="hamburgerClick">
        <span></span>
        <span></span>
        <span></span>
      </button>
      <div class="hamburgerMenu" :class="{hamburgerActive}">
      </div>
      <v-toolbar-title
        class="white--text ml-16"
        style="font-size: 24px; cursor: pointer"
      >
        <strong>{{ title }}</strong>
      </v-toolbar-title>
      <v-spacer />
      <div v-if="isLogin()">
        <v-select
          v-model="selectSettings"
          :items="settings"
          style="max-width: 80px"
          label="設定"
          @change="changeSettings"
        ></v-select>
        </div>
    </v-app-bar>
    <v-main>
      <v-container class="mt-8">
        <Nuxt />
      </v-container>
    </v-main>
    <v-footer absolute padless app>
      <v-card
        width="100%"
        class="lighten-1 text-center rounded-0"
        color="primary"
      >
        <v-card-text class="white--text">
          <strong>{{ copyright }}</strong>
        </v-card-text>
      </v-card>
    </v-footer>
  </v-app>
</template>

<script lang="ts">
import { mdiChevronRight } from '@mdi/js'
import { defineComponent, ref, useRouter } from '@nuxtjs/composition-api'
import { useUserStore } from '~/store/user'
import { emitter } from '~/plugins/emitter'
import AuthPopup  from '~/auth/authADB2C'

export default defineComponent({
  name: 'DefaultLayout',
  setup() {
    const store = useUserStore()
    const title = 'SampleApplication'
    const copyright = 'Copyright © 2022 BIPROGY Inc. All rights reserved.'
    const router = useRouter()

    // SampleApplicationの場合はTrue
    const sampledemoFlag = ref<boolean>(true)

    const hamburgerActive = ref<boolean>(false)
    const hamburgerClick = () => {
      hamburgerActive.value = !hamburgerActive.value
    }

    const isCommunities = () => {
      return router.currentRoute.path == '/sampledemo/communities'
    }
    
    const pushCommunities = () => {

      if (router.currentRoute.path == '/sampledemo/communities') {
        router.go(0)
      } else {
        router.push('/sampledemo/communities')
      }
      hamburgerActive.value = false
    }

    const settings = ["編集","退会","ログアウト"]
    const selectSettings = ref<string>("")
    const changeSettings = async (value: string) => {
      // ログイン中のユーザ情報を取得
      selectSettings.value = value
      switch(value) {
        case "編集": {
          try{
            await AuthPopup.editProfile()
          }catch(err) {
            console.log(err)
            alert('ユーザ更新処理でエラーが発生しました')
          }
          window.location.reload();
          break
          }
        case "退会": {
          try {
            const result = confirm('ユーザ情報を削除します。');
            if (result) {
              await AuthPopup.deleteUser()
            } else {
              window.location.reload();
            }
          }catch(err) {
            console.log(err)
            alert('退会処理でエラーが発生しました')
          }
          break
        }
        case "ログアウト": {
          store.setAccountHomeId('')
          AuthPopup.signOut()
        }
      }
    }

    const isLogin = () => {
      return store.getAccountHomeId()
    }

    const loading = ref<boolean>(false)
    emitter.on('loading', (state) => {
      if (state == 'show') {
        console.log(state)
        console.log('loading: true')
        loading.value = true
      } else {
        console.log('loading: false')
        loading.value = false
      }
    })
    return {
      title,
      copyright,
      hamburgerClick,
      hamburgerActive,
      isCommunities,
      settings,
      changeSettings,
      selectSettings,
      isLogin,
      loading,
      pushCommunities,
      sampledemoFlag,
    }
  }
})
</script>
<style lang="scss">
  #loading {
    display: flex;
    justify-content: center;
    align-items: center;
    width: 100%;
    height: 100vh;
    z-index: 9999;
    position: fixed;
    background-color: rgba(#000, 0.5);
  }

  // ハンバーガーメニューアイコン
  .hamburger,
  .hamburger span {
    display: inline-block;
    transition: all 0.4s;
    box-sizing: border-box;
    position: relative;
    z-index: 100;
  }
  .hamburger {
    position: relative;
    width: 32px;
    height: 26px;
    background: none;
    border: none;
    appearance: none;
    cursor: pointer;
  }
  .hamburger span {
    position: absolute;
    left: 0;
    width: 100%;
    height: 4px;
    background-color: #FFF;
    border-radius: 4px;
  }
  .hamburger span:nth-of-type(1) {
    top: 0;
  }
  .hamburger span:nth-of-type(2) {
    top: 11px;
  }
  .hamburger span:nth-of-type(3) {
    bottom: 0;
  }
  // ハンバーガーメニューアイコンのアニメーション
  .hamburger.hamburgerActive span:nth-of-type(1) {
    transform: translateY(11px) rotate(-45deg);
  }
  .hamburger.hamburgerActive span:nth-of-type(2) {
    opacity: 0;
  }
  .hamburger.hamburgerActive span:nth-of-type(3) {
    transform: translateY(-11px) rotate(45deg);
  }

  // ハンバーガーメニュー本体
  .hamburgerMenu{
    position: fixed;
    top: 64px;
    left: 0;
    z-index: 1;
    width: 17vw;
    //height: 55vh;
    height: calc(100vh - 64px);
    display: flex;
    flex-direction: column;
    align-items: center;
    background: #555;
    //justify-content: center;
    padding-top: 20px
  }
  .hamburgerMenu-item{
    width: 100%;
    height: auto;
    margin-bottom: 15px;
    padding: .5em 1em;
    text-align: left;
    font-size:16px;
    font-weight: bold;
    color: #fff;
    box-sizing: border-box;
    cursor: pointer;
  }

  // ハンバーガーメニューのアニメーション
  .hamburgerMenu{
    transform: translateX(-100vw);
    transition: all .3s linear;
  }
  .hamburgerMenu.hamburgerActive{
    transform: translateX(0);
  }
</style>
