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

  void add(@Param("order") Order order);

  void addItems(@Param("orderId") long orderId, @Param("orderItems") List<OrderItem> orderItems);

  Order findById(@Param("id") long id);
}
