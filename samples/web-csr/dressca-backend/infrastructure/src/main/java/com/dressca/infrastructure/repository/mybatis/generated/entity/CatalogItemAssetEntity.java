package com.dressca.infrastructure.repository.mybatis.generated.entity;

public class CatalogItemAssetEntity {
    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column CATALOG_ITEM_ASSETS.ID
     *
     * @mbg.generated Tue May 31 02:29:01 JST 2022
     */
    private Long id;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column CATALOG_ITEM_ASSETS.ASSET_CODE
     *
     * @mbg.generated Tue May 31 02:29:01 JST 2022
     */
    private String assetCode;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column CATALOG_ITEM_ASSETS.CATALOG_ITEM_ID
     *
     * @mbg.generated Tue May 31 02:29:01 JST 2022
     */
    private Long catalogItemId;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column CATALOG_ITEM_ASSETS.ID
     *
     * @return the value of CATALOG_ITEM_ASSETS.ID
     *
     * @mbg.generated Tue May 31 02:29:01 JST 2022
     */
    public Long getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column CATALOG_ITEM_ASSETS.ID
     *
     * @param id the value for CATALOG_ITEM_ASSETS.ID
     *
     * @mbg.generated Tue May 31 02:29:01 JST 2022
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column CATALOG_ITEM_ASSETS.ASSET_CODE
     *
     * @return the value of CATALOG_ITEM_ASSETS.ASSET_CODE
     *
     * @mbg.generated Tue May 31 02:29:01 JST 2022
     */
    public String getAssetCode() {
        return assetCode;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column CATALOG_ITEM_ASSETS.ASSET_CODE
     *
     * @param assetCode the value for CATALOG_ITEM_ASSETS.ASSET_CODE
     *
     * @mbg.generated Tue May 31 02:29:01 JST 2022
     */
    public void setAssetCode(String assetCode) {
        this.assetCode = assetCode;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column CATALOG_ITEM_ASSETS.CATALOG_ITEM_ID
     *
     * @return the value of CATALOG_ITEM_ASSETS.CATALOG_ITEM_ID
     *
     * @mbg.generated Tue May 31 02:29:01 JST 2022
     */
    public Long getCatalogItemId() {
        return catalogItemId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column CATALOG_ITEM_ASSETS.CATALOG_ITEM_ID
     *
     * @param catalogItemId the value for CATALOG_ITEM_ASSETS.CATALOG_ITEM_ID
     *
     * @mbg.generated Tue May 31 02:29:01 JST 2022
     */
    public void setCatalogItemId(Long catalogItemId) {
        this.catalogItemId = catalogItemId;
    }
}