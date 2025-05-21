import { http, HttpResponse } from 'msw';

export const handlers = [
  http.get('/api/servertime', () => {
    return HttpResponse.json({
      serverTime: new Date().toISOString(),
    });
  }),

  http.get('/api/users', () => {
    return HttpResponse.json({
      userId: 'mock-user-id-123',
    });
  }),
];
