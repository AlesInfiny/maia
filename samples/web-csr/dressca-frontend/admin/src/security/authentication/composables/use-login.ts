import { computed, reactive } from 'vue'
import { useField, useForm } from 'vee-validate'
import { toTypedSchema } from '@vee-validate/zod'
import { z } from 'zod'
import {
  useUnexpectedErrorOutcome,
  type UnexpectedErrorOutcome,
} from '@/system-common/error-handler/unexpected-error-outcome'
import { loginAsync } from '../services/authentication-service'
import { validationItems } from '../validation/validation-items'

/**
 * ログインの入力フォームです。
 * 入力項目は双方向にバインドできます。
 */
export interface LoginForm {
  userName: string
  password: string
  /** 入力項目ごとの検証エラーのメッセージです。 */
  readonly errors: { userName: string | undefined; password: string | undefined }
  /** すべての入力項目が検証に合格しているかどうかです。 */
  readonly isValid: boolean
}

/**
 * ログインした結果です。
 */
export type LoginOutcome = { kind: 'loggedIn' } | UnexpectedErrorOutcome

/**
 * ログインのユースケースです。
 */
export interface Login {
  /** ログインの入力フォームです。 */
  form: LoginForm
  /** 入力フォームの内容でログインします。 */
  login: () => Promise<LoginOutcome>
}

/**
 * ログインのユースケースを提供します。
 * vee-validate を使用するため、コンポーネントの setup の中で呼び出します。
 * @returns ログインのユースケース。
 */
export function useLogin(): Login {
  const handleUnexpectedError = useUnexpectedErrorOutcome()
  const { requiredEmail, required } = validationItems()
  const { meta } = useForm({
    validationSchema: toTypedSchema(
      z.object({
        userName: requiredEmail('ユーザー名は必須です。'),
        password: required('パスワードは必須です。'),
      }),
    ),
    initialValues: { userName: '', password: '' },
  })
  const { value: userName, errorMessage: userNameError } = useField<string>('userName')
  const { value: password, errorMessage: passwordError } = useField<string>('password')

  const form: LoginForm = reactive({
    userName,
    password,
    errors: computed(() => ({ userName: userNameError.value, password: passwordError.value })),
    isValid: computed(() => meta.value.valid),
  })

  /**
   * 入力フォームの内容でログインします。
   * @returns ログインした結果。
   */
  async function login(): Promise<LoginOutcome> {
    try {
      await loginAsync()
      return { kind: 'loggedIn' }
    } catch (error) {
      return handleUnexpectedError(error)
    }
  }

  return { form, login }
}
