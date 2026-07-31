import { HttpResponse, http } from 'msw'
import { HttpStatusCode } from 'axios'
import type { PostCatalogItemRequest, PutCatalogItemRequest } from '@/generated/api-client'
import { pagedListCatalogItem, catalogItems } from '../data/catalog-items'

export const catalogItemsHandlers = [
  http.get('/api/catalog-items', () => {
    return HttpResponse.json(pagedListCatalogItem, {
      status: HttpStatusCode.Ok,
    })
  }),
  http.post<never, PostCatalogItemRequest, never>('/api/catalog-items', () => {
    return new HttpResponse(null, { status: HttpStatusCode.Created })
  }),
  http.get('/api/catalog-items/:catalogItemId', ({ params }) => {
    const { catalogItemId } = params
    const item = catalogItems.find((catalogItem) => catalogItem.id === catalogItemId)
    return HttpResponse.json(item, { status: HttpStatusCode.Ok })
  }),
  http.delete('/api/catalog-items/:catalogItemId', () => {
    return new HttpResponse(null, { status: HttpStatusCode.NoContent })
  }),
  http.put<never, PutCatalogItemRequest, never>('/api/catalog-items/:catalogItemId', () => {
    return new HttpResponse(null, { status: HttpStatusCode.NoContent })
  }),
]
