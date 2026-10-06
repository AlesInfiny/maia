/**
 * catalog-management コンテキストの公開APIです。
 * pages や他コンテキストは、このモジュール経由でのみ catalog-management を参照します。
 */
export {
  useCatalogItemEditor,
  type CatalogItemEditor,
  type CatalogItemEditorStatus,
  type CatalogItemForm,
  type CatalogItemSnapshot,
  type CatalogOption,
  type RemoveOutcome,
  type UpdateOutcome,
} from './catalog/composables/use-catalog-item-editor'
export {
  fetchCategoriesAndBrands,
  fetchItems,
  postCatalogItem,
} from './catalog/services/catalog-service'
export {
  catalogItemTypedSchema,
  type CatalogItemFormValues,
} from './catalog/validation/validation-items'
export { default as ConfirmationModal } from './catalog/components/ConfirmationModal.vue'
export { default as NotificationModal } from './catalog/components/NotificationModal.vue'
