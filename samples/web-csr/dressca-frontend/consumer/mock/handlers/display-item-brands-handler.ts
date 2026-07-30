import { HttpStatusCode } from 'axios'
import { HttpResponse, http } from 'msw'
import { displayItemBrands } from '../data/display-item-brands'

export const displayItemBrandsHandlers = [
  http.get('/api/display-item-brands', () => {
    return HttpResponse.json(displayItemBrands, { status: HttpStatusCode.Ok })
  }),
]
