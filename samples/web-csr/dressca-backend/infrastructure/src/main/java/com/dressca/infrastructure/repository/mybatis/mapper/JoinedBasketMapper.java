package com.dressca.infrastructure.repository.mybatis.mapper;

import com.dressca.applicationcore.baskets.Basket;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 買い物かごのテーブルにアクセスするためのマッパーのインターフェースです。
 */
@Mapper
public interface JoinedBasketMapper {
  /**
   * 指定した ID の買い物かごを取得します。
   *
   * @param id 買い物かご ID 。
   * @return 条件に一致する買い物かご。
   */
  Basket findById(@Param("id") long id);

  /**
   * 指定した購入者 ID の買い物かごを取得します。
   * 
   * @param buyerId 購入者 ID 。
   * @return 条件に一致する買い物かご。
   */
  Basket findByBuyerId(@Param("buyerId") String buyerId);
}
