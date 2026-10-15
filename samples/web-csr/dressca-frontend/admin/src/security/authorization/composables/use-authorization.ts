import { computed, type ComputedRef } from 'vue'
import { useAuthenticationStore } from '../stores/authentication'
import { Roles } from '../constants/roles'

/**
 * 認可のユースケースです。
 * ロールの定数は公開せず、権限の判定結果だけを公開します。
 */
export interface Authorization {
  /**
   * ログイン中のユーザーが管理者ロールに属するかどうかです。
   * ロールの変化に追随します。
   */
  isAdmin: ComputedRef<boolean>
}

/**
 * ログイン中のユーザーの権限を判定するユースケースを提供します。
 * 権限によって表示を制御する画面で使用します。
 * @returns 認可のユースケース。
 */
export function useAuthorization(): Authorization {
  const authenticationStore = useAuthenticationStore()
  return {
    isAdmin: computed(() => authenticationStore.isInRole(Roles.ADMIN)),
  }
}
