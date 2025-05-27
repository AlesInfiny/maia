<script setup lang="ts">
import { useRoute, useRouter } from 'vue-router';
import { useField, useForm } from 'vee-validate';
import * as yup from 'yup';
import { validationItems } from '@/validation/validation-items';
import { loginAsync } from '@/services/authentication/authentication-service';
import { EnvelopeIcon, KeyIcon } from '@heroicons/vue/24/solid';
import { showToast } from '@/services/notification/notificationService';
import { useCustomErrorHandler } from '@/shared/error-handler/custom-error-handler';

// フォーム固有のバリデーション定義
const formSchema = yup.object({
  userName: validationItems.email.required('ユーザー名は必須です。'),
  password: yup.string().required('パスワードは必須です。'),
});

const router = useRouter();
const route = useRoute();
const customErrorHandler = useCustomErrorHandler();

const { meta } = useForm({ validationSchema: formSchema });
const { value: userName, errorMessage: userNameError } =
  useField<string>('userName');
const { value: password, errorMessage: passwordError } =
  useField<string>('password');

const isInvalid = () => {
  return !meta.value.valid;
};

/**
 * アプリケーションにログインします。
 */
const login = async () => {
  try {
    await loginAsync();
  } catch (error) {
    customErrorHandler.handle(error, () => {
      showToast('ログインに失敗しました。');
    });
  }
  // 別の画面からリダイレクトしていない場合は、ホーム画面に遷移します。
  if (!route.query.redirectName) {
    router.push({ name: 'home' });
  } else {
    // 別の画面からログイン画面にリダイレクトしてきたのであれば、その画面に遷移します。
    router.push({
      name: route.query.redirectName as string,
      params: JSON.parse(route.query.redirectParams as string),
      query: JSON.parse(route.query.redirectQuery as string),
    });
  }
};
</script>

<template>
  <div
    class="container mx-auto flex flex-col items-center justify-center gap-6"
  >
    <div class="p-8 text-3xl font-bold">ログイン</div>

    <form class="mt-8 text-xl">
      <div class="form-group">
        <div class="flex justify-between">
          <EnvelopeIcon class="h-8 w-8 text-gray-900 opacity-50" />
          <input
            id="userName"
            v-model="userName"
            type="text"
            placeholder="ユーザー名"
            autocomplete="username"
            class="border-b px-4 py-2 placeholder-gray-500 placeholder-opacity-50 focus:border-b-2 focus:border-gray-500 focus:outline-none"
          />
        </div>
        <p class="px-8 py-2 text-sm text-red-800">{{ userNameError }}</p>
      </div>
      <div class="form-group mt-4">
        <div class="flex justify-between">
          <KeyIcon class="h-8 w-8 text-gray-900 opacity-50" />
          <input
            id="password"
            v-model="password"
            type="password"
            placeholder="パスワード"
            autocomplete="current-password"
            class="border-b px-4 py-2 placeholder-gray-500 placeholder-opacity-50 focus:border-b-2 focus:border-gray-500 focus:outline-none"
          />
        </div>

        <p class="px-8 py-2 text-sm text-red-800">{{ passwordError }}</p>
      </div>
      <div class="form-group mt-8">
        <button
          type="button"
          class="rounded bg-blue-800 px-4 py-2 font-bold text-white hover:bg-blue-700 disabled:bg-blue-500 disabled:opacity-50"
          :disabled="isInvalid()"
          @click="login"
        >
          ログイン
        </button>
      </div>
    </form>
  </div>
</template>
