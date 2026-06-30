package com.dressca.infrastructure.repository.mybatis.mapper;

import com.dressca.applicationcore.order.Order;
import com.dressca.applicationcore.order.OrderItem;
import java.util.List;
import java.util.UUID;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 注文情報のテーブルにアクセスするためのマッパーのインターフェースです。
 */
@Mapper
public interface JoinedOrderMapper {
  /**
   * 注文を登録します。
   * 
   * @param order 注文。
   */
  void add(@Param("order") Order order);

  /**
   * 注文明細を登録します。
   * 
   * @param orderId 注文 ID 。
   * @param orderItems 注文明細一覧。
   */
  void addItems(@Param("orderId") UUID orderId, @Param("orderItems") List<OrderItem> orderItems);

  /**
   * ID を条件に注文を取得します。
   * 
   * @param id 注文 ID 。
   * @return 注文。
   */
  Order findById(@Param("id") UUID id);
}
