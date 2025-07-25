import { catalogItemsHandlers } from './catalog-items-handler'
import { catalogBrandsHandlers } from './catalog-brands-handler'
import { catalogCategoriesHandlers } from './catalog-categories-handler'
import { assetsHandlers } from './assets-handler'
import { usersHandlers } from './users-handler'

export const handlers = [
  ...catalogItemsHandlers,
  ...catalogBrandsHandlers,
  ...catalogCategoriesHandlers,
  ...assetsHandlers,
  ...usersHandlers,
]
