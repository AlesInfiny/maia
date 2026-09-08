import type { GetLoginUserResponse } from '@/system-common/generated/api-client'
import { Roles } from '@/authentication/constants/roles'

export const user: GetLoginUserResponse = {
  userName: 'admin@example.com',
  roles: [Roles.ADMIN],
}
