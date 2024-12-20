import type { GetCatalogBrandsResponse } from '@/generated/api-client';
import { HttpResponse, http } from 'msw';

const catalogBrands: GetCatalogBrandsResponse[] = [
  {
    id: 1,
    name: '高級なブランド',
  },
  {
    id: 2,
    name: 'カジュアルなブランド',
  },
  {
    id: 3,
    name: 'ノーブランド',
  },
];

export const catalogBrandsHandlers = [
  http.get('/api/catalog-brands', () => {
    return HttpResponse.json(catalogBrands, { status: 200 });
  }),
];
