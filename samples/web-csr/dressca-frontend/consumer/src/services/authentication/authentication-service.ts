import { useAuthenticationStore } from '@/stores/authentication/authentication'

export function authenticationService() {
  const signIn = () => {
    const authenticationStore = useAuthenticationStore()
    authenticationStore.signIn()
  }

  const isAuthenticated = (): boolean => {
    const authenticationStore = useAuthenticationStore()
    const result = authenticationStore.isAuthenticated
    return result
  }

  return {
    signIn,
    isAuthenticated,
  }
}
