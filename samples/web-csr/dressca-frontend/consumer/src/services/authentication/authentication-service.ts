import { useAuthenticationStore } from '@/stores/authentication/authentication'

export async function signInAsync() {
  const authenticationStore = useAuthenticationStore()
  authenticationStore.signInAsync()
}
