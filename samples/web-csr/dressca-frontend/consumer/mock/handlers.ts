import { assetsHandlers } from './handlers/assetsHandler';
import { basketsHandlers } from './handlers/basketsHandler';
import { catalogItemsHandlers } from './handlers/catalogItemsHandler';
import { catalogBrandsHandlers } from './handlers/catalogBrandsHandler';
import { catalogCategoriesHandlers } from './handlers/catalogCategoriesHandler';
import { orderingHandlers } from './handlers/orderingHandler';

export const handlers = [
  ...assetsHandlers,
  ...basketsHandlers,
  ...catalogItemsHandlers,
  ...catalogBrandsHandlers,
  ...catalogCategoriesHandlers,
  ...orderingHandlers,
];
