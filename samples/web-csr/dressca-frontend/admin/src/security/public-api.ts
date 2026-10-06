/**
 * security コンテキストの公開APIです。
 * pages や他コンテキストは、このモジュール経由でのみ security を参照します。
 */
export {
  useLogin,
  type Login,
  type LoginForm,
  type LoginOutcome,
} from './authentication/composables/use-login'
export { useAuthenticationStore } from './authorization/stores/authentication'
export { Roles } from './authorization/constants/roles'
export { createAuthenticationGuard } from './authentication/guards/authentication-guard'
