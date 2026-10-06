import { describe, it, expect, beforeEach } from 'vitest'
import { createPinia, setActivePinia } from 'pinia'
import { useAuthenticationStore } from '../../stores/authentication'
import { Roles } from '../../constants/roles'
import { useAuthorization } from '../use-authorization'

beforeEach(() => {
  sessionStorage.clear()
  setActivePinia(createPinia())
})

describe('useAuthorization', () => {
  it('管理者ロールを持つ_管理者ロールに属すると判定する', () => {
    // Arrange
    useAuthenticationStore().userRoles = [Roles.ADMIN]
    const { isInRole } = useAuthorization()
    // Act
    const result = isInRole(Roles.ADMIN)
    // Assert
    expect(result).toBe(true)
  })

  it('管理者ロールを持たない_管理者ロールに属さないと判定する', () => {
    // Arrange
    useAuthenticationStore().userRoles = ['ROLE_GUEST']
    const { isInRole } = useAuthorization()
    // Act
    const result = isInRole(Roles.ADMIN)
    // Assert
    expect(result).toBe(false)
  })
})
