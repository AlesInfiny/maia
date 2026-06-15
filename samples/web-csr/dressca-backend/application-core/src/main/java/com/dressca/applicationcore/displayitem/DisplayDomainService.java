package com.dressca.applicationcore.displayitem;

import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;

/**
 * 掲載品のドメインサービスです。
 */
@RequiredArgsConstructor
public class DisplayDomainService {
  private final DisplayRepository displayRepository;

  /**
   * 指定した掲載品 ID のうち、存在する掲載品の一覧を取得します。
   * 
   * @param displayItemIds 掲載品 ID のリスト。
   * @return 存在する掲載品の一覧。
   */
  public List<DisplayItem> getExistDisplayItems(List<Long> displayItemIds) {
    return this.displayRepository.findByDisplayItemIdIn(displayItemIds);
  }

  /**
   * 指定した ID の掲載品が、削除済み掲載品を含むリポジトリ内に存在するかどうかを示す真理値を取得します。
   * 
   * @param displayItemId 掲載品 ID 。
   * @return 指定した掲載品がリポジトリ内に存在する場合は true 、存在しない場合は false 。
   */
  public boolean existDisplayItemIncludingDeleted(long displayItemId) {
    return this.displayRepository.findByIdIncludingDeleted(displayItemId) != null;
  }

  /**
   * 指定した ID がリポジトリ内にすべて存在するかを取得します。
   * 
   * @param displayItemIds 掲載品 ID のリスト。
   * @return すべて存在する場合は true 、一部でも不在の場合は false 。
   */
  public boolean existAll(List<Long> displayItemIds) {
    List<DisplayItem> items = this.displayRepository.findByDisplayItemIdIn(displayItemIds);
    List<Long> notExistDisplayItemIds = displayItemIds.stream()
        .filter(displayItemId -> !this.existDisplayItemIdInItems(items, displayItemId))
        .collect(Collectors.toList());

    return notExistDisplayItemIds.isEmpty();
  }

  private boolean existDisplayItemIdInItems(List<DisplayItem> items, long displayItemId) {
    return items.stream().anyMatch(displayItem -> displayItem.getId() == displayItemId);
  }
}
