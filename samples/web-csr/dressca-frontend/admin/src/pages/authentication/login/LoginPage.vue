<script setup lang="ts">
import { useRoute, useRouter } from 'vue-router'
import { routeNames } from '@/business-common/router/route-names'
import { useLogin } from '@/security/public-api'
import { EnvelopeIcon, KeyIcon } from '@heroicons/vue/24/solid'
import { showToast } from '@/business-common/services/notification-service'
import { redirectHelper } from '@/system-common/helpers/redirect-helper'

const router = useRouter()
const route = useRoute(routeNames.login)
const { toSafeRedirectPath } = redirectHelper()
const { form, login } = useLogin()

/**
 * アプリケーションにログインし、戻り先の画面へ遷移します。
 * 別の画面からログイン画面にリダイレクトしてきたのであれば、その画面に遷移します。
 * 戻り先がない場合や、アプリケーション外を指す場合は、ホーム画面に遷移します。
 * ログインに失敗した場合は、利用者に通知してログイン画面にとどまります。
 */
const loginAsync = async () => {
  const outcome = await login()
  switch (outcome.kind) {
    case 'loggedIn': {
      const redirect = toSafeRedirectPath(route.query.redirect)
      router.push(redirect ?? { name: routeNames.home })
      break
    }
    case 'failed':
      showToast('ログインに失敗しました。')
      break
    case 'canceled':
      break
  }
}
</script>
<template>
  <div class="container mx-auto flex flex-col items-center justify-center gap-6">
    <div class="p-8 text-3xl font-bold">ログイン</div>

    <form class="mt-8 text-xl">
      <div class="form-group">
        <div class="flex justify-between">
          <EnvelopeIcon class="h-8 w-8 text-gray-900/50" />
          <input
            id="userName"
            v-model="form.userName"
            type="text"
            placeholder="ユーザー名"
            autocomplete="username"
            class="border-b px-4 py-2 placeholder-gray-500/50 focus:border-b-2 focus:border-gray-500 focus:outline-hidden"
          />
        </div>
        <p id="username-error" class="px-8 py-2 text-sm text-red-800">{{ form.errors.userName }}</p>
      </div>
      <div class="form-group mt-4">
        <div class="flex justify-between">
          <KeyIcon class="h-8 w-8 text-gray-900/50" />
          <input
            id="password"
            v-model="form.password"
            type="password"
            placeholder="パスワード"
            autocomplete="current-password"
            class="border-b px-4 py-2 placeholder-gray-500/50 focus:border-b-2 focus:border-gray-500 focus:outline-hidden"
          />
        </div>

        <p id="password-error" class="px-8 py-2 text-sm text-red-800">{{ form.errors.password }}</p>
      </div>
      <div class="form-group mt-8">
        <button
          type="button"
          class="rounded-sm bg-blue-800 px-4 py-2 font-bold text-white hover:bg-blue-700 disabled:bg-blue-500/50"
          :disabled="!form.isValid"
          @click="loginAsync"
        >
          ログイン
        </button>
      </div>
    </form>
  </div>
</template>
