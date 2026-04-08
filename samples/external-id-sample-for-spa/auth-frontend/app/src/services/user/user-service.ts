import { useUserStore } from '@/stores/user/user'

/**
 * 認証済みの場合にユーザー情報を取得し、ユーザーストアに反映します。
 * @returns 非同期処理の完了を表す Promise。
 */
export async function fetchUser() {
  const userStore = useUserStore()
  await userStore.fetchUserResponse()
}
