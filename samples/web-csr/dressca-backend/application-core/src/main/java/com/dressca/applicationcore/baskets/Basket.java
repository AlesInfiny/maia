package com.dressca.applicationcore.baskets;

import java.math.BigDecimal;
import java.util.UUID;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import com.dressca.applicationcore.accounting.Account;
import com.dressca.applicationcore.accounting.AccountItem;
import com.dressca.systemcommon.util.UuidGenerator;
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
   * カタログアイテムを追加します。
   * 
   * @param catalogItemId カタログアイテム ID 。
   * @param unitPrice 単価。
   * @param quantity 数量。
   */
  public void addItem(UUID catalogItemId, BigDecimal unitPrice, int quantity) {
    Optional<BasketItem> existingItem =
        this.items.stream().filter(item -> item.getCatalogItemId().equals(catalogItemId))
            .findFirst();

    existingItem.ifPresentOrElse(item -> item.addQuantity(quantity),
        () -> this.items.add(new BasketItem(UuidGenerator.generate(), id, catalogItemId, unitPrice,
            quantity)));
  }

  /**
   * 数量が 0 のカタログアイテムを削除します。
   */
  public void removeEmptyItems() {
    this.items.removeIf(item -> item.getQuantity() == 0);
  }

  /**
   * 指定した ID のカタログアイテムが買い物かごに存在するかを判定します。
   * 
   * @param catalogItemId カタログアイテム ID 。
   * @return 買い物かごに存在する場合は true 、存在しない場合は false 。
   */
  public boolean isInCatalogItem(UUID catalogItemId) {
    return this.items.stream().anyMatch(item -> item.getCatalogItemId().equals(catalogItemId));
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
