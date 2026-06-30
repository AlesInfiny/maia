import { describe, expect, it } from 'vitest'
import { ValidationItems } from '@/validation/validation-items'
import { i18n } from '@/locales/i18n'

describe('validation-items', () => {
  it('メールアドレス形式を検証できる', async () => {
    i18n.global.locale.value = 'ja'
    const { email } = ValidationItems()
    const result = await email.safeParseAsync('invalid-email')

    expect(result.success).toBe(false)
    expect(result.error?.issues[0]?.message).toBe('メールアドレスの形式で入力してください')
  })

  it('必須入力を検証できる', async () => {
    i18n.global.locale.value = 'ja'
    const { required } = ValidationItems()
    const result = await required().safeParseAsync('')

    expect(result.success).toBe(false)
    expect(result.error?.issues[0]?.message).toBe('値を入力してください')
  })
})
