package com.dressca.applicationcore.applicationservice;

import com.dressca.applicationcore.constant.MessageIdConstants;
import com.dressca.applicationcore.order.Order;
import com.dressca.applicationcore.order.OrderNotFoundException;
import com.dressca.applicationcore.order.OrderRepository;
import com.dressca.systemcommon.constant.SystemPropertyConstants;
import lombok.AllArgsConstructor;
import java.util.Locale;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 注文に関連するビジネスユースケースを実現する Application Service です。
 */
@Service
@AllArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class OrderApplicationService {

  @Autowired
  private MessageSource messages;

  private OrderRepository orderRepository;

  private static final Logger apLog = LoggerFactory.getLogger(SystemPropertyConstants.APPLICATION_LOG_LOGGER);

  /**
   * 指定した注文 Id 、購入者 Id の注文情報を取得します。
   * 
   * @param orderId 注文 Id.
   * @param buyerId 購入者 Id.
   * @return 注文情報.
   * @throws OrderNotFoundException 注文情報が見つからない場合.
   */
  public Order getOrder(long orderId, String buyerId) throws OrderNotFoundException {

    apLog.debug(
        messages.getMessage(MessageIdConstants.D_ORDER_GET_ORDER,
            new Object[] { orderId, buyerId },
            Locale.getDefault()));

    Order order = this.orderRepository.findById(orderId)
        .orElseThrow(() -> new OrderNotFoundException(null, orderId, buyerId));
    if (!order.getBuyerId().equals(buyerId)) {
      throw new OrderNotFoundException(null, orderId, buyerId);
    }

    return order;
  }
}
