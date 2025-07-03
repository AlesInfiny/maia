import type { GetLoginUserResponse } from '@/generated/api-client'
import { Roles } from '@/shared/constants/roles'

export const user: GetLoginUserResponse = {
  userName: 'admin@example.com',
  roles: [Roles.ADMIN],
}
