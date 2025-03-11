import { catalogItemsHandlers } from './handlers/catalog-items-handler';
import { catalogBrandsHandlers } from './handlers/catalog-brands-handler';
import { catalogCategoriesHandlers } from './handlers/catalog-categories-handler';
import { assetsHandlers } from './handlers/assets-handler';
import { usersHandlers } from './handlers/users-handler';

export const handlers = [
  ...catalogItemsHandlers,
  ...catalogBrandsHandlers,
  ...catalogCategoriesHandlers,
  ...assetsHandlers,
  ...usersHandlers,
];
