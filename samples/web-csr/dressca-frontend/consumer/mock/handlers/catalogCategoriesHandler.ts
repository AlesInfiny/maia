import { HttpStatusCode } from 'axios';
import { HttpResponse, http } from 'msw';
import { catalogCategories } from '../data/catalogCategories';

export const catalogCategoriesHandlers = [
  http.get('/api/catalog-categories', () => {
    return HttpResponse.json(catalogCategories, { status: HttpStatusCode.Ok });
  }),
];
