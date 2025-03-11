import { assetsHandlers } from './handlers/assets-handler';
import { basketsHandlers } from './handlers/baskets-handler';
import { catalogItemsHandlers } from './handlers/catalog-items-handler';
import { catalogBrandsHandlers } from './handlers/catalog-brands-handler';
import { catalogCategoriesHandlers } from './handlers/catalog-categories-handler';
import { orderingHandlers } from './handlers/ordering-handler';

export const handlers = [
  ...assetsHandlers,
  ...basketsHandlers,
  ...catalogItemsHandlers,
  ...catalogBrandsHandlers,
  ...catalogCategoriesHandlers,
  ...orderingHandlers,
];
