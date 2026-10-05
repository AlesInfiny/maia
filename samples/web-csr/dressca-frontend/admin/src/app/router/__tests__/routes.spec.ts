import { describe, expect, it } from 'vitest'
import { router } from '@/app/router'
import { routeNames } from '@/app/router/route-names'

describe('ルート表', () => {
  it.each([
    ['/', routeNames.home, true],
    ['/authentication/login', routeNames.login, false],
    ['/catalog/items', routeNames.catalogItems, true],
    ['/catalog/items/add', routeNames.catalogItemsAdd, true],
    ['/catalog/items/edit/1', routeNames.catalogItemsEdit, true],
    ['/error', routeNames.error, true],
    ['/not-exists', routeNames.notFound, true],
  ])('URL_%s_はルート名_%s_に解決され_認証の要否は_%s', (url, name, requiresAuth) => {
    // Arrange
    // Act
    const route = router.resolve(url)
    // Assert
    expect(route.name).toBe(name)
    expect(route.meta.requiresAuth !== false).toBe(requiresAuth)
  })
})
