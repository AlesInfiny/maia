package com.dressca.batch.job.catalog;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import com.dressca.applicationcore.catalog.CatalogItem;

/**
 * CatalogItemの商品名をバッチ出力用に先頭10文字にするprocessor。
 */
@Component
public class CatalogItemProcessor implements ItemProcessor<CatalogItem, CatalogItem> {

  @Override
  public CatalogItem process(final CatalogItem catalogItem) throws Exception {
    String name = catalogItem.getName();
    // 商品名が10文字超える場合、先頭10文字にする
    if (name.length() > 10) {
      name = name.substring(0, 10);
    }
    CatalogItem renamed = new CatalogItem();
    BeanUtils.copyProperties(catalogItem, renamed);
    renamed.setName(name);
    return renamed;
  }
}