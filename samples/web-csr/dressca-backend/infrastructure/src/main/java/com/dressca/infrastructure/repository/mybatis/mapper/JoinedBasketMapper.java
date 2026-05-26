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
  Basket findById(@Param("id") UUID id);

  Basket findByBuyerId(@Param("buyerId") UUID buyerId);
}
