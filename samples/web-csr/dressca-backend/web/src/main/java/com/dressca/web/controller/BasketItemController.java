package com.dressca.web.controller;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.dressca.applicationcore.accounting.Account;
import com.dressca.applicationcore.baskets.Basket;
import com.dressca.applicationcore.baskets.BasketApplicationService;
import com.dressca.applicationcore.baskets.BasketItem;
import com.dressca.applicationcore.catalog.CatalogDomainService;
import com.dressca.applicationcore.catalog.CatalogItem;
import com.dressca.applicationcore.catalog.CatalogItemAsset;
import com.dressca.applicationcore.catalog.CatalogRepository;
import com.dressca.web.controller.dto.AccountDto;
import com.dressca.web.controller.dto.BasketDto;
import com.dressca.web.controller.dto.BasketItemDto;
import com.dressca.web.controller.dto.CatalogItemDto;
import com.dressca.web.controller.dto.PostBasketItemsInputDto;
import com.dressca.web.controller.dto.PutBasketItemInputDto;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.AllArgsConstructor;

/**
 * BasketItem の情報にアクセスする API コントローラーです。
 */
@RestController
@RequestMapping("/api/basket-items")
@AllArgsConstructor
public class BasketItemController {
    
    private BasketApplicationService basketApplicationService;
    private CatalogDomainService catalogDomainService;
    private CatalogRepository catalogRepository;

    /**
     * 買い物かごアイテムの一覧を取得します。
     * @return 買い物かごアイテムの一覧
     */
    @GetMapping
    public ResponseEntity<BasketDto> getBasketItems() {
        // TODO: buyerId の取得が未実装
        String buyerId = null;
        Basket basket = basketApplicationService.getOrCreateBasketForUser(buyerId);
        List<Long> catalogItemIds = basket.getItems().stream()
            .map(basketItem -> basketItem.getCatalogItemId())
            .collect(Collectors.toList());
        List<CatalogItem> catalogItems = this.catalogRepository.findByCatalogItemIdIn(catalogItemIds);
        BasketDto basketDto = convertBasketDto(basket);
        
        for (BasketItemDto item : basketDto.getBasketItems()) {
            item.setCatalogItem(this.getCatalogItemDto(item.getCatalogItemId(), catalogItems));
        }
        return ResponseEntity.ok().body(basketDto);
    }

    /**
     * 買い物かごアイテム内の数量を変更します。
     * 買い物かご内に存在しないカタログアイテム ID は指定できません。
     * <p>
     * この API では、買い物かご内に存在する商品の数量を変更できます。
     * 買い物かご内に存在しないカタログアイテム Id を指定すると HTTP 400 を返却します。
     * またシステムに登録されていないカタログアイテム Id を指定した場合も HTTP 400 を返却します。
     * </p>
     * 
     * @param putBasketItems 変更する買い物かごアイテムのデータリスト
     * @return なし
     */
    public ResponseEntity<?> putBasketItem(List<PutBasketItemInputDto> putBasketItems) {
        if (putBasketItems.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        Map<Long, Integer> quantities = putBasketItems.stream()
            .collect(Collectors.toMap(PutBasketItemInputDto::getCatalogItemId, PutBasketItemInputDto::getQuentity));

        // 買い物かごに入っていないカタログアイテムが指定されていないか確認
        // TODO: buyerId の取得が未実装
        String buyerId = null;
        Basket basket = this.basketApplicationService.getOrCreateBasketForUser(buyerId);
        List<Long> notExistsInBasketCatalogIds = quantities.keySet().stream()
            .filter(catalogItemId -> !basket.isInCatalogItem(catalogItemId))
            .collect(Collectors.toList());
        if (!notExistsInBasketCatalogIds.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        // カタログリポジトリに存在しないカタログアイテムが指定されていないか確認
        if (!this.catalogDomainService.existAll(List.copyOf(quantities.keySet()))) {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.noContent().build();
    }

    /**
     * 買い物かごに商品を追加します。
     * <p>
     * この API では、システムに登録されていないカタログアイテム Id を指定した場合 HTTP 400 を返却します。
     * また買い物かごに追加していないカタログアイテムを指定した場合、その商品を買い物かごに追加します。
     * すでに買い物かごに追加されているカタログアイテムを指定した場合、指定した数量、買い物かご内の数量を追加します。
     * </p>
     * <p>
     * 買い物かご内のカタログアイテムの数量が 0 未満になるように減じることはできません。
     * 計算の結果数量が 0 未満になる場合 HTTP 500 を返却します。
     * </p>
     * @param postBasketItem 追加する商品の情報
     * @return なし
     */
    public ResponseEntity<?> postBasketItem(PostBasketItemsInputDto postBasketItem) {
        // TODO: 未実装
        return null;
    }

    /**
     * 買い物かごから指定したカタログアイテム Id の商品を削除します。
     * <p>
     * catalogItemId には買い物かご内に存在するカタログアイテム Id を指定してください。
     * カタログアイテム Id は 1 以上の整数です。
     * 0 以下の値を指定したり、整数値ではない値を指定した場合 HTTP 400 を返却します。
     * 買い物かご内に指定したカタログアイテムの商品が存在しない場合、 HTTP 404 を返却します。
     * </p>
     * @param catalogItemId カタログアイテム Id
     * @return なし
     */
    @DeleteMapping("{catalogItemId}")
    public ResponseEntity<?> deleteBasketItemAsync(@PathVariable("catalogItemId") long catalogItemId) {
        return null;
    }
    
    private BasketDto convertBasketDto(Basket basket) {
        if (basket == null) {
            return null;
        }
        Account account = basket.getAccount();
        
        AccountDto accountDto = new AccountDto(Account.CONSUMPTION_TAX_RATE, 
            account.getTotalPrice(), account.getDeliveryCharge(), account.getConsumptionTax(), 
            account.getTotalPrice());
        List<BasketItemDto> basketItems = basket.getItems().stream()
            .map(this::convertBasketItemDto)
            .collect(Collectors.toList());
        return new BasketDto(basket.getBuyerId(), accountDto, basketItems);
    }

    private BasketItemDto convertBasketItemDto(BasketItem basketItem) {
        if (basketItem == null) {
            return null;
        }

        return new BasketItemDto(basketItem.getCatalogItemId(), basketItem.getUnitPrice(), 
            basketItem.getQuantity(), basketItem.getSubtotal(), null);
    }

    private CatalogItemDto getCatalogItemDto(long catalogItemId, List<CatalogItem> catalogItems)  {
        CatalogItem catalogItem = catalogItems.stream()
            .filter(item -> item.getId() == catalogItemId)
            .findFirst().orElse(null);
        
        return convertCatalogItemDto(catalogItem);
    }

    private CatalogItemDto convertCatalogItemDto(CatalogItem catalogItem) {
        if (catalogItem == null) {
            return null;
        }

        List<String> assetCodes = catalogItem.getAssets().stream()
            .map(CatalogItemAsset::getAssetCode)
            .collect(Collectors.toList());

        return new CatalogItemDto(catalogItem.getId(), catalogItem.getName(), catalogItem.getProductCode(),
            assetCodes, catalogItem.getDescription(), catalogItem.getPrice(), catalogItem.getCatalogCategoryId(), 
            catalogItem.getCatalogBrandId());
    }
}
