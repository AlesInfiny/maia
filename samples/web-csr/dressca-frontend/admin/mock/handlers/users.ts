import { HttpResponse, http } from 'msw';
import type { GetLoginUserResponse } from '@/generated/api-client';
import { HttpStatusCode } from 'axios';
import { Roles } from '@/shared/constants/roles';

const user: GetLoginUserResponse = {
  userName: 'admin@example.com',
  roles: [Roles.ADMIN],
};

export const usersHandlers = [
  http.get('/api/users', () => {
    return HttpResponse.json(user, { status: HttpStatusCode.Ok });
  }),
];
