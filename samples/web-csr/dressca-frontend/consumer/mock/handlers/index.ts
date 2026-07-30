import { assetsHandlers } from './assets-handler'
import { basketsHandlers } from './baskets-handler'
import { displayItemsHandlers } from './display-items-handler'
import { displayItemBrandsHandlers } from './display-item-brands-handler'
import { displayItemCategoriesHandlers } from './display-item-categories-handler'
import { orderingHandlers } from './ordering-handler'

export const handlers = [
  ...assetsHandlers,
  ...basketsHandlers,
  ...displayItemsHandlers,
  ...displayItemBrandsHandlers,
  ...displayItemCategoriesHandlers,
  ...orderingHandlers,
]
