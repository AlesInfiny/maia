import { assetsHandlers } from './assets-handler'
import { basketsHandlers } from './baskets-handler'
import { catalogItemsHandlers } from './catalog-items-handler'
import { catalogBrandsHandlers } from './catalog-brands-handler'
import { catalogCategoriesHandlers } from './catalog-categories-handler'
import { orderingHandlers } from './ordering-handler'

export const handlers = [
  ...assetsHandlers,
  ...basketsHandlers,
  ...catalogItemsHandlers,
  ...catalogBrandsHandlers,
  ...catalogCategoriesHandlers,
  ...orderingHandlers,
]
