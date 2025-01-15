import { HttpResponse, http } from 'msw';
import type {
  PostCatalogItemRequest,
  PutCatalogItemRequest,
} from '@/generated/api-client';
import { HttpStatusCode } from 'axios';
import { pagedListCatalogItem, catalogItems } from '../data/catalogItems';

type GetCatalogItemParams = {
  catalogItemId: string;
};

export const catalogItemsHandlers = [
  http.get('/api/catalog-items', () => {
    return HttpResponse.json(pagedListCatalogItem, {
      status: HttpStatusCode.Ok,
    });
  }),
  http.post<never, PostCatalogItemRequest, never>(
    '/api/catalog-items',
    async () => {
      return new HttpResponse(null, { status: HttpStatusCode.Created });
    },
  ),
  http.get<
    GetCatalogItemParams,
    never,
    never,
    '/api/catalog-items/:catalogItemId'
  >('/api/catalog-items/:catalogItemId', async ({ params }) => {
    const { catalogItemId } = params;
    const item = catalogItems.find(
      (items) => items.id === Number(catalogItemId),
    );
    return HttpResponse.json(item, { status: HttpStatusCode.Ok });
  }),
  http.delete('/api/catalog-items/:catalogItemId', async () => {
    return new HttpResponse(null, { status: HttpStatusCode.NoContent });
  }),
  http.put<never, PutCatalogItemRequest, never>(
    '/api/catalog-items/:catalogItemId',
    async () => {
      return new HttpResponse(null, { status: HttpStatusCode.NoContent });
    },
  ),
];
