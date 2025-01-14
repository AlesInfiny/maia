package com.dressca.applicationcore.order;

import java.util.Optional;

/**
 * 注文のリポジトリのインターフェースです。
 */
public interface OrderRepository {
  /**
   * 注文を追加します。
   * 
   * @param order 注文。
   * @return 追加された注文情報。
   */
  Order add(Order order);

  /**
   * 指定した ID の注文情報を取得します。
   * 
   * @param id ID 。
   * @return 注文情報。存在しない場合は空の Optional 。
   */
  Optional<Order> findById(long id);
}
