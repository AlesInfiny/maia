package com.dressca.applicationmodules.shopping.entity;

import com.dressca.applicationmodules.shopping.valueobject.Account;
import com.dressca.applicationmodules.shopping.valueobject.AccountItem;
import com.dressca.systemcommon.util.UuidGenerator;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

/**
 * 買い物かごの情報を表現するエンティティです。
 */
@Data
@NoArgsConstructor
public class Basket {
  private UUID id;
  private List<BasketItem> items = new ArrayList<>();
  @NonNull
  private UUID buyerId;

  /**
   * 購入者 ID を指定して、 {@link Basket} クラスのインスタンスを初期化します。
   * 
   * @param buyerId 購入者 ID 。
   */
  public Basket(@NonNull UUID buyerId) {
    this.id = UuidGenerator.generate();
    this.buyerId = buyerId;
  }

  /**
   * 買い物かご ID と購入者 ID を指定して、 {@link Basket} クラスのインスタンスを初期化します。
   * 
   * @param id 買い物かご ID 。
   * @param buyerId 購入者 ID 。
   */
  public Basket(UUID id, @NonNull UUID buyerId) {
    this.id = id;
    this.buyerId = buyerId;
  }

  /**
   * 陳列品を追加します。
   *
   * @param displayItemId 陳列品 ID 。
   * @param unitPrice 単価。
   * @param quantity 数量。
   */
  public void addItem(UUID displayItemId, BigDecimal unitPrice, int quantity) {
    Optional<BasketItem> existingItem = this.items.stream()
        .filter(item -> item.getDisplayItemId().equals(displayItemId)).findFirst();

    existingItem.ifPresentOrElse(item -> item.addQuantity(quantity), () -> this.items
        .add(new BasketItem(UuidGenerator.generate(), id, displayItemId, unitPrice, quantity)));
  }

  /**
   * 数量が 0 の陳列品を削除します。
   */
  public void removeEmptyItems() {
    this.items.removeIf(item -> item.getQuantity() == 0);
  }

  /**
   * 指定した ID の陳列品が買い物かごに存在するかを判定します。
   *
   * @param displayItemId 陳列品 ID 。
   * @return 買い物かごに存在する場合は true 、存在しない場合は false 。
   */
  public boolean isInDisplayItem(UUID displayItemId) {
    return this.items.stream().anyMatch(item -> item.getDisplayItemId().equals(displayItemId));
  }

  /**
   * 会計情報を取得します。
   * 
   * @return 会計情報。
   */
  public Account getAccount() {
    List<AccountItem> accountItems = this.items.stream()
        .map(basketItem -> new AccountItem(basketItem.getQuantity(), basketItem.getUnitPrice()))
        .collect(Collectors.toList());
    return new Account(accountItems);
  }
}
