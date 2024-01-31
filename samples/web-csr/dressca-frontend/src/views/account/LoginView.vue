<script setup lang="ts">
import { useField, useForm } from 'vee-validate';
import * as yup from 'yup';
import { validationItems } from '@/validation/validation-items';
import { useAuthenticationStore } from '@/stores/authentication/authentication';
import { EnvelopeIcon, KeyIcon } from '@heroicons/vue/24/solid';
import { useRoutingStore } from '@/stores/routing/routing';

// フォーム固有のバリデーション定義
const formSchema = yup.object({
  email: validationItems.email.required(),
  password: yup.string().required(),
});

const { meta } = useForm({ validationSchema: formSchema });
const { value: email, errorMessage: emailError } = useField<string>('email');
const { value: password, errorMessage: passwordError } = useField('password');

const isInvalid = () => {
  return !meta.value.valid;
};

const authenticationStore = useAuthenticationStore();
const routingStore = useRoutingStore();

const signIn = () => {
  authenticationStore.signInAsync();
  routingStore.redirectToRequestRoute();
};
</script>

<template>
  <div class="container mx-auto max-w-sm">
    <form class="mt-8">
      <div class="form-group">
        <div class="flex justify-between">
          <EnvelopeIcon class="h-8 w-8 text-blue-500 opacity-50" />
          <input
            id="email"
            v-model="email"
            type="text"
            placeholder="email"
            class="w-full px-4 py-2 border-b focus:outline-none focus:border-b-2 focus:border-indigo-500 placeholder-gray-500 placeholder-opacity-50"
          />
        </div>
        <p class="text-sm text-red-500 px-8 py-2">{{ emailError }}</p>
      </div>
      <div class="form-group mt-4">
        <div class="flex justify-between">
          <KeyIcon class="h-8 w-8 text-blue-500 opacity-50" />
          <input
            id="password"
            v-model="password"
            type="password"
            placeholder="password"
            class="w-full px-4 py-2 border-b focus:outline-none focus:border-b-2 focus:border-indigo-500 placeholder-gray-500 placeholder-opacity-50"
          />
        </div>

        <p class="text-sm text-red-500 px-8 py-2">{{ passwordError }}</p>
      </div>
      <div class="form-group mt-8">
        <button
          type="button"
          class="w-full bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-4 rounded disabled:opacity-50 disabled:bg-blue-500"
          :disabled="isInvalid()"
          @click="signIn"
        >
          ログイン
        </button>
      </div>
    </form>
  </div>
</template>
