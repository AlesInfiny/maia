import { HttpStatusCode } from 'axios'
import { HttpResponse, http } from 'msw'
import { displayCategories } from '../data/display-categories'

export const displayCategoriesHandlers = [
  http.get('/api/display-categories', () => {
    return HttpResponse.json(displayCategories, { status: HttpStatusCode.Ok })
  }),
]
