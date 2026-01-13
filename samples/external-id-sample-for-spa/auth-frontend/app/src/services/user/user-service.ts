import { useUserStore } from '@/stores/user/user'
import { authenticationService } from '@/services/authentication/authentication-service'

/**
 * 認証済みの場合にユーザー情報を取得します。
 * @returns Promise<void>
 */
export async function fetchUser() {
  const userStore = useUserStore()
  if (!authenticationService().isAuthenticated()) {
    return
  }
  await userStore.fetchUserResponse()
}
