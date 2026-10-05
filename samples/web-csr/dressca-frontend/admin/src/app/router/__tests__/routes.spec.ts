import { describe, expect, it } from 'vitest'
import { router } from '@/app/router'

describe('画面ファイルの配置から生成されるルート定義', () => {
  it.each([
    ['/', '/', true],
    ['/authentication/login', '/authentication/login', false],
    ['/catalog/items', '/catalog/items/', true],
    ['/catalog/items/add', '/catalog/items/add', true],
    ['/catalog/items/edit/1', '/catalog/items/edit/[itemId]', true],
    ['/error', '/error', true],
    ['/not-exists', '/[...path]', true],
  ])('URL_%s_はルート名_%s_に解決され_認証の要否は_%s', (url, name, requiresAuth) => {
    // Arrange
    // Act
    const route = router.resolve(url)
    // Assert
    expect(route.name).toBe(name)
    expect(route.meta.requiresAuth !== false).toBe(requiresAuth)
  })
})
