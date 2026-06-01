import { describe, expect, it } from 'vitest'
import { type Ref } from 'vue'
import { z } from 'zod'
import { i18n } from '@/locales/i18n'
import { ValidationItems } from '@/validation/validation-items'

const setLocale = (locale: string) => {
  ;(i18n.global.locale as unknown as Ref<string>).value = locale
}

describe('Authentication validation', () => {
  it('日本語で必須メッセージを返す', async () => {
    setLocale('ja')
    const { email, required } = ValidationItems()
    const schema = z.object({
      email: z.string().pipe(required).pipe(email),
      password: z.string().pipe(required),
    })

    const result = await schema.safeParseAsync({
      email: '',
      password: '',
    })

    expect(result.success).toBe(false)
    expect(result.error?.flatten().fieldErrors.email).toContain('値を入力してください')
    expect(result.error?.flatten().fieldErrors.password).toContain('値を入力してください')
  })

  it('日本語でメール形式メッセージを返す', async () => {
    setLocale('ja')
    const { email, required } = ValidationItems()
    const schema = z.object({
      email: z.string().pipe(required).pipe(email),
      password: z.string().pipe(required),
    })

    const result = await schema.safeParseAsync({
      email: 'invalid-email',
      password: 'aaa',
    })

    expect(result.success).toBe(false)
    expect(result.error?.issues[0]?.message).toBe('メールアドレスの形式で入力してください')
  })

  it('英語で必須メッセージを返す', async () => {
    setLocale('en')
    const { email, required } = ValidationItems()
    const schema = z.object({
      email: z.string().pipe(required).pipe(email),
      password: z.string().pipe(required),
    })

    const result = await schema.safeParseAsync({
      email: '',
      password: '',
    })

    expect(result.success).toBe(false)
    expect(result.error?.flatten().fieldErrors.email).toContain('this field is required')
    expect(result.error?.flatten().fieldErrors.password).toContain('this field is required')
  })
})

it('英語でメール形式メッセージを返す', async () => {
  setLocale('en')
  const { email, required } = ValidationItems()
  const schema = z.object({
    email: z.string().pipe(required).pipe(email),
    password: z.string().pipe(required),
  })

  const result = await schema.safeParseAsync({
    email: 'invalid-email',
    password: 'aaa',
  })

  expect(result.success).toBe(false)
  expect(result.error?.issues[0]?.message).toBe('invalid email format')
})
