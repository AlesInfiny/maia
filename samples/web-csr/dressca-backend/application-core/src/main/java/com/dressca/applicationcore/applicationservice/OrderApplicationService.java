package com.dressca.applicationcore.applicationservice;

import com.dressca.applicationcore.constant.MessageIdConstants;
import com.dressca.applicationcore.order.Order;
import com.dressca.applicationcore.order.OrderNotFoundException;
import com.dressca.applicationcore.order.OrderRepository;
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

  public Order getOrder(UUID orderId, UUID buyerId) throws OrderNotFoundException {
    apLog.debug(messages.getMessage(MessageIdConstants.D_ORDER_GET_ORDER,
        new Object[] {orderId, buyerId}, Locale.getDefault()));

    Order order = this.orderRepository.findById(orderId)
        .orElseThrow(() -> new OrderNotFoundException(null, orderId, buyerId));
    if (!order.getBuyerId().equals(buyerId)) {
      throw new OrderNotFoundException(null, orderId, buyerId);
    }

    return order;
  }
}
