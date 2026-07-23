import { assetsHandlers } from './assets-handler'
import { basketsHandlers } from './baskets-handler'
import { displayItemsHandlers } from './display-items-handler'
import { displayBrandsHandlers } from './display-brands-handler'
import { displayCategoriesHandlers } from './display-categories-handler'
import { orderingHandlers } from './ordering-handler'

export const handlers = [
  ...assetsHandlers,
  ...basketsHandlers,
  ...displayItemsHandlers,
  ...displayBrandsHandlers,
  ...displayCategoriesHandlers,
  ...orderingHandlers,
]
