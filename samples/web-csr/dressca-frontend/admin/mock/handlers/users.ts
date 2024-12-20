import { HttpResponse, http } from 'msw';
import type { GetLoginUserResponse } from '@/generated/api-client';

const user: GetLoginUserResponse = {
  userName: 'admin@example.com',
  roles: ['Admin'],
};

export const usersHandlers = [
  http.get('/api/users', () => {
    return HttpResponse.json(user, { status: 200 });
  }),
];
