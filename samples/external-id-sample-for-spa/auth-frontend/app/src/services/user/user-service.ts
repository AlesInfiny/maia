import { useUserStore } from '@/stores/user/user'

/**
 * 認証済みの場合にユーザー情報を取得します。
 * @returns Promise<void>
 */
export async function fetchUser() {
  const userStore = useUserStore()
  await userStore.fetchUserResponse()
}
