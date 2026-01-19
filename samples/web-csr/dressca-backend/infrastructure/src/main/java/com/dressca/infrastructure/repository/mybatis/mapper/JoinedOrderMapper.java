package com.dressca.infrastructure.repository.mybatis.mapper;

import java.util.List;
import com.dressca.applicationcore.order.Order;
import com.dressca.applicationcore.order.OrderItem;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 注文情報のテーブルにアクセスするためのマッパーのインターフェースです。
 */
@Mapper
public interface JoinedOrderMapper {
  /**
   * 注文を追加します。
   * 
   * @param order 注文。
   */
  void add(@Param("order") Order order);

  /**
   * 注文商品を追加します。
   * 
   * @param orderId 注文 ID 。
   * @param orderItems 注文商品リスト。
   */
  void addItems(@Param("orderId") long orderId, @Param("orderItems") List<OrderItem> orderItems);

  /**
   * 指定した ID の注文情報を取得します。
   * 
   * @param id ID 。
   * @return 注文情報。
   */
  Order findById(@Param("id") long id);
}
