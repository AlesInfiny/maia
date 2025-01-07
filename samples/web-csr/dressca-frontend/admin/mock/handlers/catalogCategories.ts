import type { GetCatalogCategoriesResponse } from '@/generated/api-client';
import { HttpStatusCode } from 'axios';
import { HttpResponse, http } from 'msw';

const catalogCategories: GetCatalogCategoriesResponse[] = [
  {
    id: 1,
    name: '服',
  },
  {
    id: 2,
    name: 'バッグ',
  },
  {
    id: 3,
    name: 'シューズ',
  },
];

export const catalogCategoriesHandlers = [
  http.get('/api/catalog-categories', () => {
    return HttpResponse.json(catalogCategories, { status: HttpStatusCode.Ok });
  }),
];
