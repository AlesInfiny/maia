/**
 * security コンテキストの公開APIです。
 * pages や他コンテキストは、このモジュール経由でのみ security を参照します。
 */
export {
  useSignIn,
  type SignIn,
  type SignInForm,
  type SignInOutcome,
} from './authentication/composables/use-sign-in'
export { default as AuthenticationMenu } from './authentication/components/AuthenticationMenu.vue'
export { createAuthenticationGuard } from './authentication/guards/authentication-guard'
