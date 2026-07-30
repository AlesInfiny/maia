import { HttpStatusCode } from 'axios'
import { HttpResponse, http } from 'msw'
import { displayItemBrands } from '../data/display-item-brands'

export const displayBrandsHandlers = [
  http.get('/api/display-brands', () => {
    return HttpResponse.json(displayItemBrands, { status: HttpStatusCode.Ok })
  }),
]
