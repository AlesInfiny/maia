import { computed, reactive } from 'vue'
import { useField, useForm } from 'vee-validate'
import { toTypedSchema } from '@vee-validate/zod'
import { z } from 'zod'
import { ValidationItems } from '@/system-common/validation/validation-items'
import { authenticationService } from '../services/authentication-service'

/**
 * ログインの入力フォームです。
 * 入力項目は双方向にバインドできます。
 */
export interface SignInForm {
  email: string
  password: string
  /** 入力項目ごとの検証エラーのメッセージです。 */
  readonly errors: { email: string | undefined; password: string | undefined }
  /** すべての入力項目が検証に合格しているかどうかです。 */
  readonly isValid: boolean
}

/**
 * ログインした結果です。
 */
export type SignInOutcome = { kind: 'signedIn' }

/**
 * ログインのユースケースです。
 */
export interface SignIn {
  /** ログインの入力フォームです。 */
  form: SignInForm
  /** 入力フォームの内容でログインします。 */
  signIn: () => Promise<SignInOutcome>
}

/**
 * ログインのユースケースを提供します。
 * vee-validate を使用するため、コンポーネントの setup の中で呼び出します。
 * 検証のメッセージは呼び出し時点のロケールで確定します。
 * @returns ログインのユースケース。
 */
export function useSignIn(): SignIn {
  const { requiredEmail, required } = ValidationItems()
  const { meta } = useForm({
    validationSchema: toTypedSchema(
      z.object({
        email: requiredEmail(),
        password: required(),
      }),
    ),
    initialValues: { email: '', password: '' },
  })
  const { value: email, errorMessage: emailError } = useField<string>('email')
  const { value: password, errorMessage: passwordError } = useField<string>('password')

  const form: SignInForm = reactive({
    email,
    password,
    errors: computed(() => ({ email: emailError.value, password: passwordError.value })),
    isValid: computed(() => meta.value.valid),
  })

  /**
   * 入力フォームの内容でログインします。
   * @returns ログインした結果。
   */
  function signIn(): Promise<SignInOutcome> {
    authenticationService().signIn()
    return Promise.resolve({ kind: 'signedIn' })
  }

  return { form, signIn }
}
