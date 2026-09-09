import { beforeAll, describe, expect, it } from 'vitest'
import { validationItems } from '@/authentication/login/validation/validation-items'
import { customErrorMap } from '@/system-common/validation/zod-settings'
import { z } from 'zod'

beforeAll(() => {
  z.setErrorMap(customErrorMap)
})

describe('validation-items', () => {
  it('必須入力を検証できる', async () => {
    const { required } = validationItems()
    const result = await required('パスワードは必須です。').safeParseAsync('')

    expect(result.success).toBe(false)
    expect(result.error?.issues.map((issue) => issue.message)).toContain('パスワードは必須です。')
  })

  it('メールアドレス形式を検証できる', async () => {
    const { requiredEmail } = validationItems()
    const result = await requiredEmail().safeParseAsync('invalid-email')

    expect(result.success).toBe(false)
    expect(result.error?.issues.map((issue) => issue.message)).toContain(
      'メールアドレスの形式で入力してください。',
    )
  })

  it('メールアドレスの必須入力を検証できる', async () => {
    const { requiredEmail } = validationItems()
    const result = await requiredEmail('ユーザー名は必須です。').safeParseAsync('')

    expect(result.success).toBe(false)
    expect(result.error?.issues.map((issue) => issue.message)).toContain('ユーザー名は必須です。')
  })
})
