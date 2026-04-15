import { HttpResponse, http } from 'msw'
import { HttpStatusCode } from 'axios'
import { user } from '../data/users'

export const usersHandlers = [
  http.get('/api/users', () => {
    return HttpResponse.json(user, { status: HttpStatusCode.Ok })
  }),
]
