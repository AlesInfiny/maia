import { catalogItemsHandlers } from './handlers/catalogItems';
import { catalogBrandsHandlers } from './handlers/catalogBrands';
import { catalogCategoriesHandlers } from './handlers/catalogCategories';
import { assetsHandlers } from './handlers/assets';
import { usersHandlers } from './handlers/users';

export const handlers = [
  ...catalogItemsHandlers,
  ...catalogBrandsHandlers,
  ...catalogCategoriesHandlers,
  ...assetsHandlers,
  ...usersHandlers,
];
