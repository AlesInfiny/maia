package com.dressca.applicationcore.baskets;

import java.util.Optional;

/**
 * 買い物かごのリポジトリのインターフェースです。
 */
public interface BasketRepository {

  /**
   * 指定した ID の買い物かごを取得します。
   *
   * @param id 買い物かご ID 。
   * @return 条件に一致する買い物かご。
   */
  Optional<Basket> findById(long id);

  /**
   * 指定した購入者 ID の買い物かごを取得します。
   * 
   * @param buyerId 購入者 ID 。
   * @return 条件に一致する買い物かご。
   */
  Optional<Basket> findByBuyerId(String buyerId);

  /**
   * 買い物かごを追加します。
   * 
   * @param basket 買い物かご。
   * @return 追加した買い物かご。
   */
  Basket add(Basket basket);

  /**
   * 買い物かごを削除します。
   * 
   * @param basket 買い物かご。
   */
  void remove(Basket basket);

  /**
   * 買い物かごを更新します。
   * 
   * @param basket 買い物かご。
   */
  void update(Basket basket);
}
