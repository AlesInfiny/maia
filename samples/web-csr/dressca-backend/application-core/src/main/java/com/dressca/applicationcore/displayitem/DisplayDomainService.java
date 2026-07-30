package com.dressca.applicationcore.displayitem;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * 陳列品に関するドメインサービスです。
 */
@Service
@RequiredArgsConstructor
public class DisplayDomainService {
  private final DisplayRepository displayRepository;

  /**
   * 指定した陳列品 ID のうち、存在する陳列品の一覧を取得します。
   *
   * @param displayItemIds 陳列品 ID のリスト。
   * @return 存在する陳列品の一覧。
   */
  public List<DisplayItem> getExistDisplayItems(List<UUID> displayItemIds) {
    return this.displayRepository.findByDisplayItemIdIn(displayItemIds);
  }

  /**
   * 指定した陳列品 ID がリポジトリ内にすべて存在するかを取得します。
   *
   * @param displayItemIds 陳列品 ID のリスト。
   * @return すべて存在する場合は true 、一部でも不在の場合は false 。
   */
  public boolean existAll(List<UUID> displayItemIds) {
    List<DisplayItem> items = this.displayRepository.findByDisplayItemIdIn(displayItemIds);
    List<UUID> notExistDisplayItemIds = displayItemIds.stream()
        .filter(displayItemId -> !this.existDisplayItemIdInItems(items, displayItemId))
        .collect(Collectors.toList());

    return notExistDisplayItemIds.isEmpty();
  }

  /**
   * 指定した ID の陳列品が、削除済み陳列品を含むリポジトリ内に存在するかどうかを示す真理値を取得します。
   *
   * @param displayItemId 陳列品 ID 。
   * @return 指定した陳列品がリポジトリ内に存在する場合は true 、存在しない場合は false 。
   */
  public boolean existDisplayItemIncludingDeleted(UUID displayItemId) {
    return !this.displayRepository.findByDisplayItemIdInIncludingDeleted(List.of(displayItemId))
        .isEmpty();
  }

  private boolean existDisplayItemIdInItems(List<DisplayItem> items, UUID displayItemId) {
    return items.stream().anyMatch(displayItem -> displayItem.getId().equals(displayItemId));
  }
}
