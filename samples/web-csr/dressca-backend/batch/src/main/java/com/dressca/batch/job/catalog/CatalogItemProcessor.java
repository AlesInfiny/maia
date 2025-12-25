package com.dressca.batch.job.catalog;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.BeanUtils;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import com.dressca.applicationcore.catalog.CatalogItem;

/**
 * {@link CatalogItem} の商品名をバッチ出力用に先頭 10 文字にする processor です。
 */
@Component
public class CatalogItemProcessor implements ItemProcessor<CatalogItem, CatalogItem> {

  @Override
  public CatalogItem process(@NonNull final CatalogItem catalogItem) throws Exception {
    String name = catalogItem.getName();
    // 商品名が 10 文字超える場合、先頭 10 文字にする
    if (name.length() > 10) {
      name = name.substring(0, 10);
    }
    CatalogItem renamed = new CatalogItem();
    BeanUtils.copyProperties(catalogItem, renamed);
    renamed.setName(name);
    return renamed;
  }
}
