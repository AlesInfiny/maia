package com.dressca.infrastructure.repository.mybatis;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import lombok.AllArgsConstructor;

/**
 * 買い物かごのリポジトリです。
 */
@Repository
@AllArgsConstructor
public class MybatisBasketRepository implements BasketRepository {

  @Autowired
  BasketMapper basketMapper;

  @Autowired
  BasketItemMapper basketItemMapper;

  @Autowired
  JoinedBasketMapper joinedBasketMapper;

  @Override
  public Optional<Basket> findById(long id) {
    return Optional.ofNullable(joinedBasketMapper.findById(id));
  }

  @Override
  public Optional<Basket> findByBuyerId(String buyerId) {
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

    // 子要素（ BasketItem ）の削除
    List<BasketEntity> baskets = basketMapper.selectByExample(basketExample);
    baskets.stream().mapToLong(BasketEntity::getId).distinct().forEach(id -> removeBasketItem(id));

    basketMapper.deleteByExample(basketExample);
  }

  @Override
  public void update(Basket basket) {
    BasketEntity row = EntityTranslator.createBasketEntity(basket);
    basketMapper.updateByPrimaryKey(row);

    // 子要素（ BasketItem ）の更新
    // 削除された BasketItem にも対応できるように DELETE-INSERT する
    removeBasketItem(basket.getId());
    basket.getItems().stream().map(EntityTranslator::createBasketItemEntity)
        .forEach(basketItemMapper::insert);
  }

  private void removeBasketItem(long basketId) {
    BasketItemEntityExample basketItemExample = new BasketItemEntityExample();
    basketItemExample.createCriteria().andBasketIdEqualTo(basketId);
    basketItemMapper.deleteByExample(basketItemExample);
  }
}
