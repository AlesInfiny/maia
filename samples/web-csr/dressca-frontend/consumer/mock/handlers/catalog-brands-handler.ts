import { HttpStatusCode } from 'axios'
import { HttpResponse, http } from 'msw'
import { displayBrands } from '../data/catalog-brands'

export const displayBrandsHandlers = [
  http.get('/api/display-brands', () => {
    return HttpResponse.json(displayBrands, { status: HttpStatusCode.Ok })
  }),
]
