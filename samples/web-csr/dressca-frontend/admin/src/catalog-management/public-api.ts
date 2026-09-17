/**
 * catalog-management コンテキストの公開APIです。
 * pages や他コンテキストは、このモジュール経由でのみ catalog-management を参照します。
 */
export {
  fetchCategoriesAndBrands,
  fetchItem,
  fetchItems,
  postCatalogItem,
  updateCatalogItem,
  deleteCatalogItem,
} from './catalog/services/catalog-service'
export {
  catalogItemTypedSchema,
  type CatalogItemFormValues,
} from './catalog/validation/validation-items'
export { catalogRouteNames } from './catalog/router/catalog-route-names'
export { default as ConfirmationModal } from './catalog/components/ConfirmationModal.vue'
export { default as NotificationModal } from './catalog/components/NotificationModal.vue'
