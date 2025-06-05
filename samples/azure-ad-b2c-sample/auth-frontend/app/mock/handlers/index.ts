import { HttpStatusCode } from 'axios';
import { http, HttpResponse } from 'msw';

export const handlers = [
  http.get('/api/servertime', () => {
    return HttpResponse.json(
      { serverTime: new Date().toISOString() },
      { status: HttpStatusCode.Ok },
    );
  }),

  http.get('/api/users', () => {
    return HttpResponse.json(
      { userId: 'mock-user-id-123' },
      { status: HttpStatusCode.InternalServerError },
    );
  }),
];
