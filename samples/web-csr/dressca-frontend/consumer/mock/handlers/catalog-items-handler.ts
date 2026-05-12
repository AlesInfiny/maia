import { HttpResponse, http } from 'msw'
import { HttpStatusCode } from 'axios'
import { getCatalogItemsByQueryResponse } from '../data/catalog-items'

export const catalogItemsHandlers = [
  http.get('/api/catalog-items', () => {
    return HttpResponse.json(getCatalogItemsByQueryResponse, {
      status: HttpStatusCode.Ok,
    })
  }),
]
