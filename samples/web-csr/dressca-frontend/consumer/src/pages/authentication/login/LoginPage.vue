<script setup lang="ts">
import { useRoute, useRouter } from 'vue-router'
import { routeNames } from '@/business-common/router/route-names'
import { useSignIn } from '@/security/public-api'
import { EnvelopeIcon, KeyIcon } from '@heroicons/vue/24/solid'
import { redirectHelper } from '@/system-common/helpers/redirect-helper'

const router = useRouter()
const route = useRoute(routeNames.login)
const { toSafeRedirectPath } = redirectHelper()
const { form, signIn } = useSignIn()

/**
 * ログインし、戻り先の画面へ遷移します。
 * 別の画面からログイン画面にリダイレクトしてきたのであれば、その画面に遷移します。
 * 戻り先がない場合や、アプリケーション外を指す場合は、トップページに遷移します。
 */
const signInOnClick = async () => {
  await signIn()
  const redirect = toSafeRedirectPath(route.query.redirect)
  router.push(redirect ?? { name: routeNames.displayItem })
}
</script>
<template>
  <div class="container mx-auto max-w-sm">
    <form class="mt-8">
      <div class="form-group">
        <div class="flex justify-between">
          <EnvelopeIcon class="h-8 w-8 text-blue-500/50" />
          <input
            id="email"
            v-model="form.email"
            type="text"
            placeholder="email"
            autocomplete="username"
            class="w-full border-b px-4 py-2 placeholder-gray-500/50 focus:border-b-2 focus:border-indigo-500 focus:outline-hidden"
          />
        </div>
        <p id="email-error" class="px-8 py-2 text-sm text-red-500">{{ form.errors.email }}</p>
      </div>
      <div class="form-group mt-4">
        <div class="flex justify-between">
          <KeyIcon class="h-8 w-8 text-blue-500/50" />
          <input
            id="password"
            v-model="form.password"
            type="password"
            placeholder="password"
            autocomplete="current-password"
            class="w-full border-b px-4 py-2 placeholder-gray-500/50 focus:border-b-2 focus:border-indigo-500 focus:outline-hidden"
          />
        </div>

        <p id="password-error" class="px-8 py-2 text-sm text-red-500">{{ form.errors.password }}</p>
      </div>
      <div class="form-group mt-8">
        <button
          type="button"
          class="w-full rounded-sm bg-blue-500 px-4 py-2 font-bold text-white hover:bg-blue-700 disabled:bg-blue-500/50"
          :disabled="!form.isValid"
          @click="signInOnClick"
        >
          ログイン
        </button>
      </div>
    </form>
  </div>
</template>
