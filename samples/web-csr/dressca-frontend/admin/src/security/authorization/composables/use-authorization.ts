import { useAuthenticationStore } from '../stores/authentication'
import type { Roles } from '../constants/roles'

/**
 * 認可のユースケースです。
 */
export interface Authorization {
  /**
   * ログイン中のユーザーが、指定したロールに属するかどうかを判定します。
   * 画面の描画中に呼び出すと、ロールの変化に追随して再描画されます。
   */
  isInRole: (role: Roles) => boolean
}

/**
 * ログイン中のユーザーの権限を判定するユースケースを提供します。
 * 権限によって表示を制御する画面で使用します。
 * @returns 認可のユースケース。
 */
export function useAuthorization(): Authorization {
  const authenticationStore = useAuthenticationStore()
  return {
    isInRole: (role: Roles) => authenticationStore.isInRole(role),
  }
}
