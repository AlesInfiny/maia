import { describe, expect, it } from 'vitest'
import { catalogItemZodSchema, validationItems } from '@/validation/validation-items'

describe('validation-items', () => {
  it('メールアドレス形式を検証できる', async () => {
    const result = await validationItems.email.safeParseAsync('invalid-email')

    expect(result.success).toBe(false)
    expect(result.error?.issues[0]?.message).toBe('メールアドレスの形式で入力してください。')
  })

  it('カタログアイテムの必須項目を検証できる', async () => {
    const result = await catalogItemZodSchema.safeParseAsync({
      itemName: '',
      itemDescription: '',
      price: '',
      productCode: '',
    })

    expect(result.success).toBe(false)
    expect(result.error?.flatten().fieldErrors.itemName).toContain('アイテム名は必須です。')
    expect(result.error?.flatten().fieldErrors.itemDescription).toContain('説明は必須です。')
    expect(result.error?.flatten().fieldErrors.price).toContain('単価は必須です。')
    expect(result.error?.flatten().fieldErrors.productCode).toContain('商品コードは必須です。')
  })

  it('カタログアイテムの形式制約を検証できる', async () => {
    const result = await catalogItemZodSchema.safeParseAsync({
      itemName: 'a'.repeat(257),
      itemDescription: 'a'.repeat(1025),
      price: '0',
      productCode: 'abc-123',
    })

    expect(result.success).toBe(false)
    expect(result.error?.flatten().fieldErrors.itemName).toContain(
      '256文字以下で入力してください。',
    )
    expect(result.error?.flatten().fieldErrors.itemDescription).toContain(
      '1024文字以下で入力してください。',
    )
    expect(result.error?.flatten().fieldErrors.price).toContain(
      '1以上の整数を半角数字で入力してください',
    )
    expect(result.error?.flatten().fieldErrors.productCode).toContain(
      '半角英数字で入力してください。',
    )
  })

  it.each(['-1', '1.5'])('不正な単価 %s を検証できる', async (price) => {
    const result = await catalogItemZodSchema.safeParseAsync({
      itemName: 'テストアイテム',
      itemDescription: 'テスト説明',
      price,
      productCode: 'ABC123',
    })

    expect(result.success).toBe(false)
    expect(result.error?.flatten().fieldErrors.price).toContain(
      '1以上の整数を半角数字で入力してください',
    )
  })

  it('単価にスペースのみ入力された場合を検証できる', async () => {
    const result = await catalogItemZodSchema.safeParseAsync({
      itemName: 'テストアイテム',
      itemDescription: 'テスト説明',
      price: ' ',
      productCode: 'ABC123',
    })

    expect(result.success).toBe(false)
    expect(result.error?.flatten().fieldErrors.price).toContain(
      '単価は必須です。',
    )
  })
})
