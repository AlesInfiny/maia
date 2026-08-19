package com.dressca.applicationmodules.shopping;

import com.dressca.applicationmodules.shopping.entity.Order;
import com.dressca.applicationmodules.shopping.exception.OrderNotFoundException;
import com.dressca.applicationmodules.shopping.internal.domain.constant.ShoppingMessageIdConstants;
import com.dressca.applicationmodules.shopping.internal.domain.repository.OrderRepository;
import com.dressca.systemcommon.log.AbstractStructuredLogger;
import java.util.Locale;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 注文に関連するビジネスユースケースを実現するサービスです。
 */
@Service
@RequiredArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class OrderApplicationService {

  private final MessageSource messages;
  private final OrderRepository orderRepository;
  private final AbstractStructuredLogger apLog;

  /**
   * 指定した注文 ID 、購入者 ID の注文情報を取得します。
   * 
   * @param orderId 注文 ID 。
   * @param buyerId 購入者 ID 。
   * @return 注文情報。
   * @throws OrderNotFoundException 注文情報が見つからない場合。
   */
  public Order getOrder(UUID orderId, UUID buyerId) throws OrderNotFoundException {
    apLog.debug(messages.getMessage(ShoppingMessageIdConstants.D_ORDER_GET_ORDER,
        new Object[] {orderId, buyerId}, Locale.getDefault()));

    Order order = this.orderRepository.findById(orderId)
        .orElseThrow(() -> new OrderNotFoundException(null, orderId, buyerId));
    if (!order.getBuyerId().equals(buyerId)) {
      throw new OrderNotFoundException(null, orderId, buyerId);
    }

    return order;
  }
}
