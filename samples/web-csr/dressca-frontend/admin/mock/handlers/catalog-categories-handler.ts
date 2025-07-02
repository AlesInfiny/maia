import { HttpStatusCode } from 'axios'
import { HttpResponse, http } from 'msw'
import { catalogCategories } from '../data/catalog-categories'

export const catalogCategoriesHandlers = [
  http.get('/api/catalog-categories', () => {
    return HttpResponse.json(catalogCategories, { status: HttpStatusCode.Ok })
  }),
]
