package com.dressca.infrastructure.repository.mybatis;

import com.dressca.applicationcore.baskets.Basket;
import com.dressca.applicationcore.baskets.BasketRepository;
import com.dressca.infrastructure.repository.mybatis.generated.entity.BasketEntity;
import com.dressca.infrastructure.repository.mybatis.generated.entity.BasketEntityExample;
import com.dressca.infrastructure.repository.mybatis.generated.entity.BasketItemEntity;
import com.dressca.infrastructure.repository.mybatis.generated.entity.BasketItemEntityExample;
import com.dressca.infrastructure.repository.mybatis.generated.mapper.BasketItemMapper;
import com.dressca.infrastructure.repository.mybatis.generated.mapper.BasketMapper;
import com.dressca.infrastructure.repository.mybatis.mapper.JoinedBasketMapper;
import com.dressca.infrastructure.repository.mybatis.translator.EntityTranslator;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

/**
 * 買い物かごのリポジトリです。
 */
@Repository
@RequiredArgsConstructor
public class MybatisBasketRepository implements BasketRepository {

  private final BasketMapper basketMapper;
  private final BasketItemMapper basketItemMapper;
  private final JoinedBasketMapper joinedBasketMapper;

  @Override
  public Optional<Basket> findById(UUID id) {
    return Optional.ofNullable(joinedBasketMapper.findById(id));
  }

  @Override
  public Optional<Basket> findByBuyerId(UUID buyerId) {
    return Optional.ofNullable(joinedBasketMapper.findByBuyerId(buyerId));
  }

  @Override
  public Basket add(Basket basket) {
    BasketEntity row = EntityTranslator.createBasketEntity(basket);
    basketMapper.insert(row);

    // 子要素（ BasketItem ）の追加
    List<BasketItemEntity> itemRows = basket.getItems().stream()
        .map(EntityTranslator::createBasketItemEntity).collect(Collectors.toList());
    for (BasketItemEntity itemRow : itemRows) {
      basketItemMapper.insert(itemRow);
    }

    return joinedBasketMapper.findById(row.getId());
  }

  @Override
  public void remove(Basket basket) {
    BasketEntityExample basketExample = new BasketEntityExample();
    basketExample.createCriteria().andBuyerIdEqualTo(basket.getBuyerId());

    List<BasketEntity> baskets = basketMapper.selectByExample(basketExample);
    baskets.stream().map(BasketEntity::getId).distinct().forEach(this::removeBasketItem);

    basketMapper.deleteByExample(basketExample);
  }

  @Override
  public void update(Basket basket) {
    BasketEntity row = EntityTranslator.createBasketEntity(basket);
    basketMapper.updateByPrimaryKey(row);

    // 子要素（ BasketItem ）の削除
    removeBasketItem(basket.getId());
    // 子要素（ BasketItem ）の更新
    // 削除された BasketItem にも対応できるように DELETE-INSERT する
    basket.getItems().stream().map(EntityTranslator::createBasketItemEntity)
        .forEach(basketItemMapper::insert);
  }

  private void removeBasketItem(UUID basketId) {
    BasketItemEntityExample basketItemExample = new BasketItemEntityExample();
    basketItemExample.createCriteria().andBasketIdEqualTo(basketId);
    basketItemMapper.deleteByExample(basketItemExample);
  }
}
