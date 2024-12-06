import type { UserResponse } from '@/generated/api-client';
import { HttpResponse, http } from 'msw';

const user: UserResponse = { userId: 'DummyUserId' };

export const userHandlers = [
  http.get('/api/user', () => {
    return HttpResponse.json(user, { status: 200 });
  }),
];
