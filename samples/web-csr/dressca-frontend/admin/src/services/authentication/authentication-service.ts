import { useAuthenticationStore } from '@/stores/authentication/authentication'

/**
 * アプリケーションにログインします。
 */
export async function loginAsync() {
  const authenticationStore = useAuthenticationStore()
  await authenticationStore.signInAsync()
}

/**
 * アプリケーションからログアウトします。
 */
export function logout() {
  const authenticationStore = useAuthenticationStore()
  authenticationStore.signOut()
}
