import { beforeAll, describe, expect, it } from 'vitest'
import { validationItems } from '@/authentication/validation/validation-items'
import { customErrorMap } from '@/system-common/validation/zod-settings'
import { z } from 'zod'

beforeAll(() => {
  z.setErrorMap(customErrorMap)
})

describe('validation-items', () => {
  it('メールアドレス形式を検証できる', async () => {
    const result = await validationItems().email.safeParseAsync('invalid-email')

    expect(result.success).toBe(false)
    expect(result.error?.issues[0]?.message).toBe('メールアドレスの形式で入力してください。')
  })
})
