package com.dressca.infrastructure.repository.mybatis.generated.entity;

public class CatalogCategoryEntity {
    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column catalog_categories.id
     *
     * @mbg.generated
     */
    private Long id;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column catalog_categories.name
     *
     * @mbg.generated
     */
    private String name;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column catalog_categories.id
     *
     * @return the value of catalog_categories.id
     *
     * @mbg.generated
     */
    public Long getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column catalog_categories.id
     *
     * @param id the value for catalog_categories.id
     *
     * @mbg.generated
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column catalog_categories.name
     *
     * @return the value of catalog_categories.name
     *
     * @mbg.generated
     */
    public String getName() {
        return name;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column catalog_categories.name
     *
     * @param name the value for catalog_categories.name
     *
     * @mbg.generated
     */
    public void setName(String name) {
        this.name = name;
    }
}