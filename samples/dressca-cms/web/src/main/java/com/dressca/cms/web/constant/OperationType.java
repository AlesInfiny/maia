package com.dressca.cms.web.constant;

import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import com.dressca.cms.announcement.applicationcore.constant.OperationTypeConstants;
import lombok.Getter;

/**
 * 操作種別の選択肢です。
 */
@Getter
public enum OperationType {

  /** 作成の操作種別です。 */
  CREATE(OperationTypeConstants.CREATE, "作成"),
  /** 更新の操作種別です。 */
  UPDATE(OperationTypeConstants.UPDATE, "更新"),
  /** 削除の操作種別です。 */
  DELETE(OperationTypeConstants.DELETE, "削除");

  private final int value;
  private final String label;

  /**
   * 操作種別の値とラベルのマップです。
   */
  public static final Map<Integer, String> OPERATION_TYPE_LABEL_MAP = Collections
      .unmodifiableMap(Stream.of(OperationType.values())
          .collect(Collectors.toMap(OperationType::getValue, OperationType::getLabel)));

  /**
   * 値とラベルを指定して {@link OperationType} を初期化します。
   *
   * @param value 操作種別の値。
   * @param label 操作種別のラベル。
   */
  OperationType(int value, String label) {
    this.value = value;
    this.label = label;
  }
}