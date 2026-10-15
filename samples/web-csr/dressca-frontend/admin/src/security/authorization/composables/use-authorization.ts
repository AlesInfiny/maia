import { computed, type ComputedRef } from 'vue'
import { useAuthenticationStore } from '../stores/authentication'
import { Roles } from '../constants/roles'

export interface Authorization {
  isAdmin: ComputedRef<boolean>
}

/**
 * ログイン中のユーザーの権限を判定するユースケースを提供します。
 * @returns 認可のユースケース。
 */
export function useAuthorization(): Authorization {
  const authenticationStore = useAuthenticationStore()
  return {
    isAdmin: computed(() => authenticationStore.isInRole(Roles.ADMIN)),
  }
}
