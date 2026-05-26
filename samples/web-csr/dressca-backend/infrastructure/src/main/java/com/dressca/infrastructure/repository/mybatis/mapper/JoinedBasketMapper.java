package com.dressca.infrastructure.repository.mybatis.mapper;

import com.dressca.applicationcore.baskets.Basket;
import java.util.UUID;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 買い物かごのテーブルにアクセスするためのマッパーのインターフェースです。
 */
@Mapper
public interface JoinedBasketMapper {
  /**
   * ID を条件に買い物かごを取得します。
   * 
   * @param id 買い物かご ID 。
   * @return 買い物かご。
   */
  Basket findById(@Param("id") UUID id);

  /**
   * 購入者 ID を条件に買い物かごを取得します。
   * 
   * @param buyerId 購入者 ID 。
   * @return 買い物かご。
   */
  Basket findByBuyerId(@Param("buyerId") UUID buyerId);
}
