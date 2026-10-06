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
export { useAuthorization, type Authorization } from './authorization/composables/use-authorization'
export { Roles } from './authorization/constants/roles'
export { default as LoginMenu } from './authentication/components/LoginMenu.vue'
export { createAuthenticationGuard } from './authentication/guards/authentication-guard'
