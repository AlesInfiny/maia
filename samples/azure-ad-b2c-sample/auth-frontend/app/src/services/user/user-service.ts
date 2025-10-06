import { useUserStore } from '@/stores/user/user'
import { authenticationService } from '@/services/authentication/authentication-service'

/**
 * 認証済みの場合にユーザー情報を取得し、ユーザーストアに反映します。
 * 認証されていない場合は何もせずに終了します。
 * @returns 非同期処理の完了を表す Promise。
 */
export async function fetchUser() {
  const userStore = useUserStore()
  if (!authenticationService.isAuthenticated()) {
    return
  }
  await userStore.fetchUserResponse()
}
