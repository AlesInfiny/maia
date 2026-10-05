import { describe, expect, it } from 'vitest'
import { router } from '@/app/router'

describe('画面ファイルの配置から生成されるルート定義', () => {
  it.each([
    ['/', '/', false, false],
    ['/authentication/login', '/authentication/login', false, false],
    ['/basket', '/basket', false, false],
    ['/ordering/checkout', '/ordering/checkout', true, true],
    ['/ordering/done/1', '/ordering/done/[orderId]', true, true],
    ['/error', '/error', false, false],
    ['/not-exists', '/[...path]', false, false],
  ])(
    'URL_%s_はルート名_%s_に解決され_認証の要否は_%s_画面内遷移の要否は_%s',
    (url, name, requiresAuth, requiresInAppNavigation) => {
      // Arrange
      // Act
      const route = router.resolve(url)
      // Assert
      expect(route.name).toBe(name)
      expect(route.meta.requiresAuth !== false).toBe(requiresAuth)
      expect(route.meta.requiresInAppNavigation === true).toBe(requiresInAppNavigation)
    },
  )
})
