import type { TimeResponse } from '@/generated/api-client';
import { HttpResponse, http } from 'msw';

const time: TimeResponse = { serverTime: '2024/12/01 12:00' };

export const timeHandlers = [
  http.get('/api/servertime', () => {
    return HttpResponse.json(time, { status: 200 });
  }),
];
