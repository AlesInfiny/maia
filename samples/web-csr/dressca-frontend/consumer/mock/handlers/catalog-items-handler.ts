import { HttpResponse, http } from 'msw';
import { HttpStatusCode } from 'axios';
import { pagedListCatalogItem } from '../data/catalog-items';

export const catalogItemsHandlers = [
  http.get('/api/catalog-items', () => {
    return HttpResponse.json(pagedListCatalogItem, {
      status: HttpStatusCode.Ok,
    });
  }),
];
