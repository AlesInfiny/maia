import { describe, expect, it } from 'vitest'
import { redirectHelper } from '@/system-common/helpers/redirect-helper'

describe('戻り先のパスの検証', () => {
  const { toSafeRedirectPath } = redirectHelper()

  it.each(['/', '/catalog/items', '/catalog/items/edit/1?tab=detail'])(
    'アプリケーション内のパス_%s_はそのまま返す',
    (value) => {
      // Arrange
      // Act
      const actual = toSafeRedirectPath(value)
      // Assert
      expect(actual).toBe(value)
    },
  )

  it.each([
    'https://example.com',
    '//example.com',
    '/\\example.com',
    // ブラウザーの URL パーサーはタブ・改行・復帰を取り除くため、 // と同じく外部のホストを指す
    '/\t/example.com',
    '/\n/example.com',
    '/\r/example.com',
    '/\t\\example.com',
    'catalog/items',
    '',
    undefined,
    null,
    ['/catalog/items'],
  ])('アプリケーション外を指す値_%s_は受け入れない', (value) => {
    // Arrange
    // Act
    const actual = toSafeRedirectPath(value)
    // Assert
    expect(actual).toBeUndefined()
  })
})
