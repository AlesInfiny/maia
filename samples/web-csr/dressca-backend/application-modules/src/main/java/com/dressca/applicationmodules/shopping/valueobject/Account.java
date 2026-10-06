package com.dressca.applicationmodules.shopping.valueobject;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

/**
 * 会計情報を表現する値オブジェクトです。
 */
public record Account(List<AccountItem> accountItems) {
  public static final double CONSUMPTION_TAX_RATE = 0.1;

  /**
   * 会計アイテムのリストを指定して {@link Account} クラスのインスタンスを初期化します。
   *
   * @param accountItems 会計アイテムのリスト。
   */
  public Account {
    accountItems = List.copyOf(accountItems);
  }

  /**
   * 会計アイテムの合計金額を取得します。この計算結果には、消費税や送料は含まれません。
   * 
   * @return 会計アイテムの合計金額。
   */
  public BigDecimal itemTotalPrice() {
    return accountItems.stream().map(AccountItem::subTotal).reduce(BigDecimal.ZERO,
        BigDecimal::add);
  }

  /**
   * 税抜きの送料を取得します。
   * 
   * <ul>
   * <li>送料は会計アイテムの合計金額が 5,000 円以上で無料になります。</li>
   * <li>それ以外の場合 500 円です。</li>
   * <li>ただし、会計アイテムが登録されていない場合は 0 円を返します。</li>
   * </ul>
   * 
   * @return 送料。
   */
  public BigDecimal deliveryCharge() {
    int deliveryCharge = 0;
    if (!accountItems.isEmpty() && itemTotalPrice().compareTo(BigDecimal.valueOf(5000)) < 0) {
      deliveryCharge = 500;
    }
    return BigDecimal.valueOf(deliveryCharge);
  }

  /**
   * 消費税額の合計を取得します。
   * 
   * <ul>
   * <li>会計アイテムの合計金額に送料を加えた額に、消費税率を乗じて計算します。</li>
   * <li>0 円未満の端数は切り捨てます。</li>
   * </ul>
   * 
   * @return 消費税額。
   */
  public BigDecimal consumptionTax() {
    return itemTotalPrice().add(deliveryCharge()).multiply(BigDecimal.valueOf(CONSUMPTION_TAX_RATE))
        .setScale(0, RoundingMode.FLOOR);
  }

  /**
   * 税込みの合計金額を取得します。
   * 
   * @return 合計金額。
   */
  public BigDecimal totalPrice() {
    return itemTotalPrice().add(deliveryCharge()).add(consumptionTax());
  }
}
