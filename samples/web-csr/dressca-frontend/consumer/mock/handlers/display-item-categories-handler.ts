import { HttpStatusCode } from 'axios'
import { HttpResponse, http } from 'msw'
import { displayItemCategories } from '../data/display-item-categories'

export const displayItemCategoriesHandlers = [
  http.get('/api/display-item-categories', () => {
    return HttpResponse.json(displayItemCategories, { status: HttpStatusCode.Ok })
  }),
]
