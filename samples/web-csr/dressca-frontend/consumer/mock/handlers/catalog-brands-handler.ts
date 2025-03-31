import { HttpStatusCode } from 'axios';
import { HttpResponse, http } from 'msw';
import { catalogBrands } from '../data/catalog-brands';

export const catalogBrandsHandlers = [
  http.get('/api/catalog-brands', () => {
    return HttpResponse.json(catalogBrands, { status: HttpStatusCode.Ok });
  }),
];
