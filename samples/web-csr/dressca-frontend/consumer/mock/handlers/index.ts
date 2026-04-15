import { basketsHandlers } from '../basket/handlers/baskets-handler'
import { catalogBrandsHandlers } from '../catalog/handlers/catalog-brands-handler'
import { catalogCategoriesHandlers } from '../catalog/handlers/catalog-categories-handler'
import { catalogItemsHandlers } from '../catalog/handlers/catalog-items-handler'
import { assetsHandlers } from '../common/handlers/assets-handler'
import { orderingHandlers } from '../ordering/handlers/ordering-handler'

export const handlers = [
  ...assetsHandlers,
  ...basketsHandlers,
  ...catalogItemsHandlers,
  ...catalogBrandsHandlers,
  ...catalogCategoriesHandlers,
  ...orderingHandlers,
]
