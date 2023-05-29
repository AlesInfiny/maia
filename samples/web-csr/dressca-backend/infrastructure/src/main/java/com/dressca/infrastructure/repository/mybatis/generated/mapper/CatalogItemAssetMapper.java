package com.dressca.infrastructure.repository.mybatis.generated.mapper;

import com.dressca.infrastructure.repository.mybatis.generated.entity.CatalogItemAssetEntity;
import com.dressca.infrastructure.repository.mybatis.generated.entity.CatalogItemAssetEntityExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface CatalogItemAssetMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table catalog_item_assets
     *
     * @mbg.generated Mon May 29 17:01:29 JST 2023
     */
    long countByExample(CatalogItemAssetEntityExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table catalog_item_assets
     *
     * @mbg.generated Mon May 29 17:01:29 JST 2023
     */
    int deleteByExample(CatalogItemAssetEntityExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table catalog_item_assets
     *
     * @mbg.generated Mon May 29 17:01:29 JST 2023
     */
    int deleteByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table catalog_item_assets
     *
     * @mbg.generated Mon May 29 17:01:29 JST 2023
     */
    int insert(CatalogItemAssetEntity row);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table catalog_item_assets
     *
     * @mbg.generated Mon May 29 17:01:29 JST 2023
     */
    int insertSelective(CatalogItemAssetEntity row);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table catalog_item_assets
     *
     * @mbg.generated Mon May 29 17:01:29 JST 2023
     */
    List<CatalogItemAssetEntity> selectByExample(CatalogItemAssetEntityExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table catalog_item_assets
     *
     * @mbg.generated Mon May 29 17:01:29 JST 2023
     */
    CatalogItemAssetEntity selectByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table catalog_item_assets
     *
     * @mbg.generated Mon May 29 17:01:29 JST 2023
     */
    int updateByExampleSelective(@Param("row") CatalogItemAssetEntity row, @Param("example") CatalogItemAssetEntityExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table catalog_item_assets
     *
     * @mbg.generated Mon May 29 17:01:29 JST 2023
     */
    int updateByExample(@Param("row") CatalogItemAssetEntity row, @Param("example") CatalogItemAssetEntityExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table catalog_item_assets
     *
     * @mbg.generated Mon May 29 17:01:29 JST 2023
     */
    int updateByPrimaryKeySelective(CatalogItemAssetEntity row);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table catalog_item_assets
     *
     * @mbg.generated Mon May 29 17:01:29 JST 2023
     */
    int updateByPrimaryKey(CatalogItemAssetEntity row);
}