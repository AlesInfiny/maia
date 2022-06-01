package com.dressca.applicationcore.order;

import java.util.Optional;

/**
 * 注文リポジトリ。
 */
public interface OrderRepository {
  /**
   * 注文を追加します。
   * 
   * @param order 注文
   * @return 追加された注文情報
   */
  Order add(Order order);

  /**
   * 指定した Id の注文情報を取得します。
   * 
   * @param id Id
   * @return 注文情報. 存在しない場合は空のOptional.
   */
  Optional<Order> findById(long id);
}
