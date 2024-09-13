import { HttpResponse, http } from 'msw';
import type {
  PostCatalogItemRequest,
  PutCatalogItemRequest,
} from '@/generated/api-client';
import { HttpStatusCode } from './httpStatusCode';
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
    async ({ request }) => {
      const body = await request.json();
      let status;
      switch (body.name) {
        case '400':
          status = HttpStatusCode.BadRequest;
          break;
        case '401':
          status = HttpStatusCode.Unauthorized;
          break;
        case '500':
          status = HttpStatusCode.InternalServerError;
          break;
        default:
          status = HttpStatusCode.Created;
          break;
      }
      return new HttpResponse(null, { status });
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
  http.delete('/api/catalog-items/:catalogItemId', async ({ params }) => {
    const { catalogItemId } = params;
    let status;
    switch (catalogItemId) {
      case '400':
        status = HttpStatusCode.BadRequest;
        break;
      case '401':
        status = HttpStatusCode.Unauthorized;
        break;
      case '404':
        status = HttpStatusCode.NotFound;
        break;
      case '500':
        status = HttpStatusCode.InternalServerError;
        break;
      default:
        status = HttpStatusCode.NoContent;
        break;
    }
    return new HttpResponse(null, { status });
  }),
  http.put<never, PutCatalogItemRequest, never>(
    '/api/catalog-items/:catalogItemId',
    async ({ request }) => {
      const body = await request.json();
      let status;
      switch (body.name) {
        case '400':
          status = HttpStatusCode.BadRequest;
          break;
        case '401':
          status = HttpStatusCode.Unauthorized;
          break;
        case '404':
          status = HttpStatusCode.NotFound;
          break;
        case '409':
          status = HttpStatusCode.Conflict;
          break;
        case '500':
          status = HttpStatusCode.InternalServerError;
          break;
        default:
          status = HttpStatusCode.NoContent;
          break;
      }
      return new HttpResponse(null, { status });
    },
  ),
];
