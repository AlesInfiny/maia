import { useAuthenticationStore } from '@/stores/authentication/authentication';

/**
 * アプリケーションにログインします。
 */
export async function loginAsync() {
  const authenticationStore = useAuthenticationStore();
  await authenticationStore.signInAsync();
}

/**
 * アプリケーションからログアウトします。
 */
export async function logoutAsync() {
  const authenticationStore = useAuthenticationStore();
  await authenticationStore.signOutAsync();
}
