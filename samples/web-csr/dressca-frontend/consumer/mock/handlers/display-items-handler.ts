import { HttpResponse, http } from 'msw'
import { HttpStatusCode } from 'axios'
import { pagedListDisplayItem } from '../data/display-items'

export const displayItemsHandlers = [
  http.get('/api/display-items', () => {
    return HttpResponse.json(pagedListDisplayItem, {
      status: HttpStatusCode.Ok,
    })
  }),
]
