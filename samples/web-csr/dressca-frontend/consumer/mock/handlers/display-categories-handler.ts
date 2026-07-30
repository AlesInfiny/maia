import { HttpStatusCode } from 'axios'
import { HttpResponse, http } from 'msw'
import { displayItemCategories } from '../data/display-item-categories'

export const displayCategoriesHandlers = [
  http.get('/api/display-categories', () => {
    return HttpResponse.json(displayItemCategories, { status: HttpStatusCode.Ok })
  }),
]
