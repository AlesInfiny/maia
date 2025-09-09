import { useUserStore } from '@/stores/user/user'
import { authenticationService } from '@/services/authentication/authentication-service'

export async function fetchUser() {
  const userStore = useUserStore()
  const isAuthenticated = authenticationService()
  if (!isAuthenticated) {
    return
  }
  await userStore.fetchUserResponse()
}
