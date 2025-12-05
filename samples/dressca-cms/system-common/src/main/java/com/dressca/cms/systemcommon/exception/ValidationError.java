package com.dressca.cms.systemcommon.exception;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * バリデーションエラーを表すクラスです。
 */
@Data
@AllArgsConstructor
public class ValidationError {
  private String fieldName;
  private String errorCode;
}
