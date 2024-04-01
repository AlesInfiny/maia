package com.dressca.applicationcore.applicationservice;

import com.dressca.applicationcore.order.Order;
import com.dressca.applicationcore.order.OrderNotFoundException;
import com.dressca.applicationcore.order.OrderRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 注文に関連するビジネスユースケースを実現する Application Service です。
 */
@Service
@AllArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class OrderApplicationService {
  private OrderRepository orderRepository;

  /**
   * 指定した注文 Id 、購入者 Id の注文情報を取得します。
   * 
   * @param orderId 注文 Id.
   * @param buyerId 購入者 Id.
   * @return 注文情報.
   * @throws OrderNotFoundException 注文情報が見つからない場合.
   */
  public Order getOrder(long orderId, String buyerId) throws OrderNotFoundException {
    Order order = this.orderRepository.findById(orderId)
        .orElseThrow(() -> new OrderNotFoundException(null, orderId, buyerId));
    if (!order.getBuyerId().equals(buyerId)) {
      throw new OrderNotFoundException(null, orderId, buyerId);
    }

    return order;
  }

}
