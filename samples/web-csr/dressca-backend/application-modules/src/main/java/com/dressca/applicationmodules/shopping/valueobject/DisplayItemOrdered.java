package com.dressca.applicationmodules.shopping.valueobject;

import java.util.UUID;
import lombok.NonNull;

/**
 * 注文された陳列品を管理する値オブジェクトです。
 *
 * <ul>
 * <li>この値オブジェクトは、注文時点での陳列品エンティティのスナップショットです。</li>
 * <li>注文確定後に陳列品情報が変更されたとしても、注文情報は変更されるべきではないためです。</li>
 * </ul>
 */
public record DisplayItemOrdered(UUID displayItemId, @NonNull String productName,
    @NonNull String productCode) {
}
