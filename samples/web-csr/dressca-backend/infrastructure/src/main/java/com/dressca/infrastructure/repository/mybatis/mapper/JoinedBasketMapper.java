package com.dressca.infrastructure.repository.mybatis.mapper;

import com.dressca.applicationcore.baskets.Basket;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 買い物かごのテーブルにアクセスするためのマッパークラスです。
 */
@Mapper
public interface JoinedBasketMapper {

  Basket findById(@Param("id") long id);

  Basket findByBuyerId(@Param("buyerId") String buyerId);

}
