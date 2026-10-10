import { describe, expect, it } from 'vitest'
import { router } from '@/business-common/router'
import { routeNames } from '@/business-common/router/route-names'

describe('ルート表', () => {
  it.each([
    ['/', routeNames.displayItem, false, false],
    ['/authentication/login', routeNames.login, false, false],
    ['/basket', routeNames.basket, false, false],
    ['/ordering/checkout', routeNames.checkout, true, true],
    ['/ordering/done/1', routeNames.done, true, true],
    ['/error', routeNames.error, false, false],
    ['/not-exists', routeNames.notFound, false, false],
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
