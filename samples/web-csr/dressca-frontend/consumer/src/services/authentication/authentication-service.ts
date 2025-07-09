import { useAuthenticationStore } from '@/stores/authentication/authentication'

export function signIn() {
  const authenticationStore = useAuthenticationStore()
  authenticationStore.signIn()
}
